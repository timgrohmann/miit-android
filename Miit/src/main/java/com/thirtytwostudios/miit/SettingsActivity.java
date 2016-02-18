package com.thirtytwostudios.miit;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.widget.ProfilePictureView;


/**
 * Created by timgrohmann on 13.02.16.
 */
public class SettingsActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ((SimpleDraweeView) findViewById(R.id.profilePictureView)).setImageURI(Uri.parse("https://graph.facebook.com/" + AccessToken.getCurrentAccessToken().getUserId() + "/picture?type=large"));
        MiitApi mApi = new MiitApi(this);
        mApi.me(new MiitApi.AsyncResponse<User>() {
            public void complete(User user) {
                ((TextView) findViewById(R.id.realnametxtfield)).setText(user.name);
                ((TextView) findViewById(R.id.usernametextfield)).setText(user.username);


                Log.d("com.32s.miit", user.toString());
            }
        });
    }

}
