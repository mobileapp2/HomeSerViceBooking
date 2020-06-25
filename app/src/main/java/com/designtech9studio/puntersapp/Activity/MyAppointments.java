package com.designtech9studio.puntersapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.designtech9studio.puntersapp.Adapter.MainPageAdapter;
import com.designtech9studio.puntersapp.Adapter.MainPageAdapterLower;
import com.designtech9studio.puntersapp.Fragment.HistoryFragment;
import com.designtech9studio.puntersapp.Fragment.OngoingFragment;
import com.designtech9studio.puntersapp.Helpers.DataBaseHelper;
import com.designtech9studio.puntersapp.Helpers.DateGenerator;
import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.Model.BookingModel;
import com.designtech9studio.puntersapp.R;
import com.designtech9studio.puntersapp.View.CartActivity;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

public class MyAppointments extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    List<BookingModel> onGoing_list = new ArrayList<>();
    List<BookingModel> completed_list = new ArrayList<>();
    List<BookingModel> bookingModels;

    SharedPreferenceHelper sharedPreferences;
    String userId= "";

    Button book_service_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        book_service_btn= findViewById(R.id.book_service);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        sharedPreferences = new SharedPreferenceHelper(getApplicationContext());
        userId = sharedPreferences.getUserid();

        if (userId.equals("")) {
            Toast.makeText(this, "Please! Login first", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), ClientHomepageActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        book_service_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AllServices.class);
                startActivity(intent);
            }
        });
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
                        Intent intent1 = new Intent(MyAppointments.this, ClientHomepageActivity.class);
                        startActivity(intent1);
                        break;


                    case R.id.ic_books:

                        break;

                    case R.id.ic_center_focus:
                        Intent intent3 = new Intent(MyAppointments.this, ClientProfileActivity .class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_backup:
                        Intent intent4 = new Intent(MyAppointments.this, CartActivity.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });

        SyncData syncData = new SyncData();
        syncData.execute();
    }

    private class SyncData extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){
            progress = new ProgressDialog(MyAppointments.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait..");
            progress.setIndeterminate(true);
            progress.show();
        }

        protected String doInBackground(String... strings){
            try{
                DataBaseHelper dataBaseHelper = new DataBaseHelper();

                //dataBaseHelper.alterBooking2();

                //bookingModels = new ArrayList<>();
                bookingModels = dataBaseHelper.getBookingDetails(userId);
                Log.i("Getting Data", "SucessFuly rerived data size: " +bookingModels.size());

                for (BookingModel bookingModel: bookingModels) {
                    Date currentDate = Calendar.getInstance().getTime();
                    Date bookingDate = DateGenerator.convertStringToDate(bookingModel.getDate());

                    /*if (currentDate.compareTo(bookingDate)<=0) {
                        Log.i("BookingDate", "Ongoing");
                        onGoing_list.add(bookingModel);

                       *//* Log.i("BookingDate", "Completed");
                        completed_list.add(bookingModel);*//*
                    }else{
                        Log.i("BookingDate", "Completed");
                        completed_list.add(bookingModel);
                        *//*Log.i("BookingDate", "Ongoing");
                        onGoing_list.add(bookingModel);*//*
                    }*/

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



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new OngoingFragment(onGoing_list, getApplicationContext()), "ONGOING");
        adapter.addFrag(new OngoingFragment(completed_list, getApplicationContext()), "History");

        //adapter.addFrag(new HistoryFragment(), "HISTORY");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
