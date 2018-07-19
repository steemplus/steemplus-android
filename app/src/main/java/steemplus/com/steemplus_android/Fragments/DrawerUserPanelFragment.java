package steemplus.com.steemplus_android.Fragments;

import android.content.Context;
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
import steemplus.com.steemplus_android.R;
import steemplus.com.steemplus_android.Tools.AsyncGetSteemJ;
import steemplus.com.steemplus_android.Tools.SteemFormatter;
import steemplus.com.steemplus_android.taskCompleteListener;

public class DrawerUserPanelFragment extends Fragment implements taskCompleteListener<Object> {

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
        AsyncGetSteemJ asyncGetSteemJ = new AsyncGetSteemJ(activity, this, activity.getSteemJ());
        asyncTaskWeakRef = new WeakReference<>(asyncGetSteemJ);
        asyncGetSteemJ.execute(Constants.STEEMJ_GET_ACCOUNT);
    }

    @Override
    public void onTaskComplete(Object result, String action) {
        switch (action)
        {
            case Constants.STEEMJ_GET_ACCOUNT:
            {
                ArrayList<ExtendedAccount> accounts = (ArrayList<ExtendedAccount>) result;
                ExtendedAccount myAccount = accounts.get(0);
                setUsername(myAccount.getName().getName());
                setReputation(myAccount.getReputation());
                setProfilePicture(SteemFormatter.getImageURLFromAccount(myAccount));
                break;
            }
        }

    }

    private void setUsername(String username)
    {
        usernameTextView.setText('@'+username);
    }

    private void setReputation(long reputation)
    {
        reputationTextView.setText(SteemFormatter.formatReputation(2, reputation));
    }

    private void setProfilePicture(String imgURL)
    {
        Picasso.get().load(imgURL).into(userProfilePictureImageView);
    }
}
