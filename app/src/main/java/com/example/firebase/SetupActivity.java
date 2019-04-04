package com.example.firebase;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.os.Build;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Set;


public class SetupActivity extends AppCompatActivity {
     private ImageView img;
     private EditText name;
     private Button apply;
     private FirebaseUser user;
     public static String user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        getSupportActionBar().setTitle("Account Settings");// put the name account setting on the toolbar
        name = (EditText) findViewById(R.id.set_name);
        apply= (Button) findViewById(R.id.apply_btn);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals(""))
                Toast.makeText(SetupActivity.this, "The name field is empty", Toast.LENGTH_LONG).show();
                else
                {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name.getText().toString())
                            .build();
                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user_name=user.getDisplayName();
                                        Toast.makeText(SetupActivity.this, "Name updated successfully", Toast.LENGTH_LONG).show();
                                        Intent intent =new Intent(SetupActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                    MainActivity.arrayAdapter.notifyDataSetChanged();
                }
            }
        });

        /*img=(ImageView) findViewById(R.id.setup_image);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Here im going to ask user for permission to access external storage. Read external storage permission and write is been added to manifests.
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){ //the permission is only required for versions above Marshmellow
                    if(ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(SetupActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1); //asks for permission
                    }
                    else{
                        Toast.makeText(SetupActivity.this,"You already have permission",Toast.LENGTH_LONG).show();
                        //here ill add a function to add image for the user later
                    }
                }
            }
        });*/
    }
}
