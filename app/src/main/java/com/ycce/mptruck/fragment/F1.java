package com.ycce.mptruck.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;
import java.util.HashMap;

import com.ycce.mptruck.R;
import com.ycce.mptruck.adapter.SavedAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class F1 extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    ArrayList<HashMap<String,String>> list = null;
    SavedAdapter savedAdapter;

    EditText news_name,news_headline,news_description,news_source;
    long count;
    FirebaseDatabase database;
    DatabaseReference counterRef,user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_f1, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView= (RecyclerView) getActivity().findViewById(R.id.news);
/*
        database = FirebaseDatabase.getInstance();
        counterRef = database.getReferenceFromUrl("https://ngo-test-6b3e0.firebaseio.com/news_count");

        counterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count = (Long) dataSnapshot.getValue();
                Log.d("aaaaaa","change"+count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       */
        /*final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference newsRef=database.getReferenceFromUrl("https://ngo-test-6b3e0.firebaseio.com/news");
        newsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list=new ArrayList<>();
                savedAdapter =new SavedAdapter(getContext(),list);
                recyclerView.setAdapter(savedAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                ArrayList<HashMap<String,String>> list1= (ArrayList<HashMap<String, String>>) dataSnapshot.getValue();

                    for (int i = (list1.size() - 1); i > 0; i--) {
                        HashMap<String, String> hashMap = list1.get(i);
                        list.add(hashMap);
                    }
                    savedAdapter.setData(list);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    public void onClick(View v) {

    }
}
