package steemplus.com.steemplus_android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by quentin on 3/13/18.
 */

class FragmentSwitcher {

    private static final String TAG_LOG = "Tag Switcher";
    private final FragmentManager fManager;
    private boolean createnew=true;

    public FragmentSwitcher(FragmentManager fManager)
    {
        this.fManager=fManager;
    }

    public void showFragment(String current_tag, String new_tag, boolean createNew)
    {
        createnew=createNew;
        showFragment( current_tag,  new_tag);
    }

    public void showFragment(String current_tag, String new_tag)
    {
        int container=R.id.fragment_container;
        Fragment fragment=null;
        if(current_tag!=null)
            fragment=fManager.findFragmentByTag(new_tag);



        FragmentTransaction fTransaction=fManager.beginTransaction();
        if(fragment==null)
        {
            fragment=initFragment(new_tag);
            if( fragment != null)
                fragment.setRetainInstance(true);
            if(fManager.findFragmentByTag(current_tag)!=null)
            {
                fTransaction.replace(container, fragment,new_tag);
                //Log.d(TAG_LOG, "Replace " + current_tag + " with " + new_tag);
            }
            else {
                fTransaction.add(container, fragment, new_tag);
                //Log.d(TAG_LOG, "Add " + new_tag );
            }
        }
        else
        {
            fTransaction.replace(container, fragment, new_tag);
            //Log.d("My Log FS", "Replace "+current_tag+" with "+new_tag +" in "+container);
        }

        if(current_tag!=null)
            fTransaction.addToBackStack(current_tag);
        fTransaction.commit();
    }

    private Fragment initFragment(String new_tag) {
        switch (new_tag) {
            case Constants.MAIN_FRAGMENT:
                return new MainFragment();
            case Constants.WALLET_FRAGMENT:
                return new WalletFragment();

        }
        return null;
    }
}

