package com.designtech9studio.puntersapp.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.designtech9studio.puntersapp.BuildConfig;
import com.designtech9studio.puntersapp.BusinessImplement;
import com.designtech9studio.puntersapp.ConnectionClass;
import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.Helpers.VendorDatabaseHelper;
import com.designtech9studio.puntersapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Vendor_SignIn extends AppCompatActivity {
/*
    int Mode, int vendorID, String VendorName,String FirstName,
    String LastName, int countryID, int stateId, int cityId,
    String Address, String MobileNo, String Lat, String Lot,
    String CompanyName, String GstNo, String AdharcardNo,
    int CreatedBy, int Status, String PancardNo
    */

    private static final String TAG = Vendor_SignIn.class.getSimpleName();
    Button btn_Save;
    private EditText username;
    private EditText password;
    private Button signinbutton;
    private View mProgressView;
    private com.designtech9studio.puntersapp.ConnectionClass ConnectionClass;
    BusinessImplement insertClass = new BusinessImplement();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Represents a geographical location.
     */
    private Location mLastLocation;

    /**
     * Tracks whether the user has requested an address. Becomes true when the user requests an
     * address and false when the address (or an error message) is delivered.
     */
    private boolean mAddressRequested;

    /**
     * The formatted location address.
     */
    private String mAddressOutput;

    /**
     * Receiver registered with this activity to get the response from FetchAddressIntentService.
     */
    private AddressResultReceiver mResultReceiver;

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_signin);
        ConnectionClass = new ConnectionClass();
        username = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.pass);
        Button signinbutton = (Button) findViewById(R.id.button_signin);
        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

       // VendorDatabaseHelper vendorDatabaseHelper = new VendorDatabaseHelper();
        //vendorDatabaseHelper.alterVendor();


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        /*Ask For Gps permision*/
        if (!checkPermissions()) {
            requestPermissions();
        } /*else {
            getAddress();
        }*/
        if (mLastLocation != null) {
            startIntentService();
            return;
        }



        TextView signin = findViewById(R.id.register);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Vendor_SignIn.this, RegistrationActivity.class));
                finish();
            }
        });
    }
    private void startIntentService() {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Constants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         *  Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                Log.i("Address", "Found");
                //showToast(getString(R.string.address_found));
            }

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            mAddressRequested = false;
        }
    }
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(Vendor_SignIn.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(Vendor_SignIn.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private void showSnackbar(final String text) {
        View container = findViewById(android.R.id.content);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    private void attemptLogin() {

        String Errorlist = "";

        Log.i("Vednor", "1");
        if(username.getText().toString().trim().equals(""))
            Errorlist +="Enter User Name. \n";
        if(password.getText().toString().trim().equals(""))
            Errorlist +="Enter Password. \n";
        if(Errorlist.length() > 0)
            Toast.makeText(this,Errorlist.toString(),Toast.LENGTH_LONG).show();
        else
        {

            Log.i("Vednor", "2");
            Connection con = ConnectionClass.CONN();
            if (con == null) {
                Toast.makeText(getApplicationContext(), "Error in connection with SQL server", Toast.LENGTH_LONG).show();
            }
            else {


                Log.i("Vednor", "Coonect");
                //VendorDatabaseHelper databaseHelper = new VendorDatabaseHelper();
                //int vendorId = databaseHelper.validateVendor(username.getText().toString().trim(), password.getText().toString().trim());

                ValidateTask validateTask = new ValidateTask();
                validateTask.execute(username.getText().toString().trim(), password.getText().toString().trim());

                /*if (vendorId == 0) {
                    Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                //SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(Vendor_SignIn.this);
                //sharedPreferenceHelper.setVendorId(String.valueOf(vendorId));

                /*Intent intent = new Intent(Vendor_SignIn.this, VendorNewLeadActivity.class);
                startActivity(intent);
                finish();
*/


                /*String vendorvalidation = "{call ValidateUser(?,?)}";
                CallableStatement callableStatement = null;
                try {
                    callableStatement = con.prepareCall(vendorvalidation);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {

                    Log.i("Vednor", "Callable");

                    callableStatement.setString("@USERNAME",username.getText().toString().trim());
                    callableStatement.setString("@PASSWORD",password.getText().toString().trim());
                    ResultSet Result = callableStatement.executeQuery();
                    List<String> li = new ArrayList<>();
                    List<Map<String, String>> data = null;
                    data = new ArrayList<Map<String, String>>();




                    Log.i("Vednor", Result.toString());

                    if (!Result.next()) {
                        Toast.makeText(this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //Log.i("Vednor", Result.getString(0));


                        startActivity(new Intent(Vendor_SignIn.this, VendorNewLeadActivity.class));
                        finish();
                    }
                }
                catch(Exception ex)
                {
                    Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
                }*/
            }

        }

    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    int vendorId = 0;

    private class ValidateTask extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){
            progress = new ProgressDialog(Vendor_SignIn.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait");
            progress.setIndeterminate(true);
            progress.show();

         }

        protected String doInBackground(String... strings){
            try{

                VendorDatabaseHelper databaseHelper = new VendorDatabaseHelper();

                 vendorId = databaseHelper.validateVendor(strings[0], strings[1]);
                System.out.println("vendorId : " + vendorId);


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
                if (vendorId!=0) {
                    SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(Vendor_SignIn.this);
                    sharedPreferenceHelper.setVendorId(String.valueOf(vendorId));
                    Intent intent = new Intent(Vendor_SignIn.this, VendorNewLeadActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(Vendor_SignIn.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception ex){
                System.out.println("Exception MainPage "+ex.getMessage());
            }
        }
    }
}
