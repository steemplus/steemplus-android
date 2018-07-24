package steemplus.com.steemplus_android.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import steemplus.com.steemplus_android.taskCompleteListener;

public class ManageAccountFragment extends Fragment implements taskCompleteListener<Object>{

    private MainActivity activity;
    private taskCompleteListener<Object> parent;
    private UserAccountAdapter adapter;
    private WeakReference asyncTaskWeakRef;

    private ListView accountsListView;
    private Button addUserAccountButton;
    private EditText userAccountNameEditText;

    private UserAccount selectedAccount;

    private ArrayList<UserAccount> userAccounts;

    private final String TAG_LOG = "ManageAccountFragment";

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

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                userAccounts = (ArrayList<UserAccount>) activity.getAppDatabase(activity).userDao().getAll();
                return null;
            }

            @Override
            protected void onPostExecute(Void result)
            {
                setupView();
            }
        }.execute();

    }

    private void setupView()
    {
        adapter = new UserAccountAdapter(activity,
                R.layout.account_list_row, userAccounts);
        accountsListView.setAdapter(adapter);

        accountsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                selectedAccount = (UserAccount)accountsListView.getItemAtPosition(position);

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                new AsyncTask<UserAccount, Void, Void>() {
                                    @Override
                                    protected Void doInBackground(UserAccount... params) {
                                        UserAccount oldFavorite = null;
                                        for (UserAccount ua : userAccounts)
                                        {
                                            if(ua.isFavorite()) oldFavorite = ua;
                                        }
                                        params[0].setFavorite(true);
                                        if(oldFavorite==null)
                                        {
                                            activity.getAppDatabase(activity).userDao().setFavorite(params[0]);
                                        }
                                        else
                                        {
                                            oldFavorite.setFavorite(false);
                                            activity.getAppDatabase(activity).userDao().setFavorite(params[0], oldFavorite);
                                        }


                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void result)
                                    {
                                        refreshUserPanel();
                                    }
                                }.execute(selectedAccount);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage(getString(R.string.set_favorite, selectedAccount.getUsername())).setPositiveButton(getString(R.string.continue_operation), dialogClickListener)
                        .setNegativeButton(getString(R.string.cancel), dialogClickListener).show();
            }
        });

        accountsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int position, long id) {
                final UserAccount item = (UserAccount) accountsListView.getItemAtPosition(position);

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected Void doInBackground(Void... params) {
                                        activity.getAppDatabase(activity).userDao().delete(item);
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void result)
                                    {
                                        userAccounts.remove(item);
                                        adapter.notifyDataSetChanged();
                                    }
                                }.execute();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage(getString(R.string.delete_confirmation, item.getUsername())).setPositiveButton(getString(R.string.yes), dialogClickListener)
                        .setNegativeButton(getString(R.string.no), dialogClickListener).show();

                return false;
            }
        });

        addUserAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userAccountNameEditText.getText().toString().isEmpty())
                {
                    Toast.makeText(activity, R.string.username_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
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
                UserAccount newUserAccount = new UserAccount(account, userAccounts.isEmpty());
                new AsyncTask<UserAccount, Void, UserAccount>() {
                    @Override
                    protected UserAccount doInBackground(UserAccount... params) {
                        int newId = (int)activity.getAppDatabase(activity).userDao().insert(params[0]);
                        params[0].setUserAccountId(newId);
                        return params[0];
                    }

                    @Override
                    protected void onPostExecute(UserAccount newUserAccount)
                    {
                        if(userAccounts.isEmpty())
                            refreshUserPanel();
                        userAccounts.add(newUserAccount);
                        adapter.notifyDataSetChanged();
                        userAccountNameEditText.setText("");
                    }
                }.execute(newUserAccount);
                break;
            }
        }
    }

    public void refreshUserPanel()
    {
        Fragment frg = null;
        frg = getFragmentManager().findFragmentByTag(Constants.DRAWER_USER_PANEL_FRAGMENT);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    public void refreshView()
    {
        Fragment frg = null;
        frg = getFragmentManager().findFragmentByTag(Constants.MANAGE_ACCOUNT_FRAGMENT);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }
}
