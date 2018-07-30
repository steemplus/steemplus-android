package steemplus.com.steemplus_android.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.checkerframework.checker.linear.qual.Linear;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import steemplus.com.steemplus_android.Adapters.WalletItemAdapter;
import steemplus.com.steemplus_android.Constants;
import steemplus.com.steemplus_android.MainActivity;
import steemplus.com.steemplus_android.Models.UserAccount;
import steemplus.com.steemplus_android.Models.WalletItem;
import steemplus.com.steemplus_android.R;
import steemplus.com.steemplus_android.Tools.AsyncGetJSON;
import steemplus.com.steemplus_android.taskCompleteListener;

/**
 * Created by quentin on 7/9/18.
 */

public class WalletFragment extends Fragment implements taskCompleteListener {

    private MainActivity activity;
    private taskCompleteListener<Object> parent;
    private WalletItemAdapter adapter;

    private ArrayList<WalletItem> walletItems = new ArrayList<>();
    private ArrayList<WalletItem> filteredWalletItems = new ArrayList<>();
    private boolean filtersSectionOpened = false;

    private ArrayList<String> filterTypes = new ArrayList<String>();
    private boolean hideSpam = false;
    private HashMap<String, Double> filterMin = new HashMap<String, Double>();
    private UserAccount activeUser;

    private ListView walletItemListView;
    private LinearLayout hideDisplayFilterBtn;
    private LinearLayout filters;
    private TextView hideDisplayFilterText;
    private ImageView hideDisplayFilterIcon;

    private Switch switchClaim;
    private Switch switchTransferTo;
    private Switch switchTransferFrom;
    private Switch switchHideSpan;

    private EditText minSBDEditText;
    private EditText minSteemEditText;
    private EditText minSPEditText;

    private TabHost tabHost;

    private String TAG_LOG = "WalletFragment";

    @Override
    public void onAttach(Context activity) {
        //  Log.d(TAG_LOG, "onAttach");
        super.onAttach(activity);
        this.activity= (MainActivity) activity;
        parent = (taskCompleteListener<Object>) activity;
        activeUser = this.activity.getActiveUser();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //   Log.d(TAG_LOG, "onCreate");
        super.onCreate(savedInstanceState);
        filterTypes.add("claim");
        filterTypes.add("transfer_to");
        filterTypes.add("transfer_from");
        filterMin.put("SBD", new Double(0));
        filterMin.put("SP", new Double(0));
        filterMin.put("STEEM", new Double(0));
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.wallet, container, false);

        tabHost = view.findViewById(R.id.wallet_tab_host);
        tabHost.setup();
        TabHost.TabSpec spec = tabHost.newTabSpec("Information");
        spec.setIndicator("Info");
        spec.setContent(R.id.tab_wallet_information);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("History");
        spec.setContent(R.id.tab_wallet_history);
        spec.setIndicator("History");
        tabHost.addTab(spec);

