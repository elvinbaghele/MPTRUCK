package com.ycce.mptruck;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Activity_WH extends AppCompatActivity {
    private Spinner industry;
    private ToggleButton fp;
    private EditText weight,height;
    private Button next,back,btnDate;
    private int year;
    private int month;
    private int day;
    private DatePicker date_picker;
    static final int DATE_DIALOG_ID = 100;


    private String load_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity__wh);
        init();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fp.getText().toString().equals("Full Load (switch to Part Load)"))
                {   load_type="full_load";

                }else{
                    load_type="part_load";
                }
                if(weight.getText().toString().isEmpty()){
                    weight.setError("Please Enter the approximate weight");
                    return;
                }
                if(height.getText().toString().isEmpty()){
                    height.setError("Please Enter the approximate height");
                    return;
                }
                if(industry.getSelectedItemPosition()<1){
                    Toast t1 = Toast.makeText(getApplicationContext(),industry.getSelectedItem().toString()+"",Toast.LENGTH_LONG);
                    t1.setGravity(Gravity.CENTER,0,0);
                    t1.show();
                    return;
                }
                if(btnDate.getText().toString().equalsIgnoreCase("Set Date")){
                    Toast t1 = Toast.makeText(getApplicationContext(),btnDate.getText().toString()+"",Toast.LENGTH_LONG);
                    t1.setGravity(Gravity.CENTER,0,0);
                    t1.show();
                    return;
                }
                Intent i = new Intent(getApplicationContext(),SaveImage.class);
                Bundle bundle2= new Bundle();
                bundle2.putString("Weight",weight.getText().toString());
                bundle2.putString("Height",height.getText().toString());
                bundle2.putString("Industry",industry.getSelectedItem().toString());
                bundle2.putString("load_type",load_type);
                bundle2.putString("date",btnDate.getText().toString());
                bundle2.putAll(getIntent().getBundleExtra("bundle1"));
                i.putExtra("bundle2",bundle2);
                startActivity(i);
                finish();


            }
        });
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

    }
    protected Dialog onCreateDialog(int id) {

        switch (id) {

            case DATE_DIALOG_ID:

                // set date picker as current date

                return new DatePickerDialog(this, datePickerListener, year, month,day);

        }

        return null;

    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.

        public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {

            year = selectedYear;
           month = selectedMonth;

            day = selectedDay;

            // set selected date into Text View

            btnDate.setText(new StringBuilder().append(month + 1)

                            .append("-").append(day).append("-").append(year).append(" "));

            // set selected date into Date Picker

            //date_picker.init(year, month, day, null);

        }

    };



    private void init() {
        industry = (Spinner) findViewById(R.id.spinner_industry);
        weight = (EditText) findViewById(R.id.weight);
        height = (EditText) findViewById(R.id.height);
        back = (Button) findViewById(R.id.back);
        next = (Button) findViewById(R.id.next);
        fp = (ToggleButton) findViewById(R.id.toggleButton);
        btnDate = (Button) findViewById(R.id.btnDate);

    }

}


