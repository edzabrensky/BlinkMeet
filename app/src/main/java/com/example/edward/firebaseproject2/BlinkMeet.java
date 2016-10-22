package com.example.edward.firebaseproject2;

import com.firebase.client.Firebase;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Edward on 10/21/2016.
 */
//need this for android manifest do not delete or risk being cursed by the compiler gods!
public class BlinkMeet extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

    @Override
    public void onTerminate() {
//        super.onTerminate();
        DatabaseReference geoRef = FirebaseDatabase.getInstance().getReference().child("locations");
        GeoFire geoFire = new GeoFire(geoRef);
        geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid().toString(), new GeoLocation(0, 0), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location disabled to GeoFire: " + error);
                } else {
                    System.out.println("location disabled saved on server successfully!");
                }
            }
        });
        super.onTerminate();
    }

}