        walletItemListView = view.findViewById(R.id.list_items_wallet);
        hideDisplayFilterBtn = view.findViewById(R.id.filters_button);
        hideDisplayFilterText = view.findViewById(R.id.filters_button_text);
        hideDisplayFilterIcon = view.findViewById(R.id.filters_button_icon);
        filters = view.findViewById(R.id.filters);
        hideDisplayFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filtersSectionOpened)
                {
                    Toast.makeText(activity, "Close section", Toast.LENGTH_SHORT).show();
                    hideDisplayFilterText.setText(activity.getResources().getText(R.string.display_filters));
                    hideDisplayFilterIcon.setImageDrawable(activity.getResources().getDrawable(android.R.drawable.arrow_down_float));
                    filtersSectionOpened = !filtersSectionOpened;
                    filters.setVisibility(View.GONE);
                }
                else
                {
                    Toast.makeText(activity, "Open section", Toast.LENGTH_SHORT).show();
                    hideDisplayFilterText.setText(activity.getResources().getText(R.string.hide_filters));
                    hideDisplayFilterIcon.setImageDrawable(activity.getResources().getDrawable(android.R.drawable.arrow_up_float));
                    filtersSectionOpened = !filtersSectionOpened;
                    filters.setVisibility(View.VISIBLE);
                }
            }
        });
        switchClaim = view.findViewById(R.id.filter_claim);
        switchClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switchClaim.isChecked()) filterTypes.add("claim");
                else filterTypes.remove("claim");
                refreshView();
            }
        });
        switchTransferTo = view.findViewById(R.id.filter_transfer_to);
        switchTransferTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switchTransferTo.isChecked()) filterTypes.add("transfer_to");
                else filterTypes.remove("transfer_to");
                refreshView();
            }
        });
        switchTransferFrom = view.findViewById(R.id.filter_transfer_from);
        switchTransferFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switchTransferFrom.isChecked()) filterTypes.add("transfer_from");
                else filterTypes.remove("transfer_from");
                refreshView();
            }
        });
        switchHideSpan = view.findViewById(R.id.filter_hide_span);
        switchHideSpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switchHideSpan.isChecked()) hideSpam = true;
                else hideSpam = false;
                refreshView();
            }
        });

        minSBDEditText = view.findViewById(R.id.min_sbd_edit_text);
        minSBDEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0) filterMin.put("SBD", 0.0);
                else filterMin.put("SBD", Double.parseDouble(charSequence.toString()));
                refreshView();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        minSPEditText = view.findViewById(R.id.min_sp_edit_text);
        minSPEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0) filterMin.put("SP", 0.0);
                else filterMin.put("SP", Double.parseDouble(charSequence.toString()));
                refreshView();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        minSteemEditText = view.findViewById(R.id.min_steem_edit_text);
        minSteemEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0) filterMin.put("STEEM", 0.0);
                else filterMin.put("STEEM", Double.parseDouble(charSequence.toString()));
                refreshView();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        if(activeUser != null)
        {
            // Wallet info content
            ViewGroup walletInfoContainer = view.findViewById(R.id.tab_wallet_information);

            createWalletInfoRow(getString(R.string.steem_balance), activity.getActiveUser().getBalanceString(), walletInfoContainer);
            createWalletInfoRow(getString(R.string.sbd_balance), activity.getActiveUser().getSBDBalanceString(), walletInfoContainer);
            createWalletInfoRow(getString(R.string.vesting_shares), activity.getActiveUser().getVotingPowerString(activity.getDynamicGlobalProperty().getTotalVestingFundSteem().toReal(), activity.getDynamicGlobalProperty().getTotalVestingShares().toReal()), walletInfoContainer);
            createWalletInfoRow(getString(R.string.delegated_vesting_shares), activity.getActiveUser().getDelegatedVotingPowerString(activity.getDynamicGlobalProperty().getTotalVestingFundSteem().toReal(), activity.getDynamicGlobalProperty().getTotalVestingShares().toReal()), walletInfoContainer);
            createWalletInfoRow(getString(R.string.received_vesting_shares), activity.getActiveUser().getReceivedVotingPowerString(activity.getDynamicGlobalProperty().getTotalVestingFundSteem().toReal(), activity.getDynamicGlobalProperty().getTotalVestingShares().toReal()), walletInfoContainer);
            createWalletInfoRow(getString(R.string.savings_steem_balance), activity.getActiveUser().getSavingsSteemBalanceString(), walletInfoContainer);
            createWalletInfoRow(getString(R.string.savings_sbd_balance), activity.getActiveUser().getSavingsSBDBalanceString(), walletInfoContainer);
            createWalletInfoRow(getString(R.string.account_value), activity.getActiveUser().getAccountValueString(activity.getMarketPrices(), activity.getDynamicGlobalProperty()), walletInfoContainer);
        }
        return view;
    }

    private void createWalletInfoRow(String label, String value, ViewGroup walletInfoContainer)
    {
        LinearLayout walletInfoRow = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.wallet_info_row, walletInfoContainer, false);
        TextView walletInfoLabel = walletInfoRow.findViewById(R.id.wallet_info_label);
        walletInfoLabel.setText(label);
        TextView walletInfoValue = walletInfoRow.findViewById(R.id.wallet_info_value);
        walletInfoValue.setText(value);
        walletInfoContainer.addView(walletInfoRow);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        syncWalletDB();
    }

    private void syncWalletDB()
    {
        if(activity.getActiveUser() == null)
            Toast.makeText(activity, R.string.no_active_user, Toast.LENGTH_SHORT).show();
        else
                new AsyncGetJSON(activity, WalletFragment.this).execute(Constants.GET_STEEMPLUS_WALLET_HISTORY, Constants.GET_STEEMPLUS_WALLET_HISTORY_URL, activity.getActiveUser().getUsername());
    }

    private void setupList(JSONArray json)
    {
        try
        {
            for(int i = 0; i < json.length(); i++)
            {
                JSONObject obj = json.getJSONObject(i);
                WalletItem newWalletItem = new WalletItem(obj, activity.getDynamicGlobalProperty());
                walletItems.add(newWalletItem);
            }
            filteredWalletItems = (ArrayList<WalletItem>)walletItems.clone();
            adapter = new WalletItemAdapter(activity, R.layout.wallet_history_row, filteredWalletItems);
            walletItemListView.setAdapter(adapter);
        }
        catch(JSONException jsonException)
        {
            jsonException.printStackTrace();
        }
    }

    public void refreshView()
    {
        getFilteredList();
        adapter.notifyDataSetChanged();
    }

    public void getFilteredList()
    {
        for(int i = filteredWalletItems.size()-1; i >= 0; i--) filteredWalletItems.remove(i);
        for (WalletItem item : walletItems)
        {
            if(!filterTypes.contains(item.getType())) continue;
            if(hideSpam && (item.getAmount() <= 0.001)) continue;
            String type = item.getType();
            boolean test = type.equals("claim");
            if(test)
            {
                if((filterMin.get("SBD") > item.getRewardSbd()) && filterMin.get("STEEM") > item.getRewardSteem() && filterMin.get("SP") > item.getRewardVests())
                    continue;
            }
            else
            {
                String symbol = item.getAmountSymbol();
                if(filterMin.get(symbol) > item.getAmount())
                    continue;
            }
            filteredWalletItems.add(item);
        }
    }

    @Override
    public void onTaskComplete(Object result, String action) {
        switch (action)
        {
            case Constants.GET_STEEMPLUS_WALLET_HISTORY:
            {
                JSONArray json = (JSONArray) result;
                setupList(json);
                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(activeUser != activity.getActiveUser())
        {
            Fragment frg = null;
            frg = getFragmentManager().findFragmentByTag(Constants.WALLET_FRAGMENT);
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();
        }
    }
}
