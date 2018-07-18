//package steemplus.com.steemplus_android.Tools;
//
//import android.content.Context;
//import android.os.AsyncTask;
//
//import java.lang.ref.WeakReference;
//
//import eu.bittrade.libs.steemj.base.models.Account;
//import steemplus.com.steemplus_android.Constants;
//import steemplus.com.steemplus_android.taskCompleteListener;
//
//class AsyncGetJSON extends AsyncTask<String, Void, Void>
//{
//    private final taskCompleteListener<Account> listener;
//    private String data;
//    private WeakReference<Context> fragmentWeakRef=null;
//
//    public AsyncGetJSON(Context con, taskCompleteListener<> listener)
//    {
//        this.listener=listener;
//        fragmentWeakRef = new WeakReference<>(con);
//    }
//
//
//
//    @Override
//    protected Void doInBackground(String... params)
//    {
//        //Log.d(TAG_LOG, "Attempting to getWorkerTable JSON");
//        json=new JSonParsing();
//        json.getJSonFromUrl( params[0], method, data);
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Void v)
//    {
//        super.onPostExecute(v);
//        if(this.fragmentWeakRef.get()!=null)
//            listener.onTaskComplete(json);
//    }
//}
