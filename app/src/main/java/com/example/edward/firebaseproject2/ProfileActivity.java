package com.example.edward.firebaseproject2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;

public class ProfileActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView tv;
    private TextView tvGender;
    private TextView tvSexuality;
    private TextView tvRelationship;
    private Button wink;
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
        tv = (TextView) findViewById(R.id.user_profile_name);
        tvGender = (TextView)findViewById(R.id.tvGender);
        tvSexuality = (TextView)findViewById(R.id.tvSexuality);
        tvRelationship = (TextView)findViewById(R.id.tvRelationship);
        wink = (Button) findViewById(R.id.bWink);
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
        if(uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())) {
            System.out.println("%T");
            wink.setVisibility(View.INVISIBLE);
        }
        System.out.println(uid + "%f");

//        userRef.child(uid).child("picURL").addValueEventListener(new ValueEventListener() {
        userRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tv.setText(dataSnapshot.child("name").getValue().toString());
                tvGender.setText("Gender: " + dataSnapshot.child("gender").getValue().toString());
                tvRelationship.setText("Relationship: " + dataSnapshot.child("relationshipStatus").getValue().toString());
                tvSexuality.setText("Sexuality: " + dataSnapshot.child("sexuality").getValue().toString());
                picURL = (String) dataSnapshot.child("picURL").getValue();
                System.out.println(picURL + "%f");
                if(picURL.contains("h") && dataSnapshot.getValue() != null){
                    System.out.println("%f reached branch");
                    x = picURL;
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference q = storageRef.child("users").child(uid);
//                    tv.setText(dataSnapshot.child("name").getValue().toString());

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
        wink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())) {

                }
            }
        });
    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        DatabaseReference geoRef = FirebaseDatabase.getInstance().getReference().child("locations");
//        GeoFire geoFire = new GeoFire(geoRef);
//        geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid().toString(), new GeoLocation(0, 0), new GeoFire.CompletionListener() {
//            @Override
//            public void onComplete(String key, DatabaseError error) {
//                if (error != null) {
//                    System.err.println("There was an error saving the location disabled to GeoFire: " + error);
//                } else {
//                    System.out.println("location disabled saved on server successfully!");
//                }
//            }
//        });
//    }
}
