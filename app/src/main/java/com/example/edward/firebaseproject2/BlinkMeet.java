package com.example.edward.firebaseproject2;

import com.firebase.client.Firebase;

/**
 * Created by Edward on 10/11/2016.
 */
//need this for android manifest do not delete or risk being cursed by the compiler gods!
public class BlinkMeet extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
