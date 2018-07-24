package steemplus.com.steemplus_android.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import steemplus.com.steemplus_android.Models.WalletItem;
import steemplus.com.steemplus_android.R;

public class WalletItemAdapter extends ArrayAdapter<WalletItem> {

    private Context context;
    private int layoutResId;
    private ArrayList<WalletItem> walletItemList;
    private ArrayList<WalletItem> filteredWalletItemList;

    public WalletItemAdapter(@NonNull Context context, int layoutResId, @NonNull ArrayList<WalletItem> walletItemList) {
        super(context, layoutResId, walletItemList);
        this.context = context;
        this.layoutResId = layoutResId;
        this.walletItemList = walletItemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WalletItemAdapter.WalletItemHolder holder = null;

        if(convertView == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(layoutResId, parent, false);

            holder = new WalletItemAdapter.WalletItemHolder();
            holder.profilePictureIV = (ImageView)convertView.findViewById(R.id.wallet_item_img);
            holder.walletItemAmount = (TextView)convertView.findViewById(R.id.wallet_item_amount);
            holder.walletItemLabel = (TextView)convertView.findViewById(R.id.wallet_item_label);
            holder.walletItemName = (TextView)convertView.findViewById(R.id.wallet_item_name);
            holder.walletItemMemo = (TextView)convertView.findViewById(R.id.wallet_item_memo);
            holder.walletItemCreated = (TextView)convertView.findViewById(R.id.wallet_item_created);

            convertView.setTag(holder);
        }
        else
        {
            holder = (WalletItemAdapter.WalletItemHolder) convertView.getTag();
        }
        WalletItem walletItem = walletItemList.get(position);
        switch (walletItem.getType())
        {
            case "claim":
                holder.walletItemLabel.setText(context.getResources().getString(R.string.wallet_claim_text));
                holder.walletItemAmount.setText(walletItem.getClaimText());
                break;
            case "transfer_to":
                holder.walletItemLabel.setText(context.getResources().getString(R.string.wallet_transfer_to_text));
                holder.walletItemName.setText(walletItem.getUsername());
                holder.walletItemAmount.setText(walletItem.getAmountTransferText());
                break;
            case "transfer_from":
                holder.walletItemLabel.setText(context.getResources().getString(R.string.wallet_transfer_from_text));
                holder.walletItemName.setText(walletItem.getUsername());
                holder.walletItemAmount.setText(walletItem.getAmountTransferText());
                break;

        }
        holder.walletItemCreated.setText(walletItem.getOperationDateS(context.getResources().getString(R.string.format_date)));
        holder.walletItemMemo.setText(walletItem.getMemo());

        return convertView;
    }

    static class WalletItemHolder
    {
        ImageView profilePictureIV;
        TextView walletItemLabel;
        TextView walletItemName;
        TextView walletItemAmount;
        TextView walletItemMemo;
        TextView walletItemCreated;
    }
}
