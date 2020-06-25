package com.designtech9studio.puntersapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.designtech9studio.puntersapp.Fragment.VendorHistoryFragment;
import com.designtech9studio.puntersapp.Helpers.DataBaseHelper;
import com.designtech9studio.puntersapp.Helpers.DateGenerator;
import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.Helpers.VendorDatabaseHelper;
import com.designtech9studio.puntersapp.Model.BookingModel;
import com.designtech9studio.puntersapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class VendorOngoingActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    List<BookingModel> onGoing_list = new ArrayList<>();
    List<BookingModel> completed_list = new ArrayList<>();
    List<BookingModel> bookingModels;

    SharedPreferenceHelper sharedPreferences;
    String vendorId= "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_ongoing);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        sharedPreferences = new SharedPreferenceHelper(getApplicationContext());
        vendorId = sharedPreferences.getVendorId();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Log.i("BACK", "Detected");
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.ic_android:
                        Intent intent1 = new Intent(VendorOngoingActivity.this, VendorNewLeadActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_books:

                        break;

                    case R.id.ic_center_focus:
                        Intent intent3 = new Intent(VendorOngoingActivity.this, VendorProfileActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_backup:
                        Intent intent4 = new Intent(VendorOngoingActivity.this, VendorMenuActivity.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });
        SyncData syncData = new SyncData();
        syncData.execute();
    }
    private void setupViewPager(ViewPager viewPager) {
        VendorOngoingActivity.ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new VendorOngoingFragment(onGoing_list, getApplicationContext(), "Ongoing Leads Will Appear Here", false), "ONGOING");
        adapter.addFrag(new VendorOngoingFragment(completed_list, getApplicationContext(), "History Leads Will Appear Here", true), "HISTORY");
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

    private class SyncData extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){
            progress = new ProgressDialog(VendorOngoingActivity.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait");
            progress.setIndeterminate(true);
            progress.show();
        }

        protected String doInBackground(String... strings){
            try{
                VendorDatabaseHelper dataBaseHelper = new VendorDatabaseHelper();

                //dataBaseHelper.alterBooking2();

                //bookingModels = new ArrayList<>();
                bookingModels = dataBaseHelper.getBookingDetails(vendorId);

                Log.i("Getting Data", "SucessFuly rerived data size: " +bookingModels.size());

                for (BookingModel bookingModel: bookingModels) {

                    if (bookingModel.isOnGoing()) {
                        Log.i("BookingDate", "Ongoing");
                        onGoing_list.add(bookingModel);
                       /* Log.i("BookingDate", "Completed");
                        completed_list.add(bookingModel);*/
                    }else{
                        Log.i("BookingDate", "Completed");
                        completed_list.add(bookingModel);
                        /*Log.i("BookingDate", "Ongoing");
                        onGoing_list.add(bookingModel);*/
                    }



                }

                Log.i("Ongoing", onGoing_list.size()+"");
                Log.i("History", completed_list.size() + "");

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
                setupViewPager(viewPager);
            }catch (Exception ex){
                System.out.println("Exception MainPage "+ex.getMessage());
            }
        }
    }

}
