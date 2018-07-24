package steemplus.com.steemplus_android.Models;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WalletItem {

    private String type;
    private double amount;
    private String memo;
    private String username;
    private Date operationDate;
    private double rewardSteem;
    private double rewardSbd;
    private double rewardVests;
    private String amountSymbol;

    public WalletItem(JSONObject obj)
    {
        try
        {
            this.type = obj.getString("type");
            this.amount = obj.getDouble("amount");
            this.memo = obj.getString("memo");
            this.username = obj.getString("to_from");
            this.rewardSteem = obj.getDouble("reward_steem");
            this.rewardSbd = obj.getDouble("reward_sbd");
            this.rewardVests = obj.getDouble("reward_vests");
            this.amountSymbol = obj.getString("amount_symbol");

            String dtStart = obj.getString("timestamp");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            try {
                this.operationDate = format.parse(dtStart);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }

    public WalletItem(String type, double amount, String memo, String username, Date operationDate, double rewardSteem, double rewardSbd, double rewardVests, String amountSymbol) {
        this.type = type;
        this.amount = amount;
        this.memo = memo;
        this.username = username;
        this.operationDate = operationDate;
        this.rewardSteem = rewardSteem;
        this.rewardSbd = rewardSbd;
        this.rewardVests = rewardVests;
        this.amountSymbol = amountSymbol;
    }

    public String getClaimText()
    {
        ArrayList<String> txt = new ArrayList<>();
        if(rewardSbd > 0) txt.add(rewardSbd + " SBD");
        if(rewardSteem > 0) txt.add(rewardSteem + " STEEM");
        if(rewardVests > 0) txt.add(rewardVests + " SP");

        return TextUtils.join(",", txt);
    }

    public String getAmountTransferText()
    {
        return amount + " " + amountSymbol;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRewardSteem() {
        return rewardSteem;
    }

    public void setRewardSteem(double rewardSteem) {
        this.rewardSteem = rewardSteem;
    }

    public double getRewardSbd() {
        return rewardSbd;
    }

    public void setRewardSbd(double rewardSbd) {
        this.rewardSbd = rewardSbd;
    }

    public double getRewardVests() {
        return rewardVests;
    }

    public void setRewardVests(double rewardVests) {
        this.rewardVests = rewardVests;
    }

    public String getAmountSymbol() {
        return amountSymbol;
    }

    public void setAmountSymbol(String amountSymbol) {
        this.amountSymbol = amountSymbol;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public String getOperationDateS(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(operationDate);
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }
}
