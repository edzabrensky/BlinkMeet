package com.example.edward.firebaseproject2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class ProfileActivity extends AppCompatActivity {
    private ImageView imageView;
    private Firebase rootRef;
    private Firebase userRef;
    private String picURL;
    private StorageReference storageRef;
    private String x;
    private int y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//        imageView = (ImageView)findViewById(R.id.user_profile_photo);
        imageView = (ImageView)findViewById(R.id.user_profile_photo);
        rootRef = new Firebase("https://tutorial2-d6f2e.firebaseio.com/");
        userRef = rootRef.child("users");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://tutorial2-d6f2e.appspot.com");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        final String uid = intent.getStringExtra("uid");

        userRef.child(uid).child("picURL").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                picURL = (String) dataSnapshot.getValue();
                System.out.println(picURL + "%f");
                if(picURL.contains("h") && dataSnapshot.getValue() != null){
                    System.out.println("%f reached branch");
                    x = picURL;
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference q = storageRef.child("users").child(uid);

                    q.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            imageView.setImageBitmap(bitmap);
                            // Use the bytes to display the image
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });

                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }
}
