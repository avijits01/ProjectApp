package com.example.firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

     private Toolbar mainToolbar;
     private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        getSupportActionBar().setTitle("Icas App"); //this putts the title to the main page
        mAuth = FirebaseAuth.getInstance(); //gets the current object that handles the user
        

    }

    @Override
    protected void onStart() {//this method is called whenever the window is closed and opened again
        //also note. Any object created here can be used in any other function in this class.
        super.onStart();
        FirebaseApp.initializeApp(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();//checks if a user is logged in
        if (user == null) {
            sendTologIn();
        }
        else
        {
            if(user.getDisplayName()==null)
            {
                Intent intent=new Intent(MainActivity.this,SetupActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){ //called when the option menu is created
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.logout_btn:
                logOut(); //this functions logs out the user from the logout button. Defined later in the code.
                return true;
            case R.id.settings_btn:
                Intent setup =new Intent(MainActivity.this,SetupActivity.class);
                startActivity(setup); //goes to user setup page
                finish();
            default:
                return false; //se the code doesnt give any error
        }
    }
    private void logOut(){
        mAuth.signOut(); //this makes sure the firebase unAuthenticate the user
        sendTologIn();
    }
    private void sendTologIn(){
        Intent LoginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(LoginIntent); //the activity is changed to login activity
        finish(); //ensures intent will be clear when the user will come back to the project
    }

}

