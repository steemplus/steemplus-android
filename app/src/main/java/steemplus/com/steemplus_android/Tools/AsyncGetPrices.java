package steemplus.com.steemplus_android.Tools;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import steemplus.com.steemplus_android.taskCompleteListener;

public class AsyncGetPrices extends AsyncTask<String, Void, Double> {

    private final taskCompleteListener<Object> listener;
    private WeakReference<Context> fragmentWeakRef;
    private String action;

    public AsyncGetPrices(Context con, taskCompleteListener<Object> listener)
    {
        this.listener=listener;
        this.fragmentWeakRef = new WeakReference<>(con);
    }

    @Override
    protected Double doInBackground(String... params) {
        action = params[0];
        String urlRequest = params[1];
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        URL url;

        try {
            url = new URL(urlRequest);

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

            JSONObject result = null;
            result = new JSONObject(buffer.toString());
            result = result.getJSONObject("result");
            return result.getDouble("Bid");
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
    protected void onPostExecute(Double result) {
        super.onPostExecute(result);
        if(this.fragmentWeakRef.get()!=null)
            listener.onTaskComplete(result, action);
    }

}
