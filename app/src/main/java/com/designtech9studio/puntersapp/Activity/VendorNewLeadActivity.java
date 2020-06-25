package com.designtech9studio.puntersapp.Activity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.designtech9studio.puntersapp.Adapter.VendorBookingAdapter;
import com.designtech9studio.puntersapp.Helpers.Constant;
import com.designtech9studio.puntersapp.Helpers.DateGenerator;
import com.designtech9studio.puntersapp.Helpers.DistanceComparator;
import com.designtech9studio.puntersapp.Helpers.DistanceHelper;
import com.designtech9studio.puntersapp.Helpers.LocalDatabaseHelper;
import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.Helpers.VendorDatabaseHelper;
import com.designtech9studio.puntersapp.Model.BookingModel;
import com.designtech9studio.puntersapp.Model.CatSubChildModel;
import com.designtech9studio.puntersapp.Model.VendorProfileModel;
import com.designtech9studio.puntersapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class VendorNewLeadActivity extends AppCompatActivity {
    private Toolbar toolbar;
    TextView vendorCoins_txt;
    String vendorId = "-1";
    int coins = 0;
    TextView headingTextView;
    Switch vendorOnlineSwitch;
    TextView vendorOnlineTxt;
    List<BookingModel> onGoing_list = new ArrayList<>();
    boolean isAvailable = false;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_new_lead);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        headingTextView = findViewById(R.id.heading);
        vendorOnlineSwitch= findViewById(R.id.switchButton);
        vendorOnlineTxt = findViewById(R.id.switchText);

        recyclerView = findViewById(R.id.newFeedBooking);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        setSupportActionBar(toolbar);
        vendorCoins_txt = findViewById(R.id.vendorCoins_txt);

        final SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
        vendorId = sharedPreferenceHelper.getVendorId();


        isAvailable = sharedPreferenceHelper.isAvailable();
        vendorOnlineSwitch.setChecked(isAvailable);

        if (isAvailable) {
            vendorOnlineTxt.setText("Online");
        }else{
            vendorOnlineTxt.setText("Offline");
        }

        Button recharge = (Button) findViewById(R.id.recharge);
        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VendorNewLeadActivity.this, CreditHistoryActivity.class));
                finish();
            }
        });
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        vendorOnlineSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                StatusUpdate vendorStatus = new StatusUpdate();

                if(isChecked){
                    vendorOnlineTxt.setText("Online");
                    sharedPreferenceHelper.setVendorStatus("1");
                    vendorStatus.execute(1+"");
                }
                else {
                    vendorOnlineTxt.setText("Offline");
                    sharedPreferenceHelper.setVendorStatus("0");
                    vendorStatus.execute(0+"");
                }
                System.out.println("Online Status Updated to " + isChecked);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.ic_android:

                        break;

                    case R.id.ic_books:
                        Intent intent2 = new Intent(VendorNewLeadActivity.this, VendorOngoingActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_center_focus:
                        Intent intent3 = new Intent(VendorNewLeadActivity.this, VendorProfileActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_backup:
                        Intent intent4 = new Intent(VendorNewLeadActivity.this, VendorMenuActivity.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });

        VendorDatabaseHelper vendorDatabaseHelper = new VendorDatabaseHelper();
        //vendorDatabaseHelper.alterCheckOutAddtoCart();
        //vendorDatabaseHelper.alterBooking();
        //vendorDatabaseHelper.purchaseCoins(Integer.valueOf(vendorId), 1000, "Online");

        CoinTask coinTask = new CoinTask();
        coinTask.execute();

        OnGoingTask onGoingTask = new OnGoingTask();
        onGoingTask.execute();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vendor_newlead_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()){
          /*  case R.id.action_credit:
                Intent search = new Intent(this,CreditHistoryActivity.class);
                startActivity(search);
                return true;*/
            case R.id.action_alert:
                Intent alert = new Intent(this,VendorAlertActivity.class);
                startActivity(alert);
                return true;
        }

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
    private class CoinTask extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){
            progress = new ProgressDialog(VendorNewLeadActivity.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait");
            progress.setIndeterminate(true);
            progress.show();
            /*progress = ProgressDialog.show(VendorNewLeadActivity.this, "Loading",
                    "Please Wait...", true);
*/
        }

        protected String doInBackground(String... strings){
            try{

                /*vendorId Cart_Amount PAYMENT_MODE*/

                VendorDatabaseHelper vendorDatabaseHelper = new VendorDatabaseHelper();
                //vendorDatabaseHelper.purchaseCoins(Integer.valueOf(vendorId), 1000, "Online");
                coins = vendorDatabaseHelper.getVendorCoins(vendorId) ;
                isAvailable= vendorDatabaseHelper.isOnline(vendorId);



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
            vendorCoins_txt.setText(coins + " credits");
            vendorOnlineSwitch.setChecked(isAvailable);

            if (isAvailable) {
                vendorOnlineTxt.setText("Online");
            }else{
                vendorOnlineTxt.setText("Offline");
            }
        }
    }


    private class OnGoingTask extends AsyncTask<String, String, String > {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){
            progress = new ProgressDialog(VendorNewLeadActivity.this,R.style.AppCompatAlertDialogStyle);
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
                List<BookingModel> bookingModels = dataBaseHelper.getBookingDetails(vendorId);
                Log.i("Getting Data", "SucessFuly rerived data size: " +bookingModels.size());

                for (BookingModel bookingModel: bookingModels) {

                    if (bookingModel.isOnGoing()) {
                        Log.i("BookingDate", "Ongoing");
                        onGoing_list.add(bookingModel);
                       /* Log.i("BookingDate", "Completed");
                        completed_list.add(bookingModel);*/
                    }

                }

                Log.i("Ongoing", onGoing_list.size()+"");
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
                VendorBookingAdapter bookingAdapter = new VendorBookingAdapter(getApplicationContext(), onGoing_list, "", false);
                recyclerView.setAdapter(bookingAdapter);
                if (onGoing_list.isEmpty()) {
                    headingTextView.setText("No " + headingTextView.getText().toString());
                }

                //setup(viewPager);
            }catch (Exception ex){
                System.out.println("Exception MainPage "+ex.getMessage());
            }
        }
    }

    private class StatusUpdate extends AsyncTask<String, String, String >
    {

        protected void onPreExecute(){

        }

        protected String doInBackground(String... strings){
            try{
                VendorDatabaseHelper dataBaseHelper = new VendorDatabaseHelper();

                dataBaseHelper.updateOnlineStatus(Integer.valueOf(strings[0]), vendorId);
            }catch (Exception e){
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));

            }
            return "";
        }
        protected void onPostExecute(String msg){
            System.out.println("Vendor Status updated");
        }
    }



}
