package steemplus.com.steemplus_android.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import steemplus.com.steemplus_android.MainActivity;
import steemplus.com.steemplus_android.R;
import steemplus.com.steemplus_android.taskCompleteListener;

public class DrawerUserPanelFragment extends Fragment {

    private MainActivity activity;
    private taskCompleteListener<Object> parent;
    private LinearLayout userProfilePanel;
    private TextView textView;

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
        textView = view.findViewById(R.id.textView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textView.setText("TOto test");
    }
}
