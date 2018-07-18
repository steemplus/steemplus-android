package steemplus.com.steemplus_android;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import eu.bittrade.libs.steemj.SteemJ;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.configuration.SteemJConfig;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, taskCompleteListener {

    private static final String TAG_LOG = "Log MainActivity";
    private FragmentSwitcher switcher_o;
    private FragmentSwitcher drawer_switcher_o;
    private String current_fragment_s;
    private String current_fragment_user_profile;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private SteemJ steemJ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onTaskComplete(Object result)
    {

    }

    private void initSteemJ()
    {
        SteemJConfig myConfig = SteemJConfig.getInstance();
        myConfig.setResponseTimeout(100000);
        myConfig.setDefaultAccount(new AccountName("steemj"));

        try {
            steemJ = new SteemJ();
        } catch (SteemCommunicationException e) {
            Toast.makeText(MainActivity.this, "Can't connect to SteemJ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (SteemResponseException e) {
            Toast.makeText(MainActivity.this, "Can't connect to SteemJ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public SteemJ getSteemJ()
    {
        if(steemJ==null)
            initSteemJ();

        return steemJ;
    }


}
