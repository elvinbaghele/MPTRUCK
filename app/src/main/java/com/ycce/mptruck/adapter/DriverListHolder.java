package com.ycce.mptruck.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ycce.mptruck.R;

/**
 * Created by ADMIN on 24/01/2018.
 */

public class DriverListHolder extends RecyclerView.ViewHolder
{
TextView driver_name,driver_status,cur_location,accept,est_cost;
ImageView vehicle_image;
    public DriverListHolder(View itemView) {
        super(itemView);
        driver_name = (TextView) itemView.findViewById(R.id.driver_name);
        driver_status = (TextView) itemView.findViewById(R.id.driver_status);
        cur_location = (TextView) itemView.findViewById(R.id.cur_location);
        accept = (TextView) itemView.findViewById(R.id.accept);
        est_cost = (TextView) itemView.findViewById(R.id.est_cost);
        vehicle_image = (ImageView) itemView.findViewById(R.id.vehicle_image);
    }
}