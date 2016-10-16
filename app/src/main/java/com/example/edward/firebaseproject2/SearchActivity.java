package com.example.edward.firebaseproject2;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SearchActivity extends AppCompatActivity {
    private ListView lvUsers;
    private FirebaseUser user1;
    private Firebase mRootRef;
    private Firebase userRef;
    private StorageReference storageRef;
    private Firebase storage;
    private StorageReference picRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        lvUsers = (ListView)findViewById(R.id.lvUsers);
        user1 = FirebaseAuth.getInstance().getCurrentUser();
        mRootRef = new Firebase("https://tutorial2-d6f2e.firebaseio.com/");
        userRef = mRootRef.child("users");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://tutorial2-d6f2e.appspot.com");
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println(user1.getUid());
        //nearme stores the uid of each uesr nearby
        FirebaseListAdapter<String> adapter = new FirebaseListAdapter<String>(this, String.class, android.R.layout.activity_list_item,userRef.child(user1.getUid()).child("nearMe")) {
            @Override
            protected void populateView(View view, String s, int i) {
                //s is the nearby user uid
                final TextView textView = (TextView) view.findViewById(android.R.id.text1);
                final ImageView imageView = (ImageView) view.findViewById(android.R.id.icon);
                userRef.child(s).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        textView.setText(dataSnapshot.child("email").getValue().toString());
//                        picRef = storageRef.child("users/"+file.getLastPathSegment());
                        storageRef.child("users/1176409_382546168549198_612626050_n.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                imageView.setImageURI(uri);
                                System.out.println("URI successfully downloaded");
                                Log.d("URISTATUS","URI SUCCESFULLY Downloaded");
//                                imageView.setImageIcon(uri);
                            }
                        });

//                        Glide.with
                        //Uri file = Uri.fromFile(new File(path));
//                        imageView.setImageURI(file);
//                        imageView.setImageURI(null);
//
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
//                textView.setText(s);
            }
        };
        lvUsers.setAdapter(adapter);
        lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String userSelected = (String)adapterView.getItemAtPosition(i);
            }
        });

    }
}
