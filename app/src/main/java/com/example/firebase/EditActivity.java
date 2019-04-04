package com.example.firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.firebase.MainActivity.arrayAdapter;
import static com.example.firebase.MainActivity.names;
import static com.example.firebase.MainActivity.title;

public class EditActivity extends AppCompatActivity {
    Button save;
    EditText t;
    EditText content;
    public static int i=0; // was not working without static see why?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        t = findViewById(R.id.title_blog);
        content=findViewById(R.id.content_blog);
        save=findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t.getText().toString().equals("")||content.getText().toString().equals(""))
                    Toast.makeText(EditActivity.this, "Enter title and content", Toast.LENGTH_LONG).show();
                else  {
                    MainActivity.title.add(t.getText().toString());
                    MainActivity.content.add(content.getText().toString());

                    try { //if no notes are added the exception of index out of bound is handled
                        MainActivity.names.add(user.getDisplayName());
                        MainActivity.notes.add(MainActivity.title.get(i) + "\n -" + MainActivity.names.get(i));
                        i=i+1;
                        String s=Integer.toString(i);
                        Log.i("my tag",s);
                    }
                    catch (Exception e)
                    {
                        Log.i("My tag 2","exception");
                    }
                    arrayAdapter.notifyDataSetChanged();
                    mainIntent();
                }
            }
        });
    }
    private void mainIntent()
    {
        Intent intent=new Intent(EditActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
