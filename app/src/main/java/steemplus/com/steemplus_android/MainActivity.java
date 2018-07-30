package steemplus.com.steemplus_android;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import eu.bittrade.libs.steemj.SteemJ;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.DynamicGlobalProperty;
import eu.bittrade.libs.steemj.base.models.ExtendedAccount;
import eu.bittrade.libs.steemj.configuration.SteemJConfig;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;
import steemplus.com.steemplus_android.Database.AppDatabase;
import steemplus.com.steemplus_android.Database.Databases.Migrations;
import steemplus.com.steemplus_android.Models.UserAccount;
import steemplus.com.steemplus_android.Tools.AsyncGetPrices;
import steemplus.com.steemplus_android.Tools.AsyncGetSteemJ;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, taskCompleteListener {

    private static final String TAG_LOG = "Log MainActivity";
    private FragmentSwitcher switcher_o;
    private FragmentSwitcher drawer_switcher_o;
    private String current_fragment_s;
    private String current_fragment_user_profile;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private UserAccount activeUser = null;
    private HashMap<String, Double> marketPrices = new HashMap<>();
    public Bundle savedInstanceState=null;

    private SteemJ steemJ;
    private final String DATABASE_NAME = "steemplus-db";
    private AppDatabase dnInstance;
    private DynamicGlobalProperty dynamicGlobalProperty;

    private int countGetPrices = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_main);
        // Init steemJ
        initSteemJ();
        // Init database
        createDatabase(this);
        // Init prices
        new AsyncGetPrices(this, this).execute(Constants.GET_PRICE_BTC, Constants.GET_PRICE_BTC_URL);
        new AsyncGetPrices(this, this).execute(Constants.GET_PRICE_SBD, Constants.GET_PRICE_SBD_URL);
        new AsyncGetPrices(this, this).execute(Constants.GET_PRICE_STEEM, Constants.GET_PRICE_STEEM_URL);

    }

    public void initApp(Bundle savedInstanceState)
    {
        //Initialize Fragment Manager
        FragmentManager fManager = getSupportFragmentManager();
        switcher_o = new FragmentSwitcher(fManager, R.id.fragment_container);


        //Auto generated for drawer ----
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer_switcher_o = new FragmentSwitcher(fManager, R.id.nav_view);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // --- Until here

        // If activity was just launched
        if(savedInstanceState==null)
        {
            switcher_o.showFragment(current_fragment_s, Constants.MAIN_FRAGMENT);
            drawer_switcher_o.showFragment(current_fragment_user_profile, Constants.DRAWER_USER_PANEL_FRAGMENT);
            current_fragment_user_profile = Constants.DRAWER_USER_PANEL_FRAGMENT;
            current_fragment_s = Constants.MAIN_FRAGMENT;
            navigationView.setCheckedItem(R.id.steemit);

        }
        else
        {
            current_fragment_s=savedInstanceState.getString(Constants.PANEL_TAG);
        }
    }


    // Save what is the current fragment when the activity is destroyed.
    @Override
    protected void onSaveInstanceState(Bundle outstate) {
        outstate.putString(Constants.PANEL_TAG, current_fragment_s);
        super.onSaveInstanceState(outstate);
    }

    //Behavior when user presses start
    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(!current_fragment_s.equals(Constants.MAIN_FRAGMENT)) {
                navigationView.setCheckedItem(R.id.steemit);
                current_fragment_s=Constants.MAIN_FRAGMENT;
            }
            super.onBackPressed();
        }
    }




    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.steemit:
                switcher_o.showFragment(current_fragment_s,Constants.MAIN_FRAGMENT);
                current_fragment_s=Constants.MAIN_FRAGMENT;
                break;
            case R.id.wallet:
                switcher_o.showFragment(current_fragment_s,Constants.WALLET_FRAGMENT);
                current_fragment_s=Constants.WALLET_FRAGMENT;
                break;
            case R.id.manage_account:
                switcher_o.showFragment(current_fragment_s,Constants.MANAGE_ACCOUNT_FRAGMENT);
                current_fragment_s=Constants.MANAGE_ACCOUNT_FRAGMENT;
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onTaskComplete(Object result, String action)
    {
        Log.d(TAG_LOG, "test");
        switch (action)
        {
            case Constants.STEEMJ_GET_ACCOUNT:
                ArrayList<ExtendedAccount> userAccounts = (ArrayList<ExtendedAccount>)result;
                activeUser.updateUserAccount(userAccounts.get(0));
                new AsyncTask<UserAccount, Void, Void>() {
                    @Override
                    protected Void doInBackground(UserAccount ... accounts) {
                        getAppDatabase(MainActivity.this).userDao().update(activeUser);
                        return null;
                    }
                }.execute();
                break;
            case Constants.GET_PRICE_BTC:
                marketPrices.put(Constants.PRICE_BTC, (Double)result);
                countGetPrices += 1;
                break;
            case Constants.GET_PRICE_SBD:
                marketPrices.put(Constants.PRICE_SBD, (Double)result);
                countGetPrices += 1;
                break;
            case Constants.GET_PRICE_STEEM:
                marketPrices.put(Constants.PRICE_STEEM, (Double)result);
                countGetPrices += 1;
                break;
        }
        if(countGetPrices == 3)
        {
            countGetPrices = 0;
            calculatePrices();
        }
    }

    private void calculatePrices()
    {
        Double priceSteem = marketPrices.get(Constants.PRICE_STEEM);
        Double priceSBD = marketPrices.get(Constants.PRICE_SBD);
        Double priceBTC = marketPrices.get(Constants.PRICE_BTC);
        marketPrices.put(Constants.PRICE_SBD_PER_STEEM,priceSteem/priceSBD );
        marketPrices.put(Constants.PRICE_SBD_USD, priceSBD*priceBTC);
        marketPrices.put(Constants.PRICE_STEEM_USD, priceSteem*priceBTC);
    }

    private void initSteemJ()
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                SteemJConfig myConfig = SteemJConfig.getInstance();
                myConfig.setResponseTimeout(100000);
                myConfig.setDefaultAccount(new AccountName("steem-plus"));

                try {
                    steemJ = new SteemJ();
                    dynamicGlobalProperty = steemJ.getDynamicGlobalProperties();
                } catch (SteemCommunicationException e) {
                    e.printStackTrace();
                } catch (SteemResponseException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public SteemJ getSteemJ()
    {
        if(steemJ==null)
            initSteemJ();

        return steemJ;
    }

    public AppDatabase getAppDatabase(Context context) {
        if (dnInstance == null) {
            createDatabase(context);
        }
        return dnInstance;
    }

    public void destroyDbInstance() {
        dnInstance = null;
    }

    public void createDatabase(final Context context)
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                dnInstance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                        .addMigrations(Migrations.MIGRATION_1_2, Migrations.MIGRATION_2_3, Migrations.MIGRATION_3_4, Migrations.MIGRATION_4_5, Migrations.MIGRATION_5_6)
                        .build();
                activeUser = getAppDatabase(context).userDao().getFavorite();
                if(activeUser != null)
                    new AsyncGetSteemJ(context, MainActivity.this, steemJ).execute(Constants.STEEMJ_GET_ACCOUNT, activeUser.getUsername());
                return null;
            }

            @Override
            public void onPostExecute(Void voids)
            {
                initApp(savedInstanceState);
            }
        }.execute();
    }

    public UserAccount getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(UserAccount user)
    {
        activeUser = activeUser;
    }

    public DynamicGlobalProperty getDynamicGlobalProperty() {
        return dynamicGlobalProperty;
    }

    public HashMap<String, Double> getMarketPrices() {
        return marketPrices;
    }


    // Menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id)
        {
            case R.id.settings_item:
                switcher_o.showFragment(current_fragment_s,Constants.SETTINGS_FRAGMENT);
                current_fragment_s=Constants.SETTINGS_FRAGMENT;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void refreshActiveUser()
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                activeUser = getAppDatabase(MainActivity.this).userDao().getFavorite();
                return null;
            }
        }.execute();
    }
}
