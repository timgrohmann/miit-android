package com.thirtytwostudios.miit;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import com.facebook.AccessToken;
import com.facebook.FacebookActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by timgrohmann on 13.02.16.
 */
public class ServerConnection extends AsyncTask< ArrayList<ArrayList<String>>, Void, JSONObject> {
    Activity a;

    public ServerConnection(Activity activity) {
        this.a = activity;
    }
    public String downloadFromUrl(String node, ArrayList<ArrayList<String>> request) throws IOException {
        String urlstring = this.a.getResources().getString(R.string.baseurl) + node;
        InputStream is = null;
        if (AccessToken.getCurrentAccessToken() == null){
            return "";
        }
        ArrayList<String> accessToken = new ArrayList<String>();
        accessToken.add("accessToken");
        accessToken.add(AccessToken.getCurrentAccessToken().getToken() + "-" + AccessToken.getCurrentAccessToken().getUserId());
        request.add(accessToken);

        request.remove(0);

        String requestString = "";

        for(ArrayList<String> object: request){
            if (object.size() == 2){
                requestString += URLEncoder.encode(object.get(0),"utf-8") + "=" + URLEncoder.encode(object.get(1), "utf-8") + "&";
            }
        }
        if (requestString.charAt(requestString.length()-1)=='&' && requestString.length() > 0) {
            requestString = requestString.substring(0, requestString.length()-1);
        }

        byte[] postData       = requestString.getBytes(Charset.forName("utf-8"));
        int    postDataLength = postData.length;
        String c = "";
        try {
            URL url = new URL(urlstring);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setChunkedStreamingMode(0);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(requestString);
            writer.flush();
            writer.close();
            os.close();
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("com.32s.miit", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = convertStreamToString(is);
            Log.d("com.tts.miit", "The content is: " + contentAsString);
            c=contentAsString;
            return contentAsString;
        } catch (IOException e){
            Log.d("com.32s.miit", "The content is: " + c);
        }finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }



    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Override
    protected JSONObject doInBackground(ArrayList<ArrayList<String>>... array) {
        try {
            return new JSONObject(downloadFromUrl(array[0].get(0).get(0), array[0]));
        } catch (IOException e) {
            Log.d("com.32s.miit","Load failed");
            return null;
        } catch (JSONException e) {
            Log.d("com.32s.miit","Parsing failed");
            return null;
        }

    }
}