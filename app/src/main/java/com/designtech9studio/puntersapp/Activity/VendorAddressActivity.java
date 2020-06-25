package com.designtech9studio.puntersapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.designtech9studio.puntersapp.Helpers.DataBaseHelper;
import com.designtech9studio.puntersapp.Helpers.IntentsTags;
import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.Helpers.VendorDatabaseHelper;
import com.designtech9studio.puntersapp.Model.AddressModel;
import com.designtech9studio.puntersapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Locale;

public class VendorAddressActivity extends AppCompatActivity {

    AlertDialog alert;

    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Represents a geographical location.
     */
    private Location mLastLocation;

    EditText address_txt, city_txt, state_txt, country_txt, lat_txt, lot_txt;
    int catId, subCatId, childId;
    String uploadPic1, uploadPic2, uploadPic3, description;;
    double amount;

    Button registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_address);
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        address_txt= findViewById(R.id.txtVendorAddress);
        city_txt = findViewById(R.id.txtVendorCity);
        state_txt = findViewById(R.id.txtVendorState);
        country_txt = findViewById(R.id.txtVendorCountry);
        lat_txt = findViewById(R.id.txtVendorLat);
        lot_txt = findViewById(R.id.txtVendorLot);

        registerBtn = findViewById(R.id.vendorAddressRegister);

        Intent intent = getIntent();
        final String email = intent.getStringExtra(IntentsTags.EMAIL);
        final String mobilePhone = intent.getStringExtra(IntentsTags.MOBILE);
        final String password = intent.getStringExtra(IntentsTags.PASSWORD);
        final String username = intent.getStringExtra(IntentsTags.USERNAME);
        catId = Integer.valueOf(intent.getStringExtra(IntentsTags.CAT_ID));
        subCatId = Integer.valueOf(intent.getStringExtra(IntentsTags.SUB_CAT_ID));
        childId = Integer.valueOf(intent.getStringExtra(IntentsTags.childId));
        amount = Double.valueOf(intent.getStringExtra(IntentsTags.amount));

        uploadPic1 =intent.getStringExtra(IntentsTags.uploadPic1);
        uploadPic2 =intent.getStringExtra(IntentsTags.uploadPic2);
        uploadPic3 =intent.getStringExtra(IntentsTags.uploadPic3);
        description =intent.getStringExtra(IntentsTags.description);


        if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(VendorAddressActivity.this,  R.style.AlertDialogTheme);
            builder.setMessage("Enable Gps! to proceed")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                            //confirmUserBooking(strings[0], strings[1], strings[1]);
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(VendorAddressActivity.this);
                            getAddress();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();
                            Toast.makeText(getApplicationContext(),"Please fill you address Carefully",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            //Creating dialog box
            alert = builder.create();
            //Setting the title manually
            alert.setTitle("Gps");
            alert.show();
        }else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            getAddress();
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address = address_txt.getText().toString().trim();
                String city = city_txt.getText().toString().trim();
                String sate = state_txt.getText().toString().trim();
                String country = country_txt.getText().toString();
                String lat = lat_txt.getText().toString();
                String lot = lot_txt.getText().toString();

                if (address.equals("") || city.equals("") || sate.equals("") || country.equals("")) {
                    Toast.makeText(VendorAddressActivity.this, "Please! Enter details int all fields", Toast.LENGTH_SHORT).show();
                    return;
                }


                //username, email, password, phone, address, city, state, country, lat, log
                RegisterTask registerTask = new RegisterTask();
                registerTask.execute(username, email, password, mobilePhone, city, sate, country, lat, lot);


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Activity", "Restart");
    }
    private void getAddress() {
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            Log.w("Location", "onSuccess:null");
                            return;
                        }

                        mLastLocation = location;
                        setAddressAndCity(mLastLocation);


                        // Determine whether a Geocoder is available.
                        if (!Geocoder.isPresent()) {
                            Toast.makeText(VendorAddressActivity.this, getString(R.string.no_geocoder_available), Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Locaiton", "getLastLocation:onFailure", e);
                    }
                });
    }

    void setAddressAndCity(Location location) {

        Log.i("SetAddress", location.toString());
        if (location!= null){
            mLastLocation = location;
            LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(sydney.latitude, sydney.longitude, 1);
                if (addresses != null && addresses.size() > 0) {
                    System.out.println(addresses.toString());
                    Address address = addresses.get(0);
                    String streatFeature1 = address.getFeatureName();
                    String state = address.getAdminArea();
                    String stratefeature2 = address.getSubAdminArea();
                    String city = address.getLocality();
                    String country = address.getCountryName();
                    String lat = String.valueOf(address.getLatitude());
                    String longitude = String.valueOf(address.getLongitude());
                    String completeAddress = address.getAddressLine(0);

                    if (completeAddress!=null) {
                        stratefeature2 = "";
                        if (city!=null)
                            completeAddress =completeAddress.replace(city, "");
                        //.replace(country, "").replace(state, "").split(",")
                        if (country!=null)
                            completeAddress =completeAddress.replace(country, "");
                        if (state!=null)
                            completeAddress = completeAddress.replace(state, "");

                        String add[] = completeAddress.split(",");
                        //System.out.println("Complete Adress Seperate " + stratefeature2 + " " + temp2 + "   " +temp3);

                        for (int i=0;i<add.length;i++) {
                            System.out.println("Complete Address " + add[i]);
                            if (i==0) {
                                streatFeature1 = add[i].trim();
                            }else{
                                stratefeature2+= add[i].trim() + " ";
                            }
                        }


                        AddressModel addressModel = new AddressModel();

                        addressModel.setStreetAddress1(streatFeature1);
                        addressModel.setStreetAddress2(stratefeature2);
                        addressModel.setStateName(state);
                        addressModel.setCountryName(country);
                        addressModel.setLat(lat);
                        addressModel.setLot(longitude);
                        addressModel.setCityName(city);

                        address_txt.setText(streatFeature1 + " " +stratefeature2 );
                        city_txt.setText(city);
                        state_txt.setText(state);
                        country_txt.setText(country);

                        lat_txt.setText(lat);
                        lot_txt.setText(longitude);


                        System.out.println("Complete Address: " + completeAddress);
                        System.out.println("Complete Address: " + addressModel.toString());
                        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
                        sharedPreferenceHelper.setAddress(addressModel);
                    }





                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class RegisterTask extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){
            progress = new ProgressDialog(VendorAddressActivity.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait");
            progress.setIndeterminate(true);
            progress.show();
        }

        protected String doInBackground(String... strings){
            try{

                VendorDatabaseHelper databaseHelper = new VendorDatabaseHelper();
                int vendorId = databaseHelper.registerVendor(strings[0], strings[1], strings[2], strings[3], strings[4], strings[5], strings[6], strings[7], strings[8]);
                System.out.println("vendorId : " + vendorId);
                SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(VendorAddressActivity.this);
                sharedPreferenceHelper.setVendorId(String.valueOf(vendorId));

                VendorDatabaseHelper vendorDatabaseHelper = new VendorDatabaseHelper();
                vendorDatabaseHelper.setVendorActivity(vendorId, catId, subCatId,childId,amount, description, uploadPic1, uploadPic2, uploadPic3);
                //int catId, subCatId, childId, uploadPic1, uploadPic2, uploadPic3, description, amount;
                //vendorDatabaseHelper.printVendorWise("17");
                System.out.println("VendorWise Inserted");


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
                Intent intent = new Intent(VendorAddressActivity.this, Vendor_Details.class);
                startActivity(intent);
                finish();

            }catch (Exception ex){
                System.out.println("Exception MainPage "+ex.getMessage());
            }
        }
    }


}
