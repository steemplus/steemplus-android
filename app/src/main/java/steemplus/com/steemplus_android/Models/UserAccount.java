package steemplus.com.steemplus_android.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import eu.bittrade.libs.steemj.base.models.ExtendedAccount;
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

    public UserAccount(@NonNull int userAccountId, String imgUrl, String username,@NonNull boolean isFavorite, String reputation) {
        this.userAccountId = userAccountId;
        this.imgUrl = imgUrl;
        this.username = username;
        this.isFavorite = isFavorite;
        this.reputation = reputation;
    }

    public UserAccount(ExtendedAccount account, boolean isFavorite)
    {
        this.imgUrl = SteemFormatter.getImageURLFromAccount(account);
        this. username = account.getName().getName();
        this.isFavorite = isFavorite;
        this.reputation = SteemFormatter.formatReputation(2, account.getReputation());
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
}
