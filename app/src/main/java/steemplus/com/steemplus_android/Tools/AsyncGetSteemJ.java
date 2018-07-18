package steemplus.com.steemplus_android.Tools;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import eu.bittrade.libs.steemj.SteemJ;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;
import steemplus.com.steemplus_android.taskCompleteListener;

public class AsyncGetSteemJ extends AsyncTask<Void, Void, Void> {

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
    protected Void doInBackground(Void... voids) {
        ArrayList<AccountName> accountNames = new ArrayList<AccountName>();
        accountNames.add(new AccountName("cedricguillas"));
        try {
            result = steemJ.getAccounts(accountNames);
        } catch (SteemCommunicationException e) {
            e.printStackTrace();
        } catch (SteemResponseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v)
    {
        super.onPostExecute(v);
        if(this.fragmentWeakRef.get()!=null)
            listener.onTaskComplete(result);
    }
}
