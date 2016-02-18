package com.thirtytwostudios.miit;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by timgrohmann on 18.02.16.
 */
public class FriendShipUser {
    Integer id;
    String username;
    String name;
    String fbUserId;

    public FriendShipUser(Integer id, String username, String name, String fbUserId){
        this.id = id;
        this.username = username;
        this.name = name;
        this.fbUserId = fbUserId;
    }

    public FriendShipUser(JSONObject data) throws JSONException {
        this.id = data.getInt("id");
        this.username = data.getString("username");
        this.name = data.getString("name");
        this.fbUserId = data.getString("fbUserId");
    }
}
