package steemplus.com.steemplus_android.Tools;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import eu.bittrade.libs.steemj.SteemJ;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;
import steemplus.com.steemplus_android.Constants;
import steemplus.com.steemplus_android.taskCompleteListener;

public class AsyncGetSteemJ extends AsyncTask<String, Void, String> {

    private final taskCompleteListener<Object> listener;
    private Object result;
    private Context context;
    private WeakReference<Context> fragmentWeakRef=null;
    private SteemJ steemJ;

    public AsyncGetSteemJ(Context con, taskCompleteListener<Object> listener, SteemJ steemJ)
    {
        this.listener = listener;
        this.context = con;
        fragmentWeakRef = new WeakReference<>(this.context);
        this.steemJ = steemJ;
    }

    @Override
    protected String doInBackground(String... params) {
        String action = params[0];
        switch (action)
        {
            case Constants.STEEMJ_GET_ACCOUNT:
            {
                String username = new String();
                if(params.length <= 1) username = "steem-plus";
                else username = params[1];
                ArrayList<AccountName> accountNames = new ArrayList<AccountName>();
                accountNames.add(new AccountName(username));
                try {
                    result = steemJ.getAccounts(accountNames);
                } catch (SteemCommunicationException e) {
                    e.printStackTrace();
                } catch (SteemResponseException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        return action;
    }

    @Override
    protected void onPostExecute(String action)
    {
        super.onPostExecute(action);
        if(this.fragmentWeakRef.get()!=null)
            listener.onTaskComplete(result, action);
    }
}
