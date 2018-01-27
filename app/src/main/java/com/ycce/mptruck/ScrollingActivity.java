package com.ycce.mptruck;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;

public class ScrollingActivity extends AppCompatActivity {
    ImageView user_profile;
    TextView username,editoption;
    EditText Email,Phone,Address;
    int lock;
    private Uri uri;
    private int flag= 0;
    private final int PICK_IMAGE_REQUEST = 101;
    //This funtion takes the array of views, and Enable them so that they can be accessed
    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
        }
        lock = 1;
    }

    //This funtion takes the array of views, and disable them so that they cannot be accessed
    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
        }
        lock = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        getSupportActionBar();
        init();
        disableViews(Email,Address,Phone,user_profile);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,String> stringStringHashMap = (HashMap<String, String>) dataSnapshot.getValue();
                Email.setText(stringStringHashMap.get("email"));
                Phone.setText(stringStringHashMap.get("phoneNo"));
                Address.setText(stringStringHashMap.get("address"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_photos");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getApplicationContext()).load(uri).fit().into(user_profile);
            }
        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                }
        );
    }

    private void init() {
        Email = (EditText) findViewById(R.id.user_email);
        Phone = (EditText) findViewById(R.id.user_phone);
        Address = (EditText) findViewById(R.id.user_address);
        username = (TextView) findViewById(R.id.username);
        editoption = (TextView) findViewById(R.id.editoption);
        user_profile = (ImageView) findViewById(R.id.user_image);

        editoption.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(lock==0){
                    enableViews(Email,Address,Phone,user_profile);
                    user_profile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                        }
                    });

                }else{
                    disableViews(Email,Address,Phone,user_profile);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    //TODO:create Email and Phone no validations
                    databaseReference.child("email").setValue(Email.getText().toString());
                    databaseReference.child("address").setValue(Address.getText().toString());
                    databaseReference.child("phoneNo").setValue(Phone.getText().toString());
                    //TODO: create validation for size of image
                    FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
                    StorageReference riversRef = firebaseStorage.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_photos");
                    riversRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(),"Profile Saved Successfully",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                user_profile.setImageBitmap(bitmap);
                flag=1;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_history:
                startActivity(new Intent(this,TabActivity.class));
                finish();
                break;
            case R.id.action_profile:
                //Toast.makeText(this,"Coming Soon",Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(this,ScrollingActivity.class));
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
