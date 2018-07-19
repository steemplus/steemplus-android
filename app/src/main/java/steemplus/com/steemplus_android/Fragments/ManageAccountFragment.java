package steemplus.com.steemplus_android.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import eu.bittrade.libs.steemj.base.models.ExtendedAccount;
import steemplus.com.steemplus_android.Adapters.UserAccountAdapter;
import steemplus.com.steemplus_android.Constants;
import steemplus.com.steemplus_android.MainActivity;
import steemplus.com.steemplus_android.Models.UserAccount;
import steemplus.com.steemplus_android.R;
import steemplus.com.steemplus_android.Tools.AsyncGetSteemJ;
import steemplus.com.steemplus_android.Tools.SteemFormatter;
import steemplus.com.steemplus_android.taskCompleteListener;

public class ManageAccountFragment extends Fragment implements taskCompleteListener<Object>{

    private MainActivity activity;
    private taskCompleteListener<Object> parent;
    private UserAccountAdapter adapter;
    private WeakReference asyncTaskWeakRef;

    private ListView accountsListView;
    private Button addUserAccountButton;
    private EditText userAccountNameEditText;

    private ArrayList<UserAccount> userAccounts;

    @Override
    public void onAttach(Context activity) {
        //  Log.d(TAG_LOG, "onAttach");
        super.onAttach(activity);
        this.activity= (MainActivity) activity;
        parent = (taskCompleteListener<Object>) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //   Log.d(TAG_LOG, "onCreate");
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.manage_account_fragment, container, false);
        accountsListView =  view.findViewById(R.id.list_accounts);
        userAccountNameEditText = view.findViewById(R.id.user_account_name_edit_text);
        addUserAccountButton = view.findViewById(R.id.add_user_account_btn);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userAccounts = new ArrayList<>();

        adapter = new UserAccountAdapter(activity,
                R.layout.account_list_row, userAccounts);
        accountsListView.setAdapter(adapter);

        addUserAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncGetSteemJ asyncGetSteemJ = new AsyncGetSteemJ(activity, ManageAccountFragment.this, activity.getSteemJ());
                asyncTaskWeakRef = new WeakReference<>(asyncGetSteemJ);
                asyncGetSteemJ.execute(Constants.STEEMJ_GET_ACCOUNT, userAccountNameEditText.getText().toString());
            }
        });
    }


    @Override
    public void onTaskComplete(Object result, String action) {
        switch (action)
        {
            case Constants.STEEMJ_GET_ACCOUNT:
            {
                ArrayList<ExtendedAccount> accounts = (ArrayList<ExtendedAccount>) result;
                if(accounts.size() == 0)
                {
                    if(userAccountNameEditText.getText().toString().isEmpty())
                        Toast.makeText(activity, R.string.username_empty, Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(activity, R.string.username_wrong, Toast.LENGTH_SHORT).show();

                    return;
                }

                ExtendedAccount account = accounts.get(0);
                userAccounts.add(new UserAccount(SteemFormatter.getImageURLFromAccount(account),account.getName().getName()));
                adapter.notifyDataSetChanged();
                userAccountNameEditText.setText("");
                break;
            }
        }
    }
}
