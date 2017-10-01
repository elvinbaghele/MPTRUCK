package com.ycce.mptruck.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import com.ycce.mptruck.R;

/**
 * Created by prathmesh zade on 02/26/2017.
 */

public class SavedAdapter extends RecyclerView.Adapter<SavedHolder> {

    ArrayList<HashMap<String,String>> list;
    Context context;

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
        return new SavedHolder(LayoutInflater.from(context).inflate(R.layout.new_custom_layout,null));
    }

    @Override
    public void onBindViewHolder(SavedHolder holder, int position)
    {
        if(!list.isEmpty()) {
            HashMap<String, String> hashMap = list.get(position);
            holder.head.setText(hashMap.get("headline"));
            holder.desc.setText(hashMap.get("description"));
            holder.name.setText("Posted By - " +hashMap.get("name"));

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
