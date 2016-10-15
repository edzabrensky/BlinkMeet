package com.example.edward.firebaseproject2;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.firebase.client.Firebase;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.ui.FirebaseListAdapter;
import com.google.android.gms.common.internal.GetServiceRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Firebase mRootRef;
    private FirebaseAuth mAuth;
    private Button bLogout;
    private Button bSettings;
    private Button bUpload;
    private Button bSearch;
    private StorageReference storageRef;
    private boolean gpsEnabled;
    private double latitude;
    private double longitude;

    private ArrayList<String> mMessages = new ArrayList<>();

    private TextView mTextView;
    private ListView mListView;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Firebase locationRef;
    private Firebase userRef;
    private Firebase msgRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRootRef = new Firebase("https://tutorial2-d6f2e.firebaseio.com/");
        mAuth = FirebaseAuth.getInstance();
        mTextView = (TextView)findViewById(R.id.textView);
        mListView = (ListView)findViewById(R.id.listView);
        bLogout = (Button)findViewById(R.id.bLogout);
        bSettings = (Button)findViewById(R.id.bSettings);
        bUpload = (Button)findViewById(R.id.bUpload);
        bSearch = (Button)findViewById(R.id.bSearch);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://tutorial2-d6f2e.appspot.com");

        userRef = mRootRef.child("users");
        msgRef = mRootRef.child("messages");

        //checks if user is logged in
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    FirebaseAuth.getInstance().signOut();
                    MainActivity.this.startActivity(intent);
                    Log.d("STATUS/", "onAuthStateChanged:signed_out");
                }
                else {
                    Log.d("STATUS/", "onAuthStateChanged:signed_in");
                }
            }
        };



    }

    @Override
    protected void onStart() {
        super.onStart();



        final FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        mTextView.setText(user1.getUid().toString());

        //testing location
        //added firebase server dependency for database reference
        locationRef = mRootRef.child("locations");


        final DatabaseReference geoRef = FirebaseDatabase.getInstance().getReference().child("locations");

        final GeoFire geoFire = new GeoFire(geoRef);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //enabled gps after geofire completes so we get more accurate estimate of location
                gpsEnabled = false;
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                geoFire.setLocation(user1.getUid().toString(), new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            System.err.println("There was an error saving the location to GeoFire: " + error);
                        } else {
                            System.out.println("Location saved on server successfully!");
                            gpsEnabled = true;
                        }
                    }
                });
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                //wtf is a status
            }

            @Override
            public void onProviderEnabled(String s) {
                gpsEnabled = true;
                onStart();
                System.out.println("Provider enabled.");
            }
            @Override
            public void onProviderDisabled(String s) {
                gpsEnabled = false;
//                geoFire.removeLocation(user1.getUid().toString());
//                locationRef.child(user1.getUid().toString()).removeValue();
                geoFire.setLocation(user1.getUid().toString(), new GeoLocation(0, 0), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            System.err.println("There was an error saving the provider disabled error to GeoFire: " + error);
                        } else {
                            System.out.println("Provider disabled saved on server successfully!");
                        }
                    }
                });

                //dont allow them to access geoquery and remove from the geofire locations
            }
        };
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            //updates location every 5 minutes (300000 ms), and only calls the location listener if 500meters (.3miles) have passed between last point
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 500, locationListener);
        } catch(SecurityException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("GPS DOESNT WORK")
                    .setNegativeButton("Ok", null)
                    .create()
                    .show();
        }


        //end test location

        FirebaseListAdapter<String> adapter = new FirebaseListAdapter<String>(this, String.class, android.R.layout.simple_list_item_1,msgRef) {
            @Override
            protected void populateView(View view, String s, int i) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(s);
//                textView.setText("pizza");
            }
        };
        mListView.setAdapter(adapter);
        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gpsEnabled = false;
                geoFire.setLocation(user1.getUid().toString(), new GeoLocation(0, 0), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            System.err.println("There was an error saving the provider disabled location to GeoFire: " + error);
                        } else {
                            System.out.println("Provider disabled saved on server successfully!");
                        }
                    }
                });
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        bSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        bUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:find cameraroll path
                String path = android.os.Environment.DIRECTORY_DCIM;
                Uri file = Uri.fromFile(new File(path));

                StorageReference picRef = storageRef.child("images/"+file.getLastPathSegment());
                UploadTask uploadTask = picRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        System.out.print("Upload doesnt work.Doesnt work " + '\n');
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Firebase dlURL = userRef.child(user1.getUid().toString());
                        dlURL.child("picURL").setValue(downloadUrl);
                    }
                });
            }

        });
        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gpsEnabled) {
                    //searches withing .3 miles to find people around them
                    GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude, longitude), .6);
                    geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                        @Override
                        public void onKeyEntered(String key, GeoLocation location) {
                            //add item to user's nearby list
                        }

                        @Override
                        public void onKeyExited(String key) {

                        }

                        @Override
                        public void onKeyMoved(String key, GeoLocation location) {

                        }

                        @Override
                        public void onGeoQueryReady() {

                        }

                        @Override
                        public void onGeoQueryError(DatabaseError error) {

                        }
                    });
                    System.out.println("Successfully clicked search.");
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("GPS needs to be enabled to access this page.")
                            .setNegativeButton("Ok", null)
                            .create()
                            .show();
                }

            }
        });


    }

//    @Override
//    protected void onStop() {
//        super.onStop();
////        geoFire.setLocation(user1.getUid().toString(), new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
////            @Override
////            public void onComplete(String key, DatabaseError error) {
////                if (error != null) {
////                    System.err.println("There was an error saving the location to GeoFire: " + error);
////                } else {
////                    System.out.println("Location saved on server successfully!");
////                    gpsEnabled = true;
////                }
////            }
////        });
//    }
}
