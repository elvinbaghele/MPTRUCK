package com.ycce.mptruck.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.ycce.mptruck.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class F3 extends Fragment {

    FirebaseDatabase database;
    DatabaseReference ngo;
    ListView listView;
    ArrayList<String> list=null;
    ArrayAdapter<String> adapter;
    public F3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_f3, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView= (ListView) getActivity().findViewById(R.id.ngolist);
       /* database = FirebaseDatabase.getInstance();
        ngo = database.getReferenceFromUrl("https://ngo-test-6b3e0.firebaseio.com/NGOS");

        ngo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list=new ArrayList<String>();
                adapter = new  ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, list);
                listView.setAdapter(adapter);

                HashMap<String,String> map= (HashMap<String, String>) dataSnapshot.getValue();
                Set<String> keys= map.keySet();
                Iterator i=keys.iterator();
                while(i.hasNext()){
                    String ngoid=i.next().toString();

                    ngo.child(ngoid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            HashMap<String,String> map1= (HashMap<String, String>) dataSnapshot.getValue();
                            String name=map1.get("name");
                            String bname=map1.get("bname");
                            String acc=map1.get("acc");
                            String ifsc=map1.get("ifsc");
                            String listst="NGO \t\t\t\t\t\t "+name+"\nBank \t\t\t\t\t "+bname+"\n IFSC : "+ifsc+"\nACC no \t\t\t "+acc+
                                    "\nAddress \t\t "+map1.get("address")+"\nEmail \t\t\t\t\t "+map1.get("email")+"\nMobile \t\t\t\t "+map1.get("mobile")+
                                    "\nWebsite\t\t\t\t "+map1.get("website")+"\nAbout Us\t\t\t "+map1.get("desc");
                           // Log.d("listst",listst+"");
                            //list.add(listst);
                            adapter.addAll(listst);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }


                //listView.setAdapter(adapter);

                Log.d("darta",list.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }
}
