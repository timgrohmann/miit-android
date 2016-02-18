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
    Context c;

    public MiitApi(Context context) {
        this.c = context;
    }
    public void me(AsyncResponse<User> delegate) {
        ServerConnection sc = new ServerConnection(this.c);
        try {
            ArrayList<ArrayList<String>> request = new ArrayList<>();
            ArrayList<String> node = new ArrayList<>();
            node.add("me");
            request.add(node);
            //Parse



            JSONObject json = sc.execute(request).get();
            if (json == null) return;
            Integer status = json.getInt("status");
            if (status != 200) return;

            JSONObject data = json.getJSONObject("data");
            delegate.complete(new User(data));

        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void friends(AsyncResponse<User[]> delegate) {
        ServerConnection sc = new ServerConnection(this.c);
        try {
            ArrayList<ArrayList<String>> request = new ArrayList<>();
            ArrayList<String> node = new ArrayList<>();
            node.add("friends");
            request.add(node);

            JSONObject json = sc.execute(request).get();
            if (json == null) return;
            Integer status = json.getInt("status");
            if (status != 200) return;

            ArrayList<User> returnUsers = new ArrayList<>();

            JSONArray dataArray = json.getJSONArray("data");
            for (int i=0; i<dataArray.length(); i++) {
                JSONObject item = dataArray.getJSONObject(i);
                returnUsers.add(new User(item));
            }

            delegate.complete(returnUsers.toArray(new User[returnUsers.size()]));
        } catch (InterruptedException | ExecutionException | JSONException  e) {
            e.printStackTrace();
            Log.d("com.32s.miit", e.toString());
        }
    }


    public void updateLocation(Location loc){

        ServerConnection sc = new ServerConnection(this.c);
        ArrayList<ArrayList<String>> request = new ArrayList<>();
        ArrayList<String> node = new ArrayList<>();
        node.add("me/update");
        request.add(node);
        ArrayList<String> lat = new ArrayList<>();
        lat.add("lat");
        lat.add(String.valueOf(loc.getLatitude()));
        ArrayList<String> lon = new ArrayList<>();
        lon.add("lon");
        lon.add(String.valueOf(loc.getLongitude()));
        request.add(lat);
        request.add(lon);

        sc.execute(request);

    }

    public void getFriendshipRequests(AsyncResponse<FriendShipUser[]> delegate) {
        ServerConnection sc = new ServerConnection(this.c);
        try {
            ArrayList<ArrayList<String>> request = new ArrayList<>();
            ArrayList<String> node = new ArrayList<>();
            node.add("friendshipRequest/get");
            request.add(node);

            JSONObject json = sc.execute(request).get();
            if (json == null) return;
            Integer status = json.getInt("status");
            if (status != 200) return;

            ArrayList<FriendShipUser> returnUsers = new ArrayList<>();

            JSONArray dataArray = json.getJSONArray("data");
            for (int i=0; i<dataArray.length(); i++) {
                JSONObject item = dataArray.getJSONObject(i);
                returnUsers.add(new FriendShipUser(item));
            }

            delegate.complete(returnUsers.toArray(new FriendShipUser[returnUsers.size()]));
        } catch (InterruptedException | ExecutionException | JSONException  e) {
            e.printStackTrace();
            Log.d("com.32s.miit", e.toString());
        }
    }

    public void answerFriendshipRequests(Boolean accept, Integer id, AsyncResponse<Boolean> delegate) {
        ServerConnection sc = new ServerConnection(this.c);
        ArrayList<ArrayList<String>> request = new ArrayList<>();
        ArrayList<String> node = new ArrayList<>();
        if (accept){
            node.add("friendshipRequest/answer/accept");
        }else{
            node.add("friendshipRequest/answer/deny");
        }
        request.add(node);
        node = new ArrayList<>();
        node.add("otherFriend");
        node.add("" + id);
        request.add(node);

        sc.execute(request);

        delegate.complete(true);
    }


    interface AsyncResponse<T>{
        void complete(T response);
    }
}
