package com.thirtytwostudios.miit;

/**
 * Created by FM on 28.11.2015.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FriendsActivityFragment extends Fragment {

    public FriendsActivityFragment(){
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        String [] friendlistArray = {
                "Peter",
                "Claus",
                "Deine Mutter"
        };

        List<String> friendlist = new ArrayList<>(Arrays.asList(friendlistArray));

        ArrayAdapter <String> friendlistAdapter =
                new ArrayAdapter<>(
                        getActivity(), // Die aktuelle Umgebung (diese Activity)
                        R.layout.list_item_friends, // ID der XML-Layout Datei
                        R.id.list_item_friendlist_textview, // ID des TextViews
                        friendlist); // Beispieldaten in einer ArrayList

        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        ListView friendlistListView = (ListView) rootView.findViewById(R.id.listView_friendlist);
        friendlistListView.setAdapter(friendlistAdapter);

        return rootView;

    }

}
