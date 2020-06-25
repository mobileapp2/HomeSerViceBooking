package com.designtech9studio.puntersapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.designtech9studio.puntersapp.Helpers.IntentsTags;
import com.designtech9studio.puntersapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class VendorOtpActivity extends AppCompatActivity {

    EditText vendorOtp;
    Button verifyBtn;

    String otp = "";
    Intent intent;
    int catId, subCatId, childId;
    String uploadPic1, uploadPic2, uploadPic3, description;;
    double amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_otp);
        vendorOtp = findViewById(R.id.otpEditText_vendor);
        verifyBtn = findViewById(R.id.vendorOtpVerify_btn);
        intent = getIntent();

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


        System.out.println("IntentData: " + email + " " + mobilePhone + " " + password + " " + username);
        Random random = new Random();
        for (int i=0; i<4;i++) {
            otp+=String.valueOf(random.nextInt(9));
        }

        String requestUrl = "http://admagister.net/api/mt/SendSMS?user=PERPUN2020&password=PERPUN2020&senderid=PERPUN&channel=trans&DCS=0&flashsms=0&number="+ mobilePhone + "&text=Punter App OTP is "+ otp +"&route=14";
        Log.i("otpUrl", requestUrl);

        RequestTask requestTask = new RequestTask();
        requestTask.execute(requestUrl);

        //vendorOtp.setText(otp);

        Log.i("OTP", otp);

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userOtp = vendorOtp.getText().toString().trim();
                if (userOtp.equals(otp)) {
                    //valid otp
                    Intent intent1 = new Intent(VendorOtpActivity.this, VendorAddressActivity.class);
                    intent1.putExtra(IntentsTags.EMAIL, email);
                    intent1.putExtra(IntentsTags.PASSWORD, password);
                    intent1.putExtra(IntentsTags.USERNAME, username);
                    intent1.putExtra(IntentsTags.MOBILE, mobilePhone);

                    intent1.putExtra(IntentsTags.CAT_ID, String.valueOf(catId));
                    intent1.putExtra(IntentsTags.SUB_CAT_ID, String.valueOf(subCatId));
                    intent1.putExtra(IntentsTags.amount, String.valueOf(amount));
                    intent1.putExtra(IntentsTags.description, description);
                    intent1.putExtra(IntentsTags.uploadPic1, uploadPic1);
                    intent1.putExtra(IntentsTags.uploadPic2, uploadPic2);
                    intent1.putExtra(IntentsTags.uploadPic3, uploadPic3);
                    intent1.putExtra(IntentsTags.childId, String.valueOf(childId));
                    startActivity(intent1);
                    finish();
                }
            }
        });

    }
    class RequestTask extends AsyncTask<String, String, String> {

        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){
            progress = new ProgressDialog(VendorOtpActivity.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait");
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
