package com.ycce.mptruck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ycce.mptruck.adapter.DriverListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class Drivers_list extends AppCompatActivity implements View.OnClickListener{
    RecyclerView recyclerView ;
    DriverListAdapter driverListAdapter;
    String orderno;
    ArrayList<HashMap<String,String>> list = null;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers_list);
        recyclerView= (RecyclerView) findViewById(R.id.driver_list);
         orderno = getIntent().getBundleExtra("bundle").get("order").toString();
         databaseReference = FirebaseDatabase.getInstance().getReference().child("orders");
        databaseReference.child("orderno"+orderno).child("accepted").child("did").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final HashMap<String,String> hashMapHashMap = (HashMap<String, String>) dataSnapshot.getValue();
                list=new ArrayList<>();
                driverListAdapter =new DriverListAdapter(list,getApplicationContext());
                recyclerView.setAdapter(driverListAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                for(final String driverid : hashMapHashMap.keySet()){
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("driver");
                    databaseReference1.child(driverid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final HashMap<String,String> hashMapHashMap1 = (HashMap<String, String>) dataSnapshot.getValue();
                            //TODO: Delete below two lines
                            String driverName = hashMapHashMap1.get("Name");
                            Log.d("Driver Name: ",driverName);
                            databaseReference.child("orderno"+orderno).child("accepted").child("did").child(driverid).child("estimated_cost").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    hashMapHashMap1.put("estimated_cost",dataSnapshot.getValue()+"");
                                    hashMapHashMap1.put("orderno",orderno);
                                    hashMapHashMap1.put("driverid",driverid);
                                    list.add(hashMapHashMap1);
                                    driverListAdapter.setData(list);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
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

    @Override
    public void onClick(View v) {

    }
}
