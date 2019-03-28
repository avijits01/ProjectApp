package com.example.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.Manifest;
import android.support.v4.app.ActivityCompat;
//this activity will run if the user logins and has not set up his account
public class LoginActivity extends AppCompatActivity {

    private EditText loginEmailText;
    private EditText loginPassText;
    private Button loginBtn;
    private Button loginRegBtn;
    private FirebaseAuth mAuth;
    private ProgressBar loginProgress;
    private Button forgetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance(); //gets the current object that handles the user
        loginEmailText = (EditText) findViewById(R.id.userName);
        loginPassText = (EditText) findViewById(R.id.Password);
        loginBtn = (Button) findViewById(R.id.login);
        loginRegBtn = (Button) findViewById(R.id.signuUp);
        loginProgress = findViewById(R.id.login_progress);
        forgetPass = (Button) findViewById(R.id.forgot_pass);
       /* loginRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regIntent);

            }
        });*/
        loginRegBtn.setOnClickListener(new View.OnClickListener() { //sends to register activity when clicked
            @Override
            public void onClick(View v) {

                Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regIntent);

            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() { //when the login button is clicked
                                          @Override
                                          public void onClick(View v) {

                                              String loginEmail = loginEmailText.getText().toString();
                                              FirebaseAuth auth = FirebaseAuth.getInstance();
                                              if (!TextUtils.isEmpty(loginEmail)) { //if the text field is not empty
                                                  mAuth.sendPasswordResetEmail(loginEmail)
                                                          .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                              @Override
                                                              public void onComplete(@NonNull Task<Void> task) {
                                                                  if (task.isSuccessful()) {
                                                                      Toast.makeText(LoginActivity.this, "Reset Email Sent", Toast.LENGTH_LONG).show();

                                                                  }
                                                                  else{
                                                                      Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_LONG).show();
                                                                  }
                                                              }
                                                          });
                                              }
                                              else{
                                                  Toast.makeText(LoginActivity.this, "Blank", Toast.LENGTH_LONG).show();
                                              }
                                          }
                                      }
        );


        loginBtn.setOnClickListener(new View.OnClickListener() { //when the login button is clicked
            @Override
            public void onClick(View v) {

                String loginEmail = loginEmailText.getText().toString();
                String loginPass = loginPassText.getText().toString();

                if (!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPass)) { //if the text field is not empty

                    loginProgress.setVisibility(View.VISIBLE); //progress bar is shown

                    mAuth.signInWithEmailAndPassword(loginEmail, loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                checkIfEmailVerified();


                            } else {

                                String errorMessage = task.getException().getMessage(); //error is generated from firebase. Example invlid Email, pass elc
                                Toast.makeText(LoginActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();


                            }
                            //till here the authentication process is done.
                            loginProgress.setVisibility(View.INVISIBLE); //progress bar disappear

                        }

                    });
                }
            }
        });

    }
        private void sendToMain () {

            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
        private void checkIfEmailVerified ()
        {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user.isEmailVerified()) {
                // user is verified, so you can finish this activity or send user to activity which you want.
                sendToMain(); //send the user to main Activity
                Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
            } else {
                // email is not verified, so just prompt the message to the user and restart this activity.
                // NOTE: don't forget to log out the user.
                Toast.makeText(LoginActivity.this, "Please verify your email first", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(getIntent());
                //restart this activity

            }
        }

        @Override
        protected void onStart () { //function to go to main activity
            super.onStart();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) { //if the user is already logged in send him to main activity
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }
    }



