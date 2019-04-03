package com.example.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ReadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        TextView txt= (TextView) findViewById(R.id.content_disp);
        int Id= getIntent().getIntExtra("ID",-1);
        txt.setText(MainActivity.content.get(Id));
    }
}
