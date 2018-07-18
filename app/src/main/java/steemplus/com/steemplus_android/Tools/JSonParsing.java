//package steemplus.com.steemplus_android.Tools;
//
//import android.util.Log;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import steemplus.com.steemplus_android.Constants;
//
//
//public class JSonParsing {
//    private JSONObject obj=null;
//    private int nbLogError_d=-1;
//    private final String TAG_LOG="My Log JSON";
//    JSonParsing()
//    {
//    }
//
//    public void getJSonFromUrl(String new_url,int method,String data) {
//        try {
//            // Add the information to the POST file
//            URL url;
//            Log.d(TAG_LOG,new_url);
//            HttpURLConnection con;
//            if(method== Constants.URL_POST)
//            {
//                Log.d(TAG_LOG,data);
//                url = new URL(new_url);
//                con = (HttpURLConnection) url.openConnection();
//                con.setRequestMethod("POST");
//                con.setDoOutput(true);
//                con.setRequestProperty("Accept-Charset", "UTF-8");
//                OutputStream os = con.getOutputStream();
//                os.write(data.getBytes());
//                os.flush();
//                os.close(); //TODO: https
//
//            }
//            else if (method ==Constants.URL_GET)
//            {
//                Log.d(TAG_LOG, "get");
//                url = new URL(new_url+"?"+data);
//                Log.d(TAG_LOG,url.toString());
//                con = (HttpURLConnection) url.openConnection();
//                con.setRequestProperty("Accept-Charset", "UTF-8");
//                con.setRequestMethod("GET");
//            }
//            else
//                con=(HttpURLConnection)new URL(new_url).openConnection();
//
//            int responseCode = con.getResponseCode();
//
//            if (responseCode == HttpURLConnection.HTTP_OK) { //success
//                BufferedReader in = new BufferedReader(new InputStreamReader(
//                        con.getInputStream()));
//                String inputLine;
//                StringBuilder response = new StringBuilder();
//
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                in.close();
//                String jsondata= response.toString();
//                Log.d(TAG_LOG, jsondata);
//                obj= new JSONObject(jsondata);
//                Log.d(TAG_LOG, obj.toString());
//                if(obj.has("error"))
//                {
//                    nbLogError_d = obj.getJSONArray("error").getInt(0);
//                    Log.d(TAG_LOG, "error: " + Integer.toString(nbLogError_d));
//                }
//
//            }
//            else
//                nbLogError_d=3; // If status is wrong, it's a server problem
//
//            // Handle exceptions, if there are exceptions, it means sth is wrong with the server
//            // Hence, return error 3 "Sorry the server is down..."
//
//        } catch (IOException e) {
//            Log.d(TAG_LOG, "IO Exception: "+e.getMessage());
//            nbLogError_d=4;
//        } catch (JSONException e) {
//            Log.d(TAG_LOG, "JSON Exception in Parsing" + e.getMessage());
//            nbLogError_d=3;
//        }
//        switch (nbLogError_d)
//        {
//            case 0:
//                message=Constants.DISP_NONE;
//                break;
//            case 3:
//                message=Constants.DISP_NO_DB;
//                break;
//            case 4:
//                message=Constants.DISP_NO_CONNECT;
//                break;
//            case 5:
//                message=Constants.DISP_NO_RESULT;
//        }
//    }
//
//    public int getMessage() {
//        return message;
//    }
//
//    public JSONObject getObj() {
//        return obj;
//    }
//
//}
//
