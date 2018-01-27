package com.ycce.mptruck;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{
    private TextView e1,e2;
    private int field;
    int locksource =0,lockdestination= 0;
    private String sname,saddress,dname,daddress,name,address;
    private Double slatitude,slongitude,dlatitude,dlongitude,latitude,longitude;
    private Button next;
    GoogleMap mMap;
    Marker Origin,Destination;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1 = (TextView) findViewById(R.id.findPlace);
        e2 = (TextView) findViewById(R.id.findp);

        //to bring the fields over Map
        e1.bringToFront();
        e2.bringToFront();

        next = (Button) findViewById(R.id.next1);

        //Create bundle of source and destination location and start Activity_HW activity to collect other details
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(lockdestination>0 && locksource>0) {
                    Intent i = new Intent(getApplicationContext(), Activity_WH.class);
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
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    startActivity(i);
                    finish();
                }else {
                    e1.setError("Select origin");
                    e2.setError("Select destinaion");
                }
            }
        });

    }
    //called on click of fields, set from the layout xml file
    public void findPlace(View view) {
        switch(view.getId()) {
            case R.id.findp: field = 2; //state if destination location
            break;
            case R.id.findPlace: field =1; //state if source location
            break;
        }
        try {
            AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(Place.TYPE_COUNTRY)
                    .setCountry("IN")
                    .build();
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(autocompleteFilter)
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
                //Source locations
                if(field == 1){
                    slatitude=latitude;
                    slongitude=longitude;
                    sname=name;
                    name="Source :" +sname;
                    saddress=address;
        locksource++;
                    e1.setText(sname);
                }else
                {
                    //destination location
                    dlatitude=latitude;
                    dlongitude=longitude;
                    dname=name;
                    name="Destination :" +dname;
                    daddress=address;
lockdestination++;
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

    //To add marker on map
    public void onMapReady(GoogleMap map){
        mMap=map;
        LatLng nagpur = new LatLng(latitude,longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(nagpur, 16.0f));


        if(field==1)
         map.addMarker( new MarkerOptions()
                .title(name)
                .snippet(address)
                .position(nagpur))
                .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        else
            map.addMarker( new MarkerOptions()
                    .title(name)
                    .snippet(address)
                    .position(nagpur));

        if(lockdestination>0 && locksource>0){

            LatLng origin = new LatLng(slatitude,slongitude);
            LatLng dest = new LatLng(dlatitude,dlongitude);

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }

    }
    private class DownloadTask extends AsyncTask {


        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }


        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            Log.d("Path",result.toString());
            ParserTask parserTask = new ParserTask();


            parserTask.execute(result.toString());

        }


        @Override
        protected Object doInBackground(Object[] objects) {

            String data = "";

            try {
                data = downloadUrl((String) objects[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.d("Path",data);
            return data;
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("Path",routes.toString());
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble((String) point.get("lat"));
                    double lng = Double.parseDouble((String) point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(14);
                lineOptions.color(Color.CYAN);
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // To load menu bar
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
                startActivity(new Intent(this,TabActivity.class));
                break;
            case R.id.action_profile:
                //Toast.makeText(this,"Coming Soon",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,ScrollingActivity.class));
                break;

            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(this,OTP.class));
                finish();
                break;
        }

        return true;
    }
}
