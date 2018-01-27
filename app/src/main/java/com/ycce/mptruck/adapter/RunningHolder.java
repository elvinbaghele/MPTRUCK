package com.ycce.mptruck.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ycce.mptruck.R;

/**
 * Created by ADMIN on 26/01/2018.
 */

public class RunningHolder extends RecyclerView.ViewHolder {
    TextView destination,source,date,orderno,driver_name;
    Button start,cancel,call;
    ImageView vehicle_image_start;
    ImageView product_image_start;
    public RunningHolder(View itemView) {
        super(itemView);
        destination = (TextView) itemView.findViewById(R.id.destination_start);
        source= (TextView) itemView.findViewById(R.id.source_start);
        date= (TextView) itemView.findViewById(R.id.textView7);
        orderno= (TextView) itemView.findViewById(R.id.orderno_start);
        driver_name = (TextView) itemView.findViewById(R.id.driver_name_start);

        vehicle_image_start = (ImageView) itemView.findViewById(R.id.vehicle_image_start);
        product_image_start = (ImageView) itemView.findViewById(R.id.product_image_start);

        start = (Button) itemView.findViewById(R.id.start);
        call = (Button) itemView.findViewById(R.id.call_start);
        cancel = (Button) itemView.findViewById(R.id.cancle_start);

    }
}
