package com.designtech9studio.puntersapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.designtech9studio.puntersapp.Helpers.DataBaseHelper;
import com.designtech9studio.puntersapp.Helpers.IntentsTags;
import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.Model.ProfileModel;
import com.designtech9studio.puntersapp.PaymentActivity;
import com.designtech9studio.puntersapp.R;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class FeedBackActivity extends AppCompatActivity {

    TextView reviewPoint_tv, reviewComments_tv, submit_tv;

    ProfileModel profileModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        setTitle("FeedBack");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

                Log.i("BACK", "Detected");
            }
        });


        ProfileData profileData = new ProfileData();
        profileData.execute();

        reviewPoint_tv = findViewById(R.id.feedbackReviewPoint);
        reviewComments_tv = findViewById(R.id.feedbackComments);
        submit_tv = findViewById(R.id.feedBackSubmit);

        submit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reviewPoint = reviewPoint_tv.getText().toString();
                String reviewComments = reviewComments_tv.getText().toString();
                String serviceId = getIntent().getStringExtra(IntentsTags.BOOKING_ID);
                String vendorId = getIntent().getStringExtra(IntentsTags.VENDOR_ID);

                if(!reviewPoint.matches("[012345]")) {
                    Toast.makeText(FeedBackActivity.this, "Review Point can be between 0 to 5", Toast.LENGTH_SHORT).show();
                    return;
                }

                SendFeedBack sendFeedBack = new SendFeedBack();
                sendFeedBack.execute(serviceId, reviewComments, reviewPoint, vendorId);


            }
        });



    }



    private class ProfileData extends AsyncTask<String, String, String > {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress = null;

        protected void onPreExecute() {


            progress = new ProgressDialog(FeedBackActivity.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait..");
            progress.setIndeterminate(true);
            progress.show();
        }

        protected String doInBackground(String... strings) {
            try {
                SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
                String userId = sharedPreferenceHelper.getUserid();
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                profileModel = dataBaseHelper.getProfileData(userId);
                //profileModel = dataBaseHelper.getProfileData(userId);

            } catch (Exception e) {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();

            }
            return "";
        }

        protected void onPostExecute(String msg) {

            if (progress != null)
                progress.dismiss();
        }
    }

    private class SendFeedBack extends AsyncTask<String, String, String > {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress = null;

        protected void onPreExecute() {
            progress = new ProgressDialog(FeedBackActivity.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("ThankYou");
            progress.setMessage("Please Wait! we will redirect you to home page");
            progress.setIndeterminate(true);
            progress.show();
        }

        protected String doInBackground(String... strings) {
            try {

                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                dataBaseHelper.setFeedBack(Integer.valueOf(strings[3]), profileModel.getName(), profileModel.getPhone(), strings[0], strings[1], strings[2]);
                Log.i("FeedBack", "registered");


            } catch (Exception e) {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();

            }
            return "";
        }

        protected void onPostExecute(String msg) {

            if (progress != null)
                progress.dismiss();

            Intent intent = new Intent(FeedBackActivity.this, ClientHomepageActivity.class);
            startActivity(intent);
            fileList();

        }
    }

}
