package com.ycce.mptruck.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ycce.mptruck.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ADMIN on 25/01/2018.
 */

public class DriverListAdapter extends RecyclerView.Adapter<DriverListHolder> {
    ArrayList<HashMap<String,String>> list;
    Context context;
    ImageView vehicle_image;
    HashMap<String, String> hashMap;

    public DriverListAdapter(ArrayList<HashMap<String, String>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setData(ArrayList<HashMap<String,String>> list)
    {
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public DriverListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DriverListHolder(LayoutInflater.from(context).inflate(R.layout.driver_list_component,null));
    }

    @Override
    public void onBindViewHolder(DriverListHolder holder, int position) {
        if(!list.isEmpty()){
            hashMap = list.get(position);
            holder.driver_name.setText(hashMap.get("Name"));
            holder.est_cost.setText(hashMap.get("estimated_cost"));
        }
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference().child("orders").child("orderno"+hashMap.get("orderno"));
                databaseReference.child("status").setValue(1); // Changing status to accepted by the user for this driver
                databaseReference.child("accepted").child("by").child(hashMap.get("driverid")).child("estimated_cost").setValue(hashMap.get("estimated_cost"));
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
