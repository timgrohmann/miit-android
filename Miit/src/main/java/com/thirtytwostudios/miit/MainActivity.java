package com.thirtytwostudios.miit;

/**
 * Created by FM on 24.11.2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
//import android.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.maps.MapFragment;

import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        Button toFriendButton = (Button) findViewById(R.id.submitButton);
        toFriendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent nextScreen = new Intent(getApplicationContext(), FriendsActivity.class);
                startActivity(nextScreen);
            }
        });

        login();

        MiitApi mApi = new MiitApi(this);
        mApi.me(new MiitApi.AsyncUserResponse() {
            public void complete(User user) {
                Log.d("com.32s.miit", user.toString());
            }
        });
        mApi.friends(new MiitApi.AsyncUsersResponse() {
            @Override
            public void complete(User[] users) {
                Log.d("com.32s.miit", "Anja hat so viele Freunde: " + String.valueOf(users.length));
                MapsActivity map = (MapsActivity) getFragmentManager().findFragmentById(R.id.map);
                map.updateUsers(users);
            }
        });


    }

    public void login(){
        final LoginButton mLogin = (LoginButton) findViewById(R.id.login_button);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        if (AccessToken.getCurrentAccessToken() == null){
            mLogin.setVisibility(View.VISIBLE);
        } else {
            mLogin.setVisibility(View.GONE);

        }
        // TODO: 11.02.16 Geht nicht:
        //Blurry.with(getApplicationContext()).radius(25).sampling(2).onto((ViewGroup) findViewById(R.id.wrapper));

        mLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //mLogin.setVisibility(View.GONE);
                Log.v("com.32s.miit", "login succeeded");
            }

            @Override
            public void onCancel() {
                Log.v("com.32s.miit", "login c");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.v("com.32s.miit", exception.toString());
            }
        });

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,
                resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent nextScreen = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(nextScreen);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}
