package com.ycce.mptruck.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ycce.mptruck.R;

/**
 * Created by prathmesh zade on 02/26/2017.
 */

public class SavedHolder extends RecyclerView.ViewHolder
{
   TextView orderno,source,destination,textView6,count_drivers;
   ImageView product_image;
    public SavedHolder(View itemView)
    {
        super(itemView);
        orderno = (TextView) itemView.findViewById(R.id.orderno);
        source = (TextView) itemView.findViewById(R.id.source);
        destination = (TextView) itemView.findViewById(R.id.destination);
        textView6 = (TextView) itemView.findViewById(R.id.textView6);
        count_drivers = (TextView) itemView.findViewById(R.id.count_drivers);
        product_image = (ImageView) itemView.findViewById(R.id.product_image);

    }
}
