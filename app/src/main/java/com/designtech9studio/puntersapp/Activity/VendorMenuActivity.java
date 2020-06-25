package com.designtech9studio.puntersapp.Activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.sql.SQLOutput;

public class VendorMenuActivity extends AppCompatActivity {
    private Toolbar toolbar;
    TextView vendorLogOut;
    private static final int REQUEST_PHONE_CALL = 101;

    TextView callTv, termsTv, privacyTv, rateTv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        callTv = findViewById(R.id.call);
        termsTv = findViewById(R.id.terms);
        privacyTv = findViewById(R.id.privacy);
        rateTv = findViewById(R.id.rate);

        vendorLogOut = findViewById(R.id.vendorLogOut);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Log.i("BACK", "Detected");
            }
        });

        rateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id="+getPackageName())));
                }
                catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                }

            }
        });
        termsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.perfectpunters.com/Terms_Condition.aspx"));
                startActivity(browserIntent);
            }
        });
        privacyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.perfectpunters.com/privacy.aspx"));
                startActivity(browserIntent);
            }
        });
        callTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:perfectpunters1@gmail.com?subject=" + "Need Help" + "&body=" + "");
                intent.setData(data);
                startActivity(intent);
                /*Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + "+917203836930"));
                System.out.println("in call us");
                if (ContextCompat.checkSelfPermission(VendorMenuActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(VendorMenuActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                }
                else
                {
                    startActivity(intent);
                }*/

            }
        });
        vendorLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(VendorMenuActivity.this);
                sharedPreferenceHelper.getUserSignOut();
                System.out.println("SignOut");
                Intent intent = new Intent(VendorMenuActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.ic_android:
                        Intent intent1 = new Intent(VendorMenuActivity.this, VendorNewLeadActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_books:
                        Intent intent2 = new Intent(VendorMenuActivity.this, VendorOngoingActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_center_focus:
                        Intent intent3 = new Intent(VendorMenuActivity.this, VendorProfileActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_backup:
                        break;
                }


                return false;
            }
        });
    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, VendorNewLeadActivity.class);
        startActivity(intent);
    }
}
