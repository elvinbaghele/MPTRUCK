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

public class CompleteAdapter extends RecyclerView.Adapter<SavedHolder> {

    ArrayList<HashMap<String,String>> list;
    Context context;
    String help;
    private clicklistener listener;

    public CompleteAdapter(Context context, ArrayList<HashMap<String,String>> list) {
        this.context=context;
        this.list=list;
    }
    public void setData(ArrayList<HashMap<String, String>> list, String help)
    {
        this.list=list;
        notifyDataSetChanged();
        this.help=help;
    }
    @Override
    public SavedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SavedHolder(LayoutInflater.from(context).inflate(R.layout.order_layout,null));
    }

    @Override
    public void onBindViewHolder(SavedHolder holder, final int position)
    {
        if(!list.isEmpty()) {
//            final HashMap<String, String> hashMap = list.get(position);
//            holder.head.setText(hashMap.get("name"));
//            holder.desc.setText(hashMap.get("title"));
//            holder.name.setText(hashMap.get("description"));
//            if (help.equals("help"))
//            {
//                holder.button.setVisibility(View.VISIBLE);
//                holder.button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        listener.click(position,hashMap);
//                    }
//                });
//            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setListener(clicklistener listener) {
        this.listener = listener;
    }
    public interface clicklistener
    {
        public void click(int no, HashMap<String, String> hashMap);
    }
}
