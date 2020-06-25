package com.designtech9studio.puntersapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.toolbox.HttpResponse;
import com.designtech9studio.puntersapp.Helpers.Constant;
import com.designtech9studio.puntersapp.Helpers.DataBaseHelper;
import com.designtech9studio.puntersapp.Helpers.IntentsTags;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.internal.http.StatusLine;

public class OtpActivity extends AppCompatActivity {

    EditText otpEditText;
    Button confirmButton;
    SharedPreferenceHelper sharedPreferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_new);
       otpEditText = findViewById(R.id.otpEditText);
       confirmButton = findViewById(R.id.otp_confirm_button);
       sharedPreferenceHelper = new SharedPreferenceHelper(this);

        Intent intent = getIntent();
        final String otp = intent.getStringExtra(IntentsTags.OTP);
        final String mobile = intent.getStringExtra(IntentsTags.MOBILE);
        //otpEditText.setText(""+otp);

        String requestUrl = "http://admagister.net/api/mt/SendSMS?user=PERPUN2020&password=PERPUN2020&senderid=PERPUN&channel=trans&DCS=0&flashsms=0&number="+ mobile + "&text=Punter App OTP is "+ otp +"&route=14";
        Log.i("otpUrl", requestUrl);

        RequestTask requestTask = new RequestTask();
        requestTask.execute(requestUrl);

        Log.i("OTP", otp);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userOtp = otpEditText.getText().toString();
                if (otp.equals(userOtp)) {
                    Log.i("OTP", "CORRECT OTP");
                    /*Check Existance of user*/
                    DataBaseHelper dataBaseHelper = new DataBaseHelper();
                    String userAndRoleId = dataBaseHelper.getUserIdAndRole(mobile);


                    Log.i("UserId", userAndRoleId+"");

                    if (userAndRoleId.equals("")) {
                        /*Divert hime to register*/
                        Log.i("User", "Not Registered");
                        /*User is a customer*/
                        Intent intent1  = new Intent(getApplicationContext(), CustomerRegistrationActivity.class);
                        intent1.putExtra(IntentsTags.MOBILE, mobile);
                        startActivity(intent1);
                    }else{
                        /*Log him in*/
                        Log.i("User", "Already Registered");
                        String userId = userAndRoleId.split(Constant.SEPARATOR)[0];
                        String userRole = userAndRoleId.split(Constant.SEPARATOR)[1];
                        sharedPreferenceHelper.getUserLogIn(userId, mobile, userRole);
                        /*Divert him to cleint home page*/
                        String roleId = userAndRoleId.split(Constant.SEPARATOR)[1];
                        if (Integer.parseInt(roleId) == Constant.CUSTOMER_ROLE) {
                            /*Divert to  client home page*/
                            Log.i("User", "Customer In");
                            Intent intent1 = new Intent(getApplicationContext(), ClientHomepageActivity.class);
                            startActivity(intent1);
                            finish();

                        }else if (Integer.parseInt(roleId) == Constant.VENDOR_ROLE) {
                            Log.i("User", "Vendor In");
                            /*divert to vendor*/

                        }else {
                            Log.i("User", "Admin In");
                        }

                    }

                   /* dataBaseHelper.getRoleDetails();
                    dataBaseHelper.getStateDetails();
                    dataBaseHelper.getCountryDetails();*/

                }else{
                    Toast.makeText(getApplicationContext(), "Invalid! try again", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
    class RequestTask extends AsyncTask<String, String, String>{

        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){

            progress = new ProgressDialog(OtpActivity.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait..");
            progress.setIndeterminate(true);
            progress.show();
        }

        @Override
        protected String doInBackground(String... uri) {
            String responseString = null;
            try {
                URL url = new URL(uri[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if(conn.getResponseCode() == HttpsURLConnection.HTTP_OK){
                    // Do normal input or output stream reading
                    Log.i("OtpRequest", "Done");
                }
                else {
                    Log.i("OtpRequest", "Failed"); // See documentation for more info on response handling
                }
            }  catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            progress.dismiss();
        }
    }
}
