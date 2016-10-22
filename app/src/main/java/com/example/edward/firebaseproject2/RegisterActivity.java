package com.example.edward.firebaseproject2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    EditText etEmail;
    EditText etPassword1;
    EditText etPassword2;
    EditText etName;
    Button bRegister;
    Spinner sGender;
    Spinner sRelationship;
    Spinner sSexuality;
    private Firebase mRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    RegisterActivity.this.startActivity(intent);
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword1 = (EditText)findViewById(R.id.etPassword1);
        etPassword2 = (EditText)findViewById(R.id.etPassword2);
        etName = (EditText)findViewById(R.id.etName);
        bRegister = (Button)findViewById(R.id.bRegister);
        mAuth.addAuthStateListener(mAuthListener);
        mRef = new Firebase("https://tutorial2-d6f2e.firebaseio.com/");
        sGender = (Spinner)findViewById(R.id.sGender);
        sRelationship = (Spinner)findViewById(R.id.sRelationship);
        sSexuality = (Spinner)findViewById(R.id.sSexuality);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
//// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sGender.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.relationship_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sRelationship.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.sexuality_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sSexuality.setAdapter(adapter);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = etEmail.getText().toString();
                final String pass1 = etPassword1.getText().toString();
                final String pass2 = etPassword2.getText().toString();
                final String name = etName.getText().toString();
                if(pass1.equals(pass2)) {
                    mAuth.createUserWithEmailAndPassword(email, pass1)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        sendRegistrationError("Registration failed.");
                                    }
                                    else {
                                        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                                        user User = new user(user1.getUid(), email, sRelationship.getSelectedItem().toString(), sGender.getSelectedItem().toString(),
                                                sSexuality.getSelectedItem().toString(), name);
                                        mRef.child("users").child(user1.getUid()).setValue(User);
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        RegisterActivity.this.startActivity(intent);
                                    }
                                }
                            });
                }
                else {
                    sendRegistrationError("Passwords do not match.");
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void sendRegistrationError(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setMessage("Registration Failed: " + error)
                .setNegativeButton("Retry", null)
                .create()
                .show();
    }
}
