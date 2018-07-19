package steemplus.com.steemplus_android.Models;

public class UserAccount {

    private String imgUrl;
    private String username;

    public UserAccount(String imgUrl, String username) {
        this.imgUrl = imgUrl;
        this.username = username;
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
}
