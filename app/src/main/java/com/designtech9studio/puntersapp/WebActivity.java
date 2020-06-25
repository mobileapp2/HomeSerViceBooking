package com.designtech9studio.puntersapp;

import android.os.Bundle;
import android.webkit.WebView;

import com.designtech9studio.puntersapp.Activity.ClassListItems;
import com.designtech9studio.puntersapp.Activity.ListActivity;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WebActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_webactivity);


        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("https://www.perfectpunters.com");
    }
}
