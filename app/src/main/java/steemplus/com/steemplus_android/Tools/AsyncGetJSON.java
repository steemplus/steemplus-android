package steemplus.com.steemplus_android.Tools;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import eu.bittrade.libs.steemj.base.models.Account;
import steemplus.com.steemplus_android.Constants;
import steemplus.com.steemplus_android.taskCompleteListener;

public class AsyncGetJSON extends AsyncTask<String, Void, Object>
{
    private final taskCompleteListener<Object> listener;
    private String data;
    private WeakReference<Context> fragmentWeakRef=null;
    private Context context;
    private String action;

    public AsyncGetJSON(Context con, taskCompleteListener<Object> listener)
    {
        this.listener=listener;
        this.fragmentWeakRef = new WeakReference<>(con);
        this.context = con;
    }



    @Override
    protected Object doInBackground(String... params)
    {
        action = params[0];
        String requestURL = params[1];
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        URL url = null;

        try {
            switch (action)
            {
                case Constants.GET_STEEMPLUS_WALLET_HISTORY:
                    String data = params[2];
                    url = new URL(new String(requestURL + '/' + data));
                    break;
            }

            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
            }

            Object result = null;

            switch (action)
            {
                case Constants.GET_STEEMPLUS_WALLET_HISTORY:
                    result = new JSONArray(buffer.toString());
                    break;
            }
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        if(this.fragmentWeakRef.get()!=null)
            listener.onTaskComplete(result, action);
    }
}
