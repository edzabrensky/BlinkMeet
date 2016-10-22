package com.example.edward.firebaseproject2;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
//TODO:FIX THIS screen
//static listview for settings
public class SettingsActivity extends AppCompatActivity {
    private final String[] settings = {"Settings1", "View Profile"};
    private ArrayAdapter<String> adapter;
    private ListView lvSettings;
    private FirebaseUser user1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        lvSettings = (ListView)findViewById(R.id.lvSettings);
        ArrayList<String> arrList = new ArrayList<>();
        for(int i = 0; i < settings.length; ++i) {
            arrList.add(settings[i]);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrList);

        lvSettings.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        user1 = FirebaseAuth.getInstance().getCurrentUser();
        lvSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String value = (String)adapterView.getItemAtPosition(i);
                if(value.equals("Settings1")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    builder.setMessage("Worked ")
                            .setNegativeButton("Ok", null)
                                                        .create()
                            .show();
                }
                if(value.equals("View Profile")) {
                    Intent intent = new Intent(SettingsActivity.this, ProfileActivity.class);
                    intent.putExtra("uid",user1.getUid().toString());
                    SettingsActivity.this.startActivity(intent);
                }
            }
        });
    }
}
