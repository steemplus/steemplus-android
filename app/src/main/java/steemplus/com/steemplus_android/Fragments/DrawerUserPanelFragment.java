package steemplus.com.steemplus_android.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import eu.bittrade.libs.steemj.base.models.ExtendedAccount;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;
import steemplus.com.steemplus_android.Constants;
import steemplus.com.steemplus_android.MainActivity;
import steemplus.com.steemplus_android.Models.UserAccount;
import steemplus.com.steemplus_android.R;
import steemplus.com.steemplus_android.Tools.AsyncGetSteemJ;
import steemplus.com.steemplus_android.Tools.SteemFormatter;
import steemplus.com.steemplus_android.taskCompleteListener;

public class DrawerUserPanelFragment extends Fragment{

    private MainActivity activity;
    private taskCompleteListener<Object> parent;
    private LinearLayout userProfilePanel;
    private TextView usernameTextView;
    private TextView reputationTextView;
    private ImageView userProfilePictureImageView;
    private WeakReference asyncTaskWeakRef;
    private String TAG_LOG = "DrawerUserPanelFragment";

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

        View view = inflater.inflate(R.layout.menu_user_panel_layout, container, false);
        userProfilePanel = view.findViewById(R.id.menu_user_profile);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        reputationTextView = view.findViewById(R.id.reputationTextView);
        userProfilePictureImageView = view.findViewById(R.id.profilePictureImgView);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setProfile();
    }

    private void setProfile() {
        new AsyncTask<Void, Void, UserAccount>() {
            @Override
            protected UserAccount doInBackground(Void... params) {
                UserAccount userAccount = activity.getAppDatabase(activity).userDao().getFavorite();
                return userAccount;
            }

            @Override
            protected void onPostExecute(UserAccount userAccount)
            {
                if(userAccount == null) return;
                setUsername(userAccount.getUsername());
                setReputation(userAccount.getReputation());
                setProfilePicture(userAccount.getImgUrl());
            }
        }.execute();
    }

    private void setUsername(String username)
    {
        usernameTextView.setText('@'+username);
    }

    private void setReputation(String reputation)
    {
        reputationTextView.setText(reputation);
    }

    private void setProfilePicture(String imgURL)
    {
        Picasso.get().load(imgURL).into(userProfilePictureImageView);
    }
}
