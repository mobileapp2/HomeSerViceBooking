package com.designtech9studio.puntersapp.Activity;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.designtech9studio.puntersapp.R;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class Profile extends AppCompatActivity {

    TextView aboutTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }




    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}
