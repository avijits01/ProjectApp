package com.example.firebase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.Intent;
import android.widget.Toast;
import android.view.View;

public class RegisterActivity extends AppCompatActivity {
    private EditText email_text;
    private EditText password;
    private EditText confirmPass;
    private Button register;
    private Button loginIntent;
    private ProgressBar reg_progress;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email_text = (EditText) findViewById(R.id.email_field);
        password = (EditText) findViewById(R.id.pass_field);
        confirmPass = (EditText) findViewById(R.id.confirmPass_field);
        register = (Button) findViewById(R.id.register_button);
        reg_progress = (ProgressBar) findViewById(R.id.signUp_progress);
        loginIntent = (Button) findViewById(R.id.login_intent_button);
        loginIntent.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View v){
                finish(); //this finishes the intent and the user is back to the login page
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = email_text.getText().toString();
                String pass = password.getText().toString();
                String confirm_pass = confirmPass.getText().toString();
               /* FirebaseAuth mAuthListener = new FirebaseAuth.AuthStateListener() { //if the state of user is
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            sendVerificationEmail();
                            //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            //user.sendEmailVerification();
                            boolean emailVerified = user.isEmailVerified();
                            // User is signed in
                            // NOTE: this Activity should get onpen only when the user is not signed in, otherwise
                            // the user will receive another verification email.
                        } else {
                            // User is signed out

                        }
                        // ...
                    }
                };*/

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) & !TextUtils.isEmpty(confirm_pass)) { //if text fields are empty gives exception

                    if (pass.equals(confirm_pass)) { //checks if password and confirm password is equal

                        reg_progress.setVisibility(View.VISIBLE); //progress bars starts

                        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() { //Firebase method to create a user class.
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    sendVerificationEmail();// see the function down for more details. After verification is done user is logged out.
                                    Toast.makeText(RegisterActivity.this, "Registration Succesful", Toast.LENGTH_LONG).show();
                                    // startActivity(setupIntent)
                                } else {

                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();

                                }

                                reg_progress.setVisibility(View.INVISIBLE);

                            }
                        });

                    } else {

                        Toast.makeText(RegisterActivity.this, "Confirm Password and Password Field doesn't match.", Toast.LENGTH_LONG).show();

                    }
                }


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            sendToMain(); //if the user is logged in send him to main activity

        }

    }

    private void sendToMain() {

        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();

    }
    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent


                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }

}
