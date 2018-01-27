package com.ycce.mptruck.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.ycce.mptruck.Drivers_list;
import com.ycce.mptruck.R;

/**
 * Created by prathmesh zade on 02/26/2017.
 */

public class SavedAdapter extends RecyclerView.Adapter<SavedHolder> {

    ArrayList<HashMap<String,String>> list;
    Context context;
    ImageView product_image;
    HashMap<String, String> hashMap;
    //private clicklistener listener;

    public SavedAdapter(Context context, ArrayList<HashMap<String,String>> list) {
        this.context=context;
        this.list=list;

    }

    public void setData(ArrayList<HashMap<String,String>> list)
    {
        this.list=list;
        notifyDataSetChanged();
    }
    @Override
    public SavedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SavedHolder(LayoutInflater.from(context).inflate(R.layout.order_layout,null));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final SavedHolder holder, int position)
    {
        if(!list.isEmpty()) {
           hashMap = list.get(position);
           holder.orderno.setText("OrderId"+ String.valueOf(hashMap.get("order")));
           holder.source.setText("Source: "+hashMap.get("saddress"));
           holder.destination.setText("Destination: " +hashMap.get("daddress"));
           holder.textView6.setText(hashMap.get("date")+" "+hashMap.get("Weight")+" "+hashMap.get("Height"));
           holder.count_drivers.setText(String.valueOf(hashMap.get("driver_count")));
           // product_image = holder.product_image ;
            Log.d("Order No: ",String.valueOf(hashMap.get("order")));
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("orderno"+String.valueOf(hashMap.get("order")));
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context).load(uri).fit().into(holder.product_image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }

        if(Integer.parseInt(holder.count_drivers.getText().toString()) > 0){
            holder.count_drivers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(context,"Driver Count", Toast.LENGTH_LONG).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("order",String.valueOf(hashMap.get("order")));
                    Intent i = new Intent(context, Drivers_list.class);
                    i.putExtra("bundle",bundle);
                    context.startActivity(i);


                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
