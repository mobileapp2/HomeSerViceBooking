package com.designtech9studio.puntersapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.LinearLayout;

import com.designtech9studio.puntersapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    LinearLayout profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "We are Offline Presently", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        profile = header.findViewById(R.id.profile);
        profile.setOnClickListener(this);

        CardView card1 = (CardView) findViewById(R.id.card1);
        CardView card4 = (CardView) findViewById(R.id.card4);
        CardView card5 = (CardView) findViewById(R.id.card5);
        CardView card6 = (CardView) findViewById(R.id.card6);
        CardView card11 = (CardView) findViewById(R.id.card11);
        CardView salon_card = (CardView) findViewById(R.id.salon_card);
        CardView service_card = (CardView) findViewById(R.id.service_card);
        CardView clean_card = (CardView) findViewById(R.id.clean_card);
        CardView men_card = (CardView) findViewById(R.id.men_card);
        CardView paint_card = (CardView) findViewById(R.id.paint_card);
        CardView makeup_card = (CardView) findViewById(R.id.makeup_card);
        CardView teach_card = (CardView) findViewById(R.id.teach_card);
        CardView pest_card = (CardView) findViewById(R.id.pest_card);


        card1.setOnClickListener(this);
        card4.setOnClickListener(this);
        card5.setOnClickListener(this);
        card6.setOnClickListener(this);
        card11.setOnClickListener(this);
        salon_card.setOnClickListener(this);
        service_card.setOnClickListener(this);
        clean_card.setOnClickListener(this);
        men_card.setOnClickListener(this);
        paint_card.setOnClickListener(this);
        makeup_card.setOnClickListener(this);
        teach_card.setOnClickListener(this);
        pest_card.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_appointments) {
            Intent intent = new Intent(getApplicationContext(), MyAppointments.class);
            startActivity(intent);
        } else if (id == R.id.nav_punters) {
            Intent intent = new Intent(getApplicationContext(), PuntersWallet.class);
            startActivity(intent);

        } else if (id == R.id.nav_gift) {
            Intent intent = new Intent(getApplicationContext(), GiftVouchers.class);
            startActivity(intent);
        } else if (id == R.id.nav_refer) {
            Intent intent = new Intent(getApplicationContext(), ReferAFriend.class);
            startActivity(intent);
        } else if (id == R.id.nav_privacy) {
            Intent intent = new Intent(getApplicationContext(), PrivacyPolicy.class);
            startActivity(intent);

        } else if (id == R.id.nav_faq) {
            Intent intent = new Intent(getApplicationContext(), FAQS.class);
            startActivity(intent);
        } else if (id == R.id.nav_call) {
            Intent intent = new Intent(getApplicationContext(), CallUs.class);
            startActivity(intent);
        } else if (id == R.id.nav_login) {
           finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        Intent i;
        if (view.getId() == R.id.profile) {
            Intent intent = new Intent(getApplicationContext(), Profile.class);
            startActivity(intent);
        }
        switch (view.getId()){
            case R.id.card1 : i = new Intent(this, AllServices.class); startActivity(i); break;
            case R.id.card4 : i = new Intent(this, AllServices.class); startActivity(i); break;
            case R.id.card5 : i = new Intent(this, AllServices.class); startActivity(i); break;
            case R.id.card6 : i = new Intent(this, AllServices.class); startActivity(i); break;
            case R.id.card11 : i = new Intent(this, AllServices.class); startActivity(i); break;
            case R.id.salon_card : i = new Intent(this, List_Activity.class); startActivity(i); break;
            case R.id.service_card : i = new Intent(this, Service_List_Activity.class); startActivity(i); break;
            case R.id.men_card : i = new Intent(this, AllServices.class); startActivity(i); break;
            case R.id.makeup_card : i = new Intent(this, AllServices.class); startActivity(i); break;
            case R.id.clean_card : i = new Intent(this, Cleaning_List_Activity.class); startActivity(i); break;
            case R.id.paint_card : i = new Intent(this, AllServices.class); startActivity(i); break;
            case R.id.teach_card : i = new Intent(this, AllServices.class); startActivity(i); break;
            case R.id.pest_card : i = new Intent(this, AllServices.class); startActivity(i); break;

            default:break;
        }
    }
}

