package com.ycce.mptruck;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.io.IOException;
import java.util.Set;

public class SaveImage extends AppCompatActivity {
    private int PICK_IMAGE_REQUEST = 1;
    private Uri uri;
    private ImageView imageView;
    private Button saveall;
    int flag=0;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,databaseReference1,databaseReference2;
    private ProgressDialog progressDialog;
    private int order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_image);
        imageView = (ImageView) findViewById(R.id.profile_image1);
        saveall = (Button) findViewById(R.id.saveall);
        progressDialog = new ProgressDialog(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference1 = firebaseDatabase.getReference().child("orderno");

        // for two or more apps working at the same time this order no may be same , so its the problem.
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                order=Integer.parseInt(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference = firebaseDatabase.getReference().child("orders");
        databaseReference2 = firebaseDatabase.getReference().child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("saved");
        saveall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Creating Order");
                progressDialog.setCancelable(false);

                if(flag!=1){
                    Toast.makeText(getApplicationContext(),"Pleas Upload Product Image",Toast.LENGTH_LONG).show();
                    return;
                }
                progressDialog.show();
                Bundle b = getIntent().getBundleExtra("bundle2");
                Set<String> key = b.keySet();
                for(String keys:key){
                       databaseReference.child("orderno"+order).child(keys).setValue(b.get(keys));
                }
                databaseReference.child("orderno"+order).child("driver_count").setValue(0);
                databaseReference.child("orderno"+order).child("order").setValue(order);
                FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
                StorageReference riversRef = firebaseStorage.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("orderno"+order);
                riversRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(),"Order Created Successfully, You can check the progress in the My Orders",Toast.LENGTH_LONG).show();
                    }
                });
                databaseReference2.child("orderno"+order).setValue("");
                databaseReference1.setValue(order+1);

                firebaseDatabase.getReference().child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("saved_count").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       // Log.d("Number of saved orders", (String) dataSnapshot.getValue());
                        firebaseDatabase.getReference().child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("saved_count").setValue((long)dataSnapshot.getValue()+1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Toast.makeText(getApplicationContext(),"Order placed successfully",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                finish();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
                flag=1;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
