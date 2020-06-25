package com.designtech9studio.puntersapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import com.designtech9studio.puntersapp.NewLeadFragment;
import com.designtech9studio.puntersapp.OnGoingFragment;
import com.designtech9studio.puntersapp.R;
import com.designtech9studio.puntersapp.VendorMenuFragment;
import com.designtech9studio.puntersapp.VendorProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Vendor_Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        changeFragment(new NewLeadFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_lead:
                    changeFragment(new NewLeadFragment());
                    return true;
                case R.id.navigation_ongoing:
                    changeFragment(new OnGoingFragment());
                    return true;
                case R.id.navigation_profile:
                    changeFragment(new VendorProfileFragment());
                    return true;
                case R.id.navigation_menu:
                    changeFragment(new VendorMenuFragment());
                    return true;
            }
            return false;
        }

    };

    private void changeFragment(Fragment fm){
        androidx.fragment.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, fm);
        ft.setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }
}

