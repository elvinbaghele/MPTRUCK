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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;
import java.util.HashMap;

import com.ycce.mptruck.R;
import com.ycce.mptruck.adapter.CancleAdapter;
import com.ycce.mptruck.adapter.RunningAdapter;
import com.ycce.mptruck.adapter.SavedAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class F2 extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    ArrayList<HashMap<String,String>> list = null;
    RunningAdapter runningAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_f2, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView= (RecyclerView) getActivity().findViewById(R.id.start_list);
        final DatabaseReference savedReference = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        savedReference.child("saved_count").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long saved_count= (long) dataSnapshot.getValue();
                if(saved_count > 0){
                    savedReference.child("saved").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            HashMap<String,String> list1= (HashMap<String, String>) dataSnapshot.getValue(); // list of orderno created by user
                            list=new ArrayList<>();
                            runningAdapter =new RunningAdapter(list,getContext(),getActivity());
                            recyclerView.setAdapter(runningAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            for(String key: list1.keySet()){
                                final DatabaseReference orders = FirebaseDatabase.getInstance().getReference().child("orders").child(key);
                                orders.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                        final HashMap<String,String>list2= (HashMap<String, String>) dataSnapshot.getValue(); //order details

                                        //Toast.makeText(getContext(),"status : "+ String.valueOf(list2.get("status")),Toast.LENGTH_LONG).show();
                                        //adding data of an order as hashMap to the arraylist, which is passed to the SavedAdapter
                                        if(String.valueOf(list2.get("status")).equals("1")){

                                            //Running status start disabled
                                            orders.child("accepted").child("by").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    HashMap<String,String>list3= (HashMap<String, String>) dataSnapshot.getValue(); //driver ids
                                                    for(String key: list3.keySet()){

                                                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("driver").child(key);
                                                        databaseReference1.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                HashMap<String,String>list4= (HashMap<String, String>) dataSnapshot.getValue(); //driver data
                                                                Toast.makeText(getContext(),"driver "+list4.get("Name"),Toast.LENGTH_LONG).show();
                                                                list2.putAll(list4);
                                                                list.add(list2);
                                                                runningAdapter.setData(list);
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{
                    // Display no order placed
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
    }
}

