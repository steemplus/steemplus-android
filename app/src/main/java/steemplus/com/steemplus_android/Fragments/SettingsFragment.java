package steemplus.com.steemplus_android.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import eu.bittrade.libs.steemj.base.models.ExtendedAccount;
import steemplus.com.steemplus_android.Adapters.UserAccountAdapter;
import steemplus.com.steemplus_android.Constants;
import steemplus.com.steemplus_android.MainActivity;
import steemplus.com.steemplus_android.Models.UserAccount;
import steemplus.com.steemplus_android.R;
import steemplus.com.steemplus_android.taskCompleteListener;

public class SettingsFragment extends Fragment {

    private MainActivity activity;
    private taskCompleteListener<Object> parent;
    private WeakReference asyncTaskWeakRef;
    private UserAccount activeUser;

    private final String TAG_LOG = "ManageAccountFragment";

    private Button saveBtn;
    private EditText privateMemoKeyEditText;



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
        View view = inflater.inflate(R.layout.settings_layout, container, false);
        privateMemoKeyEditText = view.findViewById(R.id.settings_memo_key);
        saveBtn = view.findViewById(R.id.save_settings_btn);
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initView()
    {
        activeUser = activity.getActiveUser();
        if(activeUser != null)
        {
            privateMemoKeyEditText.setText(activeUser.getPrivateMemoKeyString());


            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, "test", Toast.LENGTH_SHORT).show();
//                    activity.getActiveUser().setPrivateMemoKey(privateMemoKeyEditText.getText().toString());
//                    new AsyncTask<UserAccount, Void, Void>() {
//                        @Override
//                        protected Void doInBackground(UserAccount ... accounts) {
//                            activity.getAppDatabase(activity).userDao().update(activity.getActiveUser());
//                            return null;
//                        }
//                    }.execute();
                }
            });
        }
    }
}
