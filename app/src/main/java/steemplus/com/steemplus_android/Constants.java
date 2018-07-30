package steemplus.com.steemplus_android;

/**
 * Created by quentin on 7/10/18.
 */

public class Constants {
    public static final String MAIN_FRAGMENT="MAIN_FRAGMENT";
    public static final String WALLET_FRAGMENT="WALLET_FRAGMENT";
    public static final String DRAWER_USER_PANEL_FRAGMENT="DRAWER_USER_PANEL_FRAGMENT";
    public static final String MANAGE_ACCOUNT_FRAGMENT="MANAGE_ACCOUNT_FRAGMENT";
    public static final String SETTINGS_FRAGMENT="SETTINGS_FRAGMENT";


    public static final String PANEL_TAG = "PANEL_TAG";

    public static final String URL_GET = "GET";
    public static final String URL_POST = "POST";

    public static final String STEEMJ_GET_ACCOUNT = "steemJGetAccount";

    public static final String GET_STEEMPLUS_WALLET_HISTORY = "getSteemplusWalletHistory";
    public static final String GET_PRICE_STEEM = "getPriceSteem";
    public static final String GET_PRICE_SBD = "getPriceSBD";
    public static final String GET_PRICE_BTC = "getPriceBTC";

    public static final String PRICE_STEEM = "priceSteem";
    public static final String PRICE_SBD = "priceSBD";
    public static final String PRICE_BTC = "priceBTC";
    public static final String PRICE_SBD_PER_STEEM = "SBDperSteem";
    public static final String PRICE_STEEM_USD = "priceSteemUSD";
    public static final String PRICE_SBD_USD = "priceSBDUSD";

    // Steemplus-api urls
    public static final String STEEMPLUS_API_BASE_URL = "https://steemplus-api.herokuapp.com/api/";
    public static final String GET_STEEMPLUS_WALLET_HISTORY_URL = STEEMPLUS_API_BASE_URL + "get-wallet-content";

    // Bittrex urls
    public static final String GET_PRICE_STEEM_URL = "https://bittrex.com/api/v1.1/public/getticker?market=BTC-STEEM";
    public static final String GET_PRICE_SBD_URL = "https://bittrex.com/api/v1.1/public/getticker?market=BTC-SBD";
    public static final String GET_PRICE_BTC_URL = "https://bittrex.com/api/v1.1/public/getticker?market=USDT-BTC";
}
