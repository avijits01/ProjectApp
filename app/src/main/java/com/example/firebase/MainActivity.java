package com.example.firebase;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

     private Toolbar mainToolbar;
     private FirebaseAuth mAuth;
     static ArrayAdapter<String> arrayAdapter;
     public static ArrayList<String> notes =new ArrayList<>(); //creates an array list to implement adapter in the list view. In simple words the elements here are the elements of menu in the list view.
     public static ArrayList<String> title =new ArrayList<>(); //creates an array to store title of blog posts
     public static ArrayList<String> content=new ArrayList<>();// creates an array to store content of blog post *sequentially*
     public static ArrayList<String> names=new ArrayList<>();
    //So here goes the logic. Every index of arrays are linked sequentially. Means 1st element of the name array is the name of the user who wrote about the topic and content of the first element of topic and content array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        Button add=(Button) findViewById(R.id.add_notes);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        getSupportActionBar().setTitle("Social Feed"); //this putts the title to the main page
        mAuth = FirebaseAuth.getInstance(); //gets the current object that handles the user
       // SharedPreferences sharedpreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPref.edit();
        //editor.putInt(getString(R.string.saved_high_score_key), newHighScore);
        //editor.commit();
        ListView listView= (ListView) findViewById(R.id.list_view);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                startActivity(intent);
                finish();
            }
        });
        arrayAdapter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,notes);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View itemView, int itemPosition, long itemId)
            {
                Intent intent = new Intent(getBaseContext(), ReadActivity.class);
                intent.putExtra("ID", itemPosition);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if(names.get(position).equals(user.getDisplayName())) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Are you sure?")
                            .setMessage("Do you want to delete your post?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    notes.remove(position);
                                    title.remove(position);
                                    content.remove(position);
                                    names.remove(position);
                                    EditActivity.i = EditActivity.i - 1;
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
                return false;
            }

            });
    }

    @Override
    protected void onStart() {//this method is called whenever the window is closed and opened again
        //also note. Any object created here can be used in any other function in this class.
        super.onStart();
        FirebaseApp.initializeApp(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();//checks if a user is logged in
        if (user == null) { //if there is no user. Login Activity starts
            sendTologIn();
        }
        else
        {
            if(user.getDisplayName()==null) //if the user is not registered registeration is done first
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

