package steemplus.com.steemplus_android.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.HashMap;

import eu.bittrade.libs.steemj.base.models.DynamicGlobalProperty;
import eu.bittrade.libs.steemj.base.models.ExtendedAccount;
import steemplus.com.steemplus_android.Constants;
import steemplus.com.steemplus_android.Tools.SteemFormatter;

@Entity
public class UserAccount {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int userAccountId;
    private String imgUrl;
    private String username;
    @NonNull
    private boolean isFavorite;
    private String reputation;
    private double balance;
    private double sbdBalance;
    private double vestingShares;
    private double delegatedVestingShares;
    private double receivedVestingShares;
    private double savingsSteemBalance;
    private double savingsSbdBalance;
    private String privateMemoKey;


    public UserAccount(@NonNull int userAccountId, String imgUrl, String username,@NonNull boolean isFavorite, String reputation, double balance, double sbdBalance, double vestingShares, double delegatedVestingShares, double receivedVestingShares, double savingsSteemBalance, double savingsSbdBalance, String privateMemoKey) {
        this.userAccountId = userAccountId;
        this.imgUrl = imgUrl;
        this.username = username;
        this.isFavorite = isFavorite;
        this.reputation = reputation;
        this.balance = balance;
        this.sbdBalance = sbdBalance;
        this.vestingShares = vestingShares;
        this.delegatedVestingShares = delegatedVestingShares;
        this.receivedVestingShares = receivedVestingShares;
        this.savingsSbdBalance = savingsSbdBalance;
        this.savingsSteemBalance = savingsSteemBalance;
        this.privateMemoKey = privateMemoKey;
    }

    public void updateUserAccount(ExtendedAccount account)
    {
        this.imgUrl = SteemFormatter.getImageURLFromAccount(account);
        this.username = account.getName().getName();
        this.reputation = SteemFormatter.formatReputation(2, account.getReputation());
        this.balance = account.getBalance().toReal();
        this.sbdBalance = account.getSbdBalance().toReal();
        this.vestingShares = account.getVestingShares().toReal();
        this.delegatedVestingShares = account.getDelegatedVestingShares().toReal();
        this.receivedVestingShares = account.getReceivedVestingShares().toReal();
        this.savingsSbdBalance = account.getSavingsSbdBalance().toReal();
        this.savingsSteemBalance = account.getSavingsBalance().toReal();
    }

    public UserAccount(ExtendedAccount account, boolean isFavorite)
    {
        this.imgUrl = SteemFormatter.getImageURLFromAccount(account);
        this.username = account.getName().getName();
        this.isFavorite = isFavorite;
        this.reputation = SteemFormatter.formatReputation(2, account.getReputation());
        this.balance = account.getBalance().toReal();
        this.sbdBalance = account.getSbdBalance().toReal();
        this.vestingShares = account.getVestingShares().toReal();
        this.delegatedVestingShares = account.getDelegatedVestingShares().toReal();
        this.receivedVestingShares = account.getReceivedVestingShares().toReal();
        this.savingsSbdBalance = account.getSavingsSbdBalance().toReal();
        this.savingsSteemBalance = account.getSavingsBalance().toReal();
    }

    @NonNull
    public int getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(@NonNull int userAccountId) {
        this.userAccountId = userAccountId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    @NonNull
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getReputation() {
        return reputation;
    }

    public void setReputation(String reputation) {
        this.reputation = reputation;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getSbdBalance() {
        return sbdBalance;
    }

    public void setSbdBalance(double sbdBalance) {
        this.sbdBalance = sbdBalance;
    }

    public double getVestingShares() {
        return vestingShares;
    }

    public void setVestingShares(double vestingShares) {
        this.vestingShares = vestingShares;
    }

    public double getDelegatedVestingShares() {
        return delegatedVestingShares;
    }

    public void setDelegatedVestingShares(double delegatedVestingShares) {
        this.delegatedVestingShares = delegatedVestingShares;
    }

    public double getReceivedVestingShares() {
        return receivedVestingShares;
    }

    public void setReceivedVestingShares(double receivedVestingShares) {
        this.receivedVestingShares = receivedVestingShares;
    }

    public double getSavingsSteemBalance() {
        return savingsSteemBalance;
    }

    public void setSavingsSteemBalance(double savingsSteemBalance) {
        this.savingsSteemBalance = savingsSteemBalance;
    }

    public double getSavingsSbdBalance() {
        return savingsSbdBalance;
    }

    public void setSavingsSbdBalance(double savingsSbdBalance) {
        this.savingsSbdBalance = savingsSbdBalance;
    }

    public String getPrivateMemoKey() {
        return privateMemoKey;
    }

    public String getPrivateMemoKeyString() {
        return (privateMemoKey == null ? "" : privateMemoKey);
    }

    public void setPrivateMemoKey(String privateMemoKey) {
        this.privateMemoKey = privateMemoKey;
    }

    // String getters

    public String getBalanceString()
    {
        return new String(balance + " Steem");
    }
    public String getSBDBalanceString()
    {
        return new String(sbdBalance + " SBD");
    }
    public String getVotingPowerString(double totalVestingFundSteem, double totalVestingShares)
    {
        return new String(SteemFormatter.vestToSteemPower(vestingShares, totalVestingFundSteem, totalVestingShares) + " Steem");
    }
    public String getDelegatedVotingPowerString(double totalVestingFundSteem, double totalVestingShares)
    {
        return new String(SteemFormatter.vestToSteemPower(delegatedVestingShares, totalVestingFundSteem, totalVestingShares) + " Steem");
    }
    public String getReceivedVotingPowerString(double totalVestingFundSteem, double totalVestingShares)
    {
        return new String(SteemFormatter.vestToSteemPower(receivedVestingShares, totalVestingFundSteem, totalVestingShares) + " Steem");
    }
    public String getSavingsSteemBalanceString()
    {
        return new String(savingsSteemBalance + " Steem");
    }
    public String getSavingsSBDBalanceString()
    {
        return new String(savingsSbdBalance + " SBD");
    }
    public String getAccountValueString(HashMap<String, Double> marketPrice, DynamicGlobalProperty properties)
    {
        double totalSteem = savingsSteemBalance + balance + SteemFormatter.vestToSteemPower(vestingShares, properties.getTotalVestingFundSteem().toReal(), properties.getTotalVestingShares().toReal());
        double totalSBD = savingsSbdBalance + sbdBalance;
        double total = (totalSteem*marketPrice.get(Constants.PRICE_STEEM_USD)+totalSBD*marketPrice.get(Constants.PRICE_SBD_USD));

        return new String(SteemFormatter.toFixed(3, total) + " USD");
    }

}
