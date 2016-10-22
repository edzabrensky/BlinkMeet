package com.example.edward.firebaseproject2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private ListView lvUsers;
    private FirebaseUser user1;
    private Firebase mRootRef;
    private Firebase userRef;
    private StorageReference storageRef;
    private Firebase storage;
    private StorageReference picRef;
    private String email;
    private ArrayList<String> arrList;
    private int counter = 0;
    private Bitmap bmap;
//    private TextView textView;
//    private ImageView imageView;
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
        arrList = new ArrayList<String>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println(user1.getUid());
        //nearme stores the uid of each uesr nearby
        arrList.clear();

//        FirebaseListAdapter<String> adapter = new FirebaseListAdapter<String>(this, String.class, R.layout.user_in_list,userRef.child(user1.getUid()).child("nearMe")) {
//            @Override
//            protected void populateView(View view, String s, int i) {
//                //s is the nearby user uid
//                TextView textView = (TextView) view.findViewById(R.id.textView1);
//                ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
////                final TextView textView1 = (TextView) view.findViewById(R.id.item_counter);
////                String email;
////                storageRef.child("users/1176409_382546168549198_612626050_n.jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
////                    @Override
////                    public void onSuccess(byte[] bytes) {
////                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
////                        imageView.setImageBitmap(bitmap);
////                        imageView.setVisibility(View.VISIBLE);
////                    }
////                });
////                textView.setText("pizza");
//                userRef.child(s).child("email").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.getValue() != null) {
//                            email = dataSnapshot.getValue().toString();
//                            System.out.println(email + " single event listener&");
//                            if(email != null && !arrList.contains(email)) {
//                                arrList.add(email);
//                                ++counter;
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(FirebaseError firebaseError) {
//
//                    }
//                });
//                if(s.equals("2z5bjEsDwBM38cV8O2d2kpDpYwM2")) {
//                    storageRef.child("users/1176409_382546168549198_612626050_n.jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                        @Override
//                        public void onSuccess(byte[] bytes) {
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
////                            imageView.setImageBitmap(bitmap);
////                            imageView.setVisibility(View.VISIBLE);
//                            bmap = bitmap;
//                        }
//                    });
//                }
//                else if(s.equals("F8hcmDG5bvTeXLGYb9B9YMucWVy2")) {
//                    storageRef.child("users/asdasda.png").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                        @Override
//                        public void onSuccess(byte[] bytes) {
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
////                            imageView.setImageBitmap(bitmap);
////                            imageView.setVisibility(View.VISIBLE);
//                            bmap = bitmap;
//                        }
//                    });
//                }
//                imageView.setImageBitmap(bmap);
//                imageView.setVisibility(View.VISIBLE);
////                userRef.child(s).child("email").addListenerForSingleValueEvent(new ValueEventListener() {
////                    @Override
////                    public void onDataChange(DataSnapshot dataSnapshot) {
////                        email = dataSnapshot.getValue(String.class);
////                        //                        Log.d("CANT", "event listender work.");
////                        System.out.println("listener works " + email);
////                    }
////
////                    @Override
////                    public void onCancelled(FirebaseError firebaseError) {
////                        Log.d("CANT", "event listender for search activity doesnt work.");
////                        System.out.println("Doesnt work");
////                    }
////                });
////                textView.setText("pizza");
//                if(email != null && !email.isEmpty()) {
//                    if(arrList.size() > 0) {
//                        System.out.println(email + " this is what it is going to be set to &" + counter + " " + arrList.get(counter-1));
//                        if(counter == 2) {
//                            textView.setText(arrList.get(0));
//                        }
//                        else {
//                            textView.setText(arrList.get(counter-1));
//                        }
//                    }
//                }
////                textView.setText(userRef.child(s).child("email").getKey().toString());
////                userRef.child(s).addValueEventListener(new ValueEventListener() {
////                    @Override
////                    public void onDataChange(DataSnapshot dataSnapshot) {
////                        email = dataSnapshot.child("email").getValue().toString();
////                        System.out.println(email + " this is supposed to be assigned to textView");
////                    }
////
////                    @Override
////                    public void onCancelled(FirebaseError firebaseError) {
////
////                    }
////                });
////                textView.setText(email);
//
////                userRef.child(s).addValueEventListener(new ValueEventListener() {
////                    @Override
////                    public void onDataChange(DataSnapshot dataSnapshot) {
//////                        textView.setText(dataSnapshot.child("email").getValue().toString());
////
//////                        picRef = storageRef.child("users/"+file.getLastPathSegment());
//////                        storageRef.child("users/1176409_382546168549198_612626050_n.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//////                            @Override
//////                            public void onSuccess(Uri uri) {
//////                                imageView.setImageURI(uri);
//////                                imageView.setIm();
//////                                System.out.println("URI successfully downloaded");
//////                                Log.d("URISTATUS","URI SUCCESFULLY Downloaded");
//////                                imageView.setVisibility(View.VISIBLE);
////////                                imageView.setImageIcon(uri);
//////                            }
//////                        });
////
//////                        Glide.with
////                        //Uri file = Uri.fromFile(new File(path));
//////                        imageView.setImageURI(file);
//////                        imageView.setImageURI(null);
//////
////                    }
////
////                    @Override
////                    public void onCancelled(FirebaseError firebaseError) {
////
////                    }
////                });
////                textView.setT
//// ext(s);
//            }
//        };
        FirebaseListAdapter<String> adapter = new FirebaseListAdapter<String>(this, String.class, R.layout.user_in_list,userRef.child(user1.getUid()).child("nearMe")) {
            @Override
            protected void populateView(View view, String s, int i) {
                //s is the nearby user uid
                final TextView textView = (TextView) view.findViewById(R.id.textView1);
                final ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                final TextView tvUID = (TextView)view.findViewById(R.id.uid);
                tvUID.setText(s);
                userRef.child(s).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        textView.setText(dataSnapshot.child("name").getValue().toString());
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference q = storageRef.child("users").child(tvUID.getText().toString());
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
                        //                        picRef = storageRef.child("users/"+file.getLastPathSegment());
//                        storageRef.child("users/" + tvUID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//
//                                imageView.setImageURI(uri);
//                                System.out.println("URI successfully downloaded");
//                                Log.d("URISTATUS","URI SUCCESFULLY Downloaded");
//                                //                                imageView.setImageIcon(uri);
//                            }
//                        });

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
                String uid = ((TextView)view.findViewById(R.id.uid)).getText().toString();
                Intent intent = new Intent(SearchActivity.this,ProfileActivity.class);
                intent.putExtra("uid", uid);
                SearchActivity.this.startActivity(intent);
//                String userSelected = (String)adapterView.getItemAtPosition(i);
            }
        });

    }
}
