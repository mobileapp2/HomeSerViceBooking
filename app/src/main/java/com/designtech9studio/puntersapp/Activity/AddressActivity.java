package com.designtech9studio.puntersapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.designtech9studio.puntersapp.Adapter.MainPageAdapter;
import com.designtech9studio.puntersapp.Adapter.MainPageAdapterLower;
import com.designtech9studio.puntersapp.Helpers.CoinsComparator;
import com.designtech9studio.puntersapp.Helpers.Constant;
import com.designtech9studio.puntersapp.Helpers.DataBaseHelper;
import com.designtech9studio.puntersapp.Helpers.DistanceComparator;
import com.designtech9studio.puntersapp.Helpers.DistanceHelper;
import com.designtech9studio.puntersapp.Helpers.IntentsTags;
import com.designtech9studio.puntersapp.Helpers.LocalDatabaseHelper;
import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.Helpers.VendorDatabaseHelper;
import com.designtech9studio.puntersapp.Model.AddressModel;
import com.designtech9studio.puntersapp.Model.CatSubChildModel;
import com.designtech9studio.puntersapp.Model.ProfileModel;
import com.designtech9studio.puntersapp.Model.VendorProfileModel;
import com.designtech9studio.puntersapp.R;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AddressActivity extends AppCompatActivity {

    Toolbar toolbar;

    EditText street1_ed, street2_ed, landmark_ed, city_ed, state_ed, country_ed;
    TextView confirmButton;
    AddressModel addressModel;
    SharedPreferenceHelper sharedPreferenceHelper;
    DataBaseHelper dataBaseHelper;
    double userLat, userLot;
    String msg = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Address");
        setContentView(R.layout.activity_address);

        street1_ed = findViewById(R.id.addressStreet1);
        street2_ed = findViewById(R.id.addressStreet2);
        landmark_ed = findViewById(R.id.addresslandmark);
        city_ed = findViewById(R.id.addressCity);
        state_ed = findViewById(R.id.addressState);
        country_ed = findViewById(R.id.addressCountry);
        state_ed = findViewById(R.id.addressState);

        confirmButton = findViewById(R.id.addressConfirmButton);
        toolbar = (Toolbar) findViewById(R.id.addressToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
        addressModel = sharedPreferenceHelper.getAddress();
        dataBaseHelper = new DataBaseHelper();

        street1_ed.setText(addressModel.getStreetAddress1());
        street2_ed.setText(addressModel.getStreetAddress2());
        landmark_ed.setText("");
        city_ed.setText(addressModel.getCityName());
        state_ed.setText(addressModel.getStateName());
        country_ed.setText(addressModel.getCountryName());

        if (addressModel.getLat().equals(""))userLat=0.0;
        else userLat =Double.parseDouble(addressModel.getLat());

        if (addressModel.getLot().equals(""))userLot = 0.0;
        else userLot = Double.parseDouble(addressModel.getLot());

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String street1 = street1_ed.getText().toString();
                String street2 = street2_ed.getText().toString();
                String landmark = landmark_ed.getText().toString();
                String city = city_ed.getText().toString();
                String state = state_ed.getText().toString();
                String country = country_ed.getText().toString();

                if (street1.equals("") || street2.equals("") || landmark.equals("") || city.equals("") || state.equals("") || country.equals("")) {
                    Toast.makeText(AddressActivity.this, "Please! fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                /*get all vendor in users city*/


                addressModel.setStreetAddress1(street1);
                addressModel.setStreetAddress2(street2);
                addressModel.setLandmark(landmark);
                addressModel.setCityName(city);
                addressModel.setStateName(state);
                addressModel.setCountryName(country);
                addressModel.setLat(String.valueOf(userLat));
                addressModel.setLot(String.valueOf(userLot));

                System.out.println("Send Data to database");
                System.out.println(addressModel.toString());


                SyncData syncData = new SyncData();
                syncData.execute();

                //confirmUserBooking();


            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Log.i("BACK", "Detected");
            }
        });

    }


    private class SyncData extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){
            progress = new ProgressDialog(AddressActivity.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Updating");
            progress.setMessage("Please Wait");
            progress.setIndeterminate(true);
            progress.show();

        }

        protected String doInBackground(String... strings){
            try{

                dataBaseHelper.setAddress(addressModel, sharedPreferenceHelper.getUserid());
                sharedPreferenceHelper.setAddress(addressModel);
                Log.i("Database", "updated");

                /*VendorDatabaseHelper vendorDatabaseHelper = new VendorDatabaseHelper();

                //vendorDatabaseHelper.deleteVendor();

                ArrayList<VendorProfileModel> vendorProfileModelArrayList = vendorDatabaseHelper.getVendorDetailsForBooking(addressModel.getCityName());

                if (vendorProfileModelArrayList.size() == 0 ){
                    //Toast.makeText(AddressActivity.this, , Toast.LENGTH_SHORT).show();
                    msg = "Sorry! No vendor available near by";

                }else {
                    *//*some vendor are there*//*
                    System.out.println("Some vendor are present near by "+ vendorProfileModelArrayList.size());
                    DistanceHelper distanceHelper = new DistanceHelper();

                    vendorProfileModelArrayList  = distanceHelper.getUserVendorDistance(vendorProfileModelArrayList, userLat, userLot);

                    Collections.sort(vendorProfileModelArrayList, new DistanceComparator());

                    ArrayList<VendorProfileModel> distanceFillteredVendors = new ArrayList<>();

                    for (VendorProfileModel model: vendorProfileModelArrayList) {
                        if (model.getDistanceFromCustomer() <= Constant.VENDOR_RADIUS) {
                            distanceFillteredVendors.add(model);

                        }
                        System.out.println(model.getDistanceFromCustomer());
                    }

                    if (distanceFillteredVendors.size() == 0) {
                        msg = "Sorry! No vendor available near by";
                        //Toast.makeText(AddressActivity.this, "Sorry! No vendor available near by", Toast.LENGTH_SHORT).show();

                    }else{
                        *//*Arranging vendors according to decending coins from vendor*//*

                        for (VendorProfileModel vendorProfileModel: distanceFillteredVendors) {
                            if (vendorProfileModel.getCoins() >= Constant.VENDOR_MIN_COINS) {
                                selectedVendor = vendorProfileModel;
                                break;
                            }
                        }

                        if (selectedVendor == null) {
                            msg = "Sorry! No vendor available near by";
                            //Toast.makeText(AddressActivity.this, "Sorry! No vendor available near by", Toast.LENGTH_SHORT).show();
                        }else{
                            System.out.println("Selected vendorId: " + selectedVendor.getVendorId());
                            *//*vendor selected*//*
                            dataBaseHelper.setAddress(addressModel, sharedPreferenceHelper.getUserid());
                            sharedPreferenceHelper.setAddress(addressModel);
                            Log.i("Date", "Distributed");

                        }


                    }*/


                //}

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
            Intent intent = new Intent(getApplicationContext(), MySchedule.class);
            intent.putExtra(IntentsTags.VENDOR_ID, "0");
            startActivity(intent);
            /*try {
                if (selectedVendor!=null){
                    Intent intent = new Intent(getApplicationContext(), MySchedule.class);
                    intent.putExtra(IntentsTags.VENDOR_ID, selectedVendor.getVendorId());
                    startActivity(intent);
                }else{
                    Toast.makeText(AddressActivity.this, "Sorry! No vendor near by", Toast.LENGTH_SHORT).show();
                }


            }catch (Exception ex){
                System.out.println("Exception MainPage "+ex.getMessage());
            }*/
        }
    }


    /*CheckOutAddtoCart	pkChkOut
CheckOutAddtoCart	UserId
CheckOutAddtoCart	EmailId
CheckOutAddtoCart	MobileNo
CheckOutAddtoCart	FirstName
CheckOutAddtoCart	LastName
CheckOutAddtoCart	StreetAddress
CheckOutAddtoCart	GrossAmount
CheckOutAddtoCart	TotalAmount
CheckOutAddtoCart	TaxAmount
CheckOutAddtoCart	Cancel
CheckOutAddtoCart	CanCelReason
CheckOutAddtoCart	CancelDate
CheckOutAddtoCart	CreatedBy
CheckOutAddtoCart	CreatedDate
CheckOutAddtoCart	InvoiceNo
CheckOutAddtoCart	Year_Session
CheckOutAddtoCart	UniqueNo
*/

    /*private void confirmUserBooking() {

        String vendorId = sharedPreferenceHelper.getUserid();
        ProfileModel profileModel = dataBaseHelper.getProfileData(vendorId);
        AddressModel addressModel = sharedPreferenceHelper.getAddress();
        String streetAddress = addressModel.toString();
        LocalDatabaseHelper localDatabaseHelper = new LocalDatabaseHelper(getApplicationContext());
        int grossTotal = localDatabaseHelper.getGrandTotal();
        //int totalAmount = grossTotal;
        //int taxAmount = 0;
        *//*Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
*//*
        List<CatSubChildModel> cartData =  localDatabaseHelper.fetchCartData();
        dataBaseHelper.setBookingTable(vendorId, cartData, grossTotal);
        Log.i("Insertion", "Copleted");
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
