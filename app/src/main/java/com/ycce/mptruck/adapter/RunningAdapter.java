package com.ycce.mptruck.adapter;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentHostCallback;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ycce.mptruck.R;
import com.ycce.mptruck.TabActivity;

import java.util.ArrayList;
import java.util.HashMap;

import static android.Manifest.permission.CALL_PHONE;
import static android.content.ContentValues.TAG;

/**
 * Created by ADMIN on 26/01/2018.
 */

public class RunningAdapter extends RecyclerView.Adapter<RunningHolder> {
    ArrayList<HashMap<String, String>> list;
    Context context;
    Activity a;
    ImageView vehicle_image, product_image;
    HashMap<String, String> hashMap;
    private final int REQUEST_CALL = 1;
    Intent callIntent;
    public RunningAdapter(ArrayList<HashMap<String, String>> list, Context context, Activity a) {
        this.list = list;
        this.context = context;
        this.a=a;
    }

    public void setData(ArrayList<HashMap<String, String>> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public RunningHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RunningHolder(LayoutInflater.from(context).inflate(R.layout.start_running_layout, null));
    }

    @Override
    public void onBindViewHolder(RunningHolder holder, int position) {
        if (!list.isEmpty()) {
            hashMap = list.get(position);
            holder.orderno.setText("OrderId" + String.valueOf(hashMap.get("order")));
            holder.source.setText("Source: " + hashMap.get("saddress"));
            holder.destination.setText("Destination: " + hashMap.get("daddress"));
            holder.date.setText(hashMap.get("date") + " " + hashMap.get("Weight") + " " + hashMap.get("Height"));
            holder.driver_name.setText("Driver Name: " + hashMap.get("Name"));
            if (String.valueOf(hashMap.get("status")).equals("1"))
                holder.start.setEnabled(false);
            else if (String.valueOf(hashMap.get("status")).equals("2")) { // status= 2 ready flag by driver
                holder.start.setEnabled(true);
            } else {
                if (String.valueOf(hashMap.get("status")).equals("3")) {
                    holder.start.setText("Navigate");
                }
            }
        }
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String number = hashMap.get("phone");
                callIntent = new Intent(Intent.ACTION_CALL);
                //Toast.makeText(context,"Phone "+String.valueOf(hashMap.get("phone")),Toast.LENGTH_LONG).show();
                callIntent.setData(Uri.parse("tel:" + String.valueOf(hashMap.get("phone"))));

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions(a,new String[] {Manifest.permission.CALL_PHONE},REQUEST_CALL);

                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                }else{
                    context.startActivity(callIntent);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                             int[] grantResults){
        switch (requestCode){
            case REQUEST_CALL:
                if(grantResults.length> 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    context.startActivity(callIntent);
                }else{
                    //
                    Toast.makeText(context,"Permission Not Granted",Toast.LENGTH_LONG).show();
                }
        }
    }
}
