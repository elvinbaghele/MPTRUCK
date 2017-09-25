package com.ycce.mptruck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{
    private EditText e1,e2;
    private int field;
    private String sname,saddress,dname,daddress,name,address;
    private Double slatitude,slongitude,dlatitude,dlongitude,latitude,longitude;
    private Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1 = (EditText) findViewById(R.id.findPlace);
        e2 = (EditText) findViewById(R.id.findp);
        e1.bringToFront();
        e2.bringToFront();
        next = (Button) findViewById(R.id.next1);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(),Activity_WH.class);
                  try {
                      Bundle bundle = new Bundle();
                      bundle.putString("sname", sname);
                      bundle.putString("saddress", saddress);
                      bundle.putDouble("slatitude", slatitude);
                      bundle.putDouble("slongitude", slongitude);
                      bundle.putString("dname", dname);
                      bundle.putString("daddress", daddress);
                      bundle.putDouble("dlatitude", dlatitude);
                      bundle.putDouble("dlongitude", dlongitude);
                      i.putExtra("bundle1", bundle);
                  }catch(NullPointerException e){
                      e.printStackTrace();
                  }
                startActivity(i);
                finish();


            }
        });

    }
//dasdasd
    public void findPlace(View view) {
        switch(view.getId()) {
            case R.id.findp: field = 2;
            break;
            case R.id.findPlace: field =1;
            break;
        }        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);

            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber() + place.getLatLng().latitude);


                latitude=place.getLatLng().latitude;
                longitude=place.getLatLng().longitude;
                name=place.getName().toString();
                address=place.getAddress().toString();
                if(field == 1){
                    slatitude=place.getLatLng().latitude;
                    slongitude=place.getLatLng().longitude;
                    sname=place.getName().toString();

                    name="Source :" +name;
                    saddress=place.getAddress().toString();
                    e1.setText(sname);
                }else
                {
                    dlatitude=place.getLatLng().latitude;
                    dlongitude=place.getLatLng().longitude;
                    dname=place.getName().toString();
                    name="Destination :" +name;
                    daddress=place.getAddress().toString();
                    e2.setText(dname);
                }
                SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);


//                        ((TextView) findViewById(R.id.searched_address)).setText(place.getName() + ",\n" +
//                        place.getAddress() + "\n" + place.getPhoneNumber());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void onMapReady(GoogleMap map){
        LatLng nagpur = new LatLng(latitude,longitude);


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(nagpur, 16.0f));
        map.addMarker( new MarkerOptions()
                .title(name)
                .snippet(address)
                .position(nagpur));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_history:
                Toast.makeText(this,"Coming Soon",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_profile:
                Toast.makeText(this,"Coming Soon",Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,OTP.class));
                break;
        }

        return true;
    }
}
