package com.ycce.mptruck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private String name,address;
    private Double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1 = (EditText) findViewById(R.id.findPlace);
        e2 = (EditText) findViewById(R.id.findp);
        e1.bringToFront();
        e2.bringToFront();

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
                    e1.setText(name);
                }else
                {
                    e2.setText(name);
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
        LatLng sydney = new LatLng(latitude,longitude);


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
        map.addMarker( new MarkerOptions()
                .title(name)
                .snippet(address)
                .position(sydney));
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
