package com.thirtytwostudios.miit;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by timgrohmann on 18.02.16.
 */
public class FriendshipRequestsActivity extends AppCompatActivity{

    private FriendShipUser[] users = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendshiprequests);
        refresh();
    }

    public void refresh(){
        MiitApi mApi = new MiitApi(this);
        mApi.getFriendshipRequests(new MiitApi.AsyncResponse<FriendShipUser[]>() {
            @Override
            public void complete(FriendShipUser[] users) {
                populateRequestsList(users);
            }
        });
    }

    public void populateRequestsList(final FriendShipUser[] users){
        this.users = users;
        //Populate Friendslist
        ListView fListView = (ListView) findViewById(R.id.friendshipsListView);
        fListView.setAdapter(new requestAdapter());

    }

    private class requestAdapter extends ArrayAdapter<FriendShipUser> {

        public requestAdapter(){
            super(FriendshipRequestsActivity.this, R.layout.friendshiprequest_list_item, FriendshipRequestsActivity.this.users);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View thisView = convertView;
            if (thisView == null){
                thisView = getLayoutInflater().inflate(R.layout.friendshiprequest_list_item, parent, false);
            }
            FriendShipUser currentUser = users[position];
            if (currentUser == null) return null;

            ((TextView) thisView.findViewById(R.id.usernameTextView)).setText(currentUser.username);

            ((SimpleDraweeView) thisView.findViewById(R.id.listProfilePictureView)).setImageURI(Uri.parse("https://graph.facebook.com/" + currentUser.fbUserId + "/picture?type=large"));
            ((ImageButton) thisView.findViewById(R.id.acceptButton)).setOnClickListener(new answerListener(currentUser.id, true));
            ((ImageButton) thisView.findViewById(R.id.denyButton)).setOnClickListener(new answerListener(currentUser.id,false));

            return thisView;
        }

    }

    public class answerListener implements View.OnClickListener{

        private Integer id;
        private Boolean accept;

        public answerListener(Integer id, Boolean accept){
            this.id = id;
            this.accept = accept;
        }

        @Override
        public void onClick(View v) {
            MiitApi mAPI = new MiitApi(FriendshipRequestsActivity.this);
            mAPI.answerFriendshipRequests(this.accept, this.id, new MiitApi.AsyncResponse<Boolean>() {
                @Override
                public void complete(Boolean response) {
                    refresh();
                }
            });
        }
    }
}
