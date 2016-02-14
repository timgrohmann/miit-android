package com.thirtytwostudios.miit;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by timgrohmann on 13.02.16.
 */
public class MiitApi {
    Activity a;

    public MiitApi(Activity activity) {
        this.a = activity;
    }
    public void me(AsyncUserResponse delegate) {
        ServerConnection sc = new ServerConnection(this.a);
        try {
            ArrayList<ArrayList<String>> request = new ArrayList<ArrayList<String>>();
            ArrayList<String> node = new ArrayList<String>();
            node.add("me");
            request.add(node);
            //Parse



            JSONObject json = sc.execute(request).get();
            if (json == null) return;
            Integer status = json.getInt("status");
            if (status != 200) return;

            JSONObject data = json.getJSONObject("data");
            delegate.complete(new User(data));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    interface AsyncUserResponse{
        void complete(User user);
    }

    public void friends(AsyncUsersResponse delegate) {
        ServerConnection sc = new ServerConnection(this.a);
        try {
            ArrayList<ArrayList<String>> request = new ArrayList<ArrayList<String>>();
            ArrayList<String> node = new ArrayList<String>();
            node.add("friends");
            request.add(node);

            JSONObject json = sc.execute(request).get();
            Integer status = json.getInt("status");
            if (status != 200) return;

            ArrayList<User> returnUsers = new ArrayList<User>();

            JSONArray dataArray = json.getJSONArray("data");
            for (int i=0; i<dataArray.length(); i++) {
                JSONObject item = dataArray.getJSONObject(i);
                returnUsers.add(new User(item));
            }

            delegate.complete((User[]) returnUsers.toArray(new User[returnUsers.size()]));
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("com.32s.miit", e.toString());
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.d("com.32s.miit", e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("com.32s.miit", e.toString());
        }
    }
    interface AsyncUsersResponse{
        void complete(User[] users);
    }

    public void updateLocation(Location loc){

        ServerConnection sc = new ServerConnection(this.a);
        ArrayList<ArrayList<String>> request = new ArrayList<ArrayList<String>>();
        ArrayList<String> node = new ArrayList<String>();
        node.add("me/update");
        request.add(node);
        ArrayList<String> lat = new ArrayList<String>();
        lat.add("lat");
        lat.add(String.valueOf(loc.getLatitude()));
        ArrayList<String> lon = new ArrayList<String>();
        lon.add("lon");
        lon.add(String.valueOf(loc.getLongitude()));
        request.add(lat);
        request.add(lon);

        sc.execute(request);

    }
}
