package com.thirtytwostudios.miit;

/**
 * Created by FM on 24.11.2015.
 */

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
//import android.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    User[] users;
    MapsActivity theMap = null;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);


        login();



        startService(new Intent(this, LocationService.class));

        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_location);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
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
        refresh();

    }
    public void refresh(){
        MiitApi mApi = new MiitApi(this);
        mApi.me(new MiitApi.AsyncResponse<User>() {
            public void complete(User user) {
                Log.d("com.32s.miit", user.toString());
            }
        });
        theMap = (MapsActivity) getFragmentManager().findFragmentById(R.id.map);
        mApi.friends(new MiitApi.AsyncResponse<User[]>() {
            @Override
            public void complete(User[] users) {
                Log.d("com.32s.miit", "Anja hat so viele Freunde: " + String.valueOf(users.length));
                theMap.updateUsers(users);
                ((SwipeRefreshLayout) findViewById(R.id.refresh_location)).setRefreshing(false);
                populateFriendsList(users);
            }
        });
    }

    public void checkFriendshipRequests() {
        MiitApi mApi = new MiitApi(this);
        mApi.getFriendshipRequests(new MiitApi.AsyncResponse<FriendShipUser[]>() {
            public void complete(FriendShipUser[] users) {
                if (users.length != 0){
                    //New friendshiprequest
                }else{

                }
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

        menu.findItem(R.id.action_FriendShipRequest).setVisible(true);

        this.menu = menu;
        return true;
    }

    public void populateFriendsList(final User[] users){
        this.users = users;
        //Populate Friendslist
        ListView fListView = (ListView) findViewById(R.id.friendListView);
        fListView.setAdapter(new friendAdapter());
        fListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User clickedUser = MainActivity.this.users[position];
                if (clickedUser == null || theMap == null) return;
                theMap.focusOnUser(clickedUser);
            }
        });
    }

    private class friendAdapter extends ArrayAdapter<User>{

        public friendAdapter(){
            super(MainActivity.this, R.layout.friend_list_item, MainActivity.this.users);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View thisView = convertView;
            if (thisView == null){
                thisView = getLayoutInflater().inflate(R.layout.friend_list_item, parent, false);
            }
            User currentUser = users[position];
            if (currentUser == null) return null;

            ((TextView) thisView.findViewById(R.id.usernameTextView)).setText(currentUser.username);

            ((SimpleDraweeView) thisView.findViewById(R.id.listProfilePictureView)).setImageURI(Uri.parse("https://graph.facebook.com/" + currentUser.fbUserId + "/picture?type=large"));


            return thisView;
        }

    }

    public void showUserDetails(){

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent nextScreen = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(nextScreen);
                return true;
            case R.id.action_FriendShipRequest:
                startActivity(new Intent(getApplicationContext(), FriendshipRequestsActivity.class));
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
