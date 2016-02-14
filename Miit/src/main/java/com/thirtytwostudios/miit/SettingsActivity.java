package com.thirtytwostudios.miit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;


/**
 * Created by timgrohmann on 13.02.16.
 */
public class SettingsActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.profilePictureView);

        MiitApi mApi = new MiitApi(this);
        mApi.me(new MiitApi.AsyncUserResponse() {
            public void complete(User user) {
                ((TextView) findViewById(R.id.realnametxtfield)).setText(user.name);
                ((TextView) findViewById(R.id.usernametextfield)).setText(user.username);
                profilePictureView.setProfileId(user.fbUserId);
                profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
                Log.d("com.32s.miit", user.toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MiitApi mApi = new MiitApi(this);
        mApi.me(new MiitApi.AsyncUserResponse() {
            public void complete(User user) {
                ((TextView) findViewById(R.id.realnametxtfield)).setText(user.name);
                ((TextView) findViewById(R.id.usernametextfield)).setText(user.username);
                Log.d("com.32s.miit", user.toString());
            }
        });
    }
}
