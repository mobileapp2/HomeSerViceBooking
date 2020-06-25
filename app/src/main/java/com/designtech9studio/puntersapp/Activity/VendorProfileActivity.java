package com.designtech9studio.puntersapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.designtech9studio.puntersapp.Fragment.Appliance_Repair;
import com.designtech9studio.puntersapp.Fragment.HomeDesign;
import com.designtech9studio.puntersapp.Fragment.HouseCleaning;
import com.designtech9studio.puntersapp.Fragment.Other_Services;
import com.designtech9studio.puntersapp.Fragment.Salon_At_Home;
import com.designtech9studio.puntersapp.Fragment.Shifting_Home;
import com.designtech9studio.puntersapp.Fragment.Spa_At_Home;
import com.designtech9studio.puntersapp.Fragment.Tution_Classes;
import com.designtech9studio.puntersapp.Fragment.VendorAboutMeFragment;
import com.designtech9studio.puntersapp.Fragment.VendorBankDetailFragment;
import com.designtech9studio.puntersapp.Fragment.VendorIdentityFragment;
import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.Helpers.VendorDatabaseHelper;
import com.designtech9studio.puntersapp.Model.VendorProfileModel;
import com.designtech9studio.puntersapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class VendorProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    VendorProfileModel vendorProfileModel = new VendorProfileModel();
    String vendorId = "0";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Log.i("BACK", "Detected");
            }
        });



        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(VendorProfileActivity.this);
        vendorId = sharedPreferenceHelper.getVendorId();
        System.out.println("vendorId : " + vendorId);

        FetchDataTask fetchDataTask = new FetchDataTask();
        fetchDataTask.execute();



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.ic_android:
                        Intent intent1 = new Intent(VendorProfileActivity.this, VendorNewLeadActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_books:
                        Intent intent2 = new Intent(VendorProfileActivity.this, VendorOngoingActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_center_focus:

                        break;

                    case R.id.ic_backup:
                        Intent intent4 = new Intent(VendorProfileActivity.this, VendorMenuActivity.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });



    }
    private void setupViewPager(ViewPager viewPager) {
        VendorProfileActivity.ViewPagerAdapter adapter = new VendorProfileActivity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new VendorIdentityFragment(vendorProfileModel.getGst(), vendorProfileModel.getPan(), vendorProfileModel.getAadhar(), vendorId), "Identity Verification");
        adapter.addFrag(new VendorAboutMeFragment(vendorProfileModel.getBusinessName(), vendorProfileModel.getPhone(), vendorProfileModel.getWebsiteLink(), vendorProfileModel.getIntroduction(), vendorId), "About Me");
        adapter.addFrag(new VendorBankDetailFragment(vendorProfileModel.getBankName(), vendorProfileModel.getBankAccountNumber(), vendorProfileModel.getIfscCode(), vendorId, getApplicationContext()), "Bank Details");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    private class FetchDataTask extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){
            progress = new ProgressDialog(VendorProfileActivity.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait");
            progress.setIndeterminate(true);
            progress.show();

        }

        protected String doInBackground(String... strings){
            try{

                VendorDatabaseHelper databaseHelper = new VendorDatabaseHelper();


                /*FetchVendorData*/
                vendorProfileModel = databaseHelper.getVendorProfile(Integer.valueOf(vendorId));
                System.out.println("Vendore profile fetch complete");

            }catch (Exception e){
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();

            }
            return "";
        }
        protected void onPostExecute(String msg){

            progress.dismiss();
            try {
                /*Set up View Pager here*/
                setupViewPager(viewPager);
            }catch (Exception ex){
                System.out.println("Exception MainPage "+ex.getMessage());
            }
        }
    }
}
