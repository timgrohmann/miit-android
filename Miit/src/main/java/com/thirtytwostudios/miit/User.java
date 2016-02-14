package com.thirtytwostudios.miit;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by timgrohmann on 13.02.16.
 */
public class User {
    Integer id;
    String username;
    String name;
    LatLng location;
    Integer timelastupdated;
    String fbUserId;

    public User(Integer id, String username, String name, LatLng location, Integer timelastupdated, String fbUserId){
        this.id = id;
        this.username = username;
        this.name = name;
        this.location = location;
        this.timelastupdated = timelastupdated;
        this.fbUserId = fbUserId;
    }

    public User(JSONObject data) throws JSONException{
        this.id = data.getInt("id");
        this.username = data.getString("username");
        this.name = data.getString("name");
        this.location = new LatLng(data.getDouble("latitude"),data.getDouble("longitude"));
        this.timelastupdated = data.getInt("timelastupdated");
        this.fbUserId = data.getString("fbUserId");
    }
}
