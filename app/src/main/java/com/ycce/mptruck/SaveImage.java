package com.ycce.mptruck;

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
    private int order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_image);
        imageView = (ImageView) findViewById(R.id.profile_image1);
        saveall = (Button) findViewById(R.id.saveall);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference1 = firebaseDatabase.getReference().child("orderno");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                order=Integer.parseInt(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference = firebaseDatabase.getReference().child("saved");
        databaseReference2 = firebaseDatabase.getReference().child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("saved");
        saveall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag!=1){
                    Toast.makeText(getApplicationContext(),"Pleas Upload Product Image",Toast.LENGTH_LONG).show();
                    return;
                }

                Bundle b = getIntent().getBundleExtra("bundle2");
                Set<String> key = b.keySet();
                for(String keys:key){
                       databaseReference.child("orderno"+order).child(keys).setValue(b.get(keys));
                }
                FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
                StorageReference riversRef = firebaseStorage.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("orderno"+order);
                riversRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(),"Product Photo Uploaded",Toast.LENGTH_LONG).show();
                    }
                });
                databaseReference2.child("orderno"+order).setValue("");
                databaseReference1.setValue(order+1);
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
