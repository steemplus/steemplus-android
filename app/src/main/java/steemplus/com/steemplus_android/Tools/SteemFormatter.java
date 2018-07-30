package steemplus.com.steemplus_android.Tools;

import org.json.JSONException;
import org.json.JSONObject;
import java.math.BigDecimal;
import eu.bittrade.libs.steemj.base.models.ExtendedAccount;

public class SteemFormatter {

    public static String formatReputation(int decimal, long reputation)
    {
        double tmp = (((Math.log10(reputation) - 9.00)*9)+25);

        return "(" + new BigDecimal(tmp).setScale(2, BigDecimal.ROUND_HALF_UP) + ")";
    }

    public static String getImageURLFromAccount(ExtendedAccount account)
    {
        String imgURL = new String();
        try {
            JSONObject metadataJSONObject = new JSONObject(account.getJsonMetadata());
             imgURL = metadataJSONObject.getJSONObject("profile").getString("profile_image");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "https://steemitimages.com/128x128/"+ imgURL;
    }

    public static double vestToSteemPower(double vests, double totalVestingFundSteem, double totalVestingShares)
    {
        double sp = totalVestingFundSteem * vests / totalVestingShares;
        return toFixed(3, sp);
    }

    public static double toFixed(int decimal, double x)
    {
        return new BigDecimal(x).setScale(decimal, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
