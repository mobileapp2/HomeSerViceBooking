package com.designtech9studio.puntersapp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.designtech9studio.puntersapp.Helpers.Constant;
import com.designtech9studio.puntersapp.Helpers.DataBaseHelper;
import com.designtech9studio.puntersapp.Helpers.DistanceComparator;
import com.designtech9studio.puntersapp.Helpers.DistanceHelper;
import com.designtech9studio.puntersapp.Helpers.IntentsTags;
import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.Helpers.VendorDatabaseHelper;
import com.designtech9studio.puntersapp.Model.AddressModel;
import com.designtech9studio.puntersapp.Model.UserModel;
import com.designtech9studio.puntersapp.Model.VendorProfileModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.designtech9studio.puntersapp.R;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CustomerRegistrationActivity extends AppCompatActivity {

    EditText userName_txt, firstName_txt, lastName_txt, password_txt, confirmPassword_txt;
    Button registerButton;
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Represents a geographical location.
     */
    private Location mLastLocation;
    String mobile;

    AddressModel addressModel;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration);

        userName_txt = findViewById(R.id.customerTxtUserName);
        firstName_txt = findViewById(R.id.customerTxtFirstName);
        lastName_txt = findViewById(R.id.customerTxtLastName);
        password_txt = findViewById(R.id.customerTxtConfirmPassword);
        confirmPassword_txt = findViewById(R.id.customerTxtConfirmPassword);
        registerButton = findViewById(R.id.customerRegister);

        Intent intent = getIntent();
        mobile= intent.getStringExtra(IntentsTags.MOBILE);
        //mobile = "1234567809";


        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        /*addressModel = new AddressModel();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        }else{
            showGPSDisabledAlertToUser();
        }*/

//        System.out.println("Hello world");
//        locationManager = (LocationManager) this.getSystemService(LocationManager.GPS_PROVIDER);

/*
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        System.out.println("LAST COCATIOn");
                        if (location != null) {
                            Log.i("last know location", location.toString());
                            setAddressAndCity(location);
                        }else{
                            Log.i("Location", "Please on GPS location");
                        }
                    }
                });

*/



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userName_txt.getText().toString();
                String firstName = firstName_txt.getText().toString();
                String lastName = lastName_txt.getText().toString();
                String password = password_txt.getText().toString();
                String confirmPassword = confirmPassword_txt.getText().toString();

                if (username.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please! Enter username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (firstName.equals("")){
                    Toast.makeText(getApplicationContext(), "Please! Enter First Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (lastName.equals("")){
                    Toast.makeText(getApplicationContext(), "Please! Enter Last Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please! Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (confirmPassword.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please! Enter confirm password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!confirmPassword.equals(password)) {
                    Toast.makeText(getApplicationContext(), "Password Mis Match", Toast.LENGTH_SHORT).show();
                    return;
                }

                userModel = new UserModel(username, password, Constant.CUSTOMER_ROLE, mobile, firstName, lastName);

                SyncData syncData = new SyncData();
                syncData.execute();




            }
        });

    }
    private class SyncData extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){

            progress = new ProgressDialog(CustomerRegistrationActivity.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait..");
            progress.setIndeterminate(true);
            progress.show();
        }

        protected String doInBackground(String... strings){
            try{

                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                dataBaseHelper.registerUser(userModel);

                String userAndRoleId = dataBaseHelper.getUserIdAndRole(mobile);
                String userId = userAndRoleId.split(Constant.SEPARATOR)[0];
                String role = userAndRoleId.split(Constant.SEPARATOR)[1];
                SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
                sharedPreferenceHelper.getUserLogIn(userId, mobile, role);
                Log.i("Login", "User Succesfull");

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
            Intent intent1 = new Intent(getApplicationContext(), ClientHomepageActivity.class);
            startActivity(intent1);
            finish();

        }
    }


}
