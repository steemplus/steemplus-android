package steemplus.com.steemplus_android.Adapters;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import steemplus.com.steemplus_android.Models.UserAccount;
import steemplus.com.steemplus_android.R;

public class UserAccountAdapter extends ArrayAdapter<UserAccount> {

    private Context context;
    private int layoutResId;
    private ArrayList<UserAccount> userAccounts;


    public UserAccountAdapter(@NonNull Context context, int layoutResId, @NonNull ArrayList<UserAccount> userAccounts) {
        super(context, layoutResId, userAccounts);
        this.layoutResId = layoutResId;
        this.context = context;
        this.userAccounts = userAccounts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserAccountHolder holder = null;

        if(convertView == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(layoutResId, parent, false);

            holder = new UserAccountHolder();
            holder.userImageView = (ImageView)convertView.findViewById(R.id.avatar);
            holder.usernameTextView = (TextView)convertView.findViewById(R.id.username);

            convertView.setTag(holder);
        }
        else
        {
            holder = (UserAccountHolder) convertView.getTag();
        }
        UserAccount userAccount = userAccounts.get(position);
        holder.usernameTextView.setText(userAccount.getUsername());
        Picasso.get().load(userAccount.getImgUrl()).into(holder.userImageView);


        return convertView;
    }

    static class UserAccountHolder
    {
        ImageView userImageView;
        TextView usernameTextView;
    }

}
