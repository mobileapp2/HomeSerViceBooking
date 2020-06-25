package com.designtech9studio.puntersapp.Activity;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.designtech9studio.puntersapp.Helpers.IntentsTags;
import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.Helpers.VendorDatabaseHelper;
import com.designtech9studio.puntersapp.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class Vendor_Recharge_Wallet extends AppCompatActivity {
    private Toolbar toolbar;
    TextView amount_tv;
    RadioGroup radioGroup;
    RadioButton cashRadio, onlineRadio;
    boolean cashPay = false;
    TextView paymentBtn;
    AlertDialog alert;
    int amount = 0;
    String paymentMode = "Cash";
    String vendorId = "0";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_recharge_wallet);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        amount_tv = findViewById(R.id.walletAmount);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        paymentBtn= findViewById(R.id.paymentBtn);
        cashRadio = findViewById(R.id.cashOnDeliveryRadio);
        onlineRadio = findViewById(R.id.onlinePaymentRadio);
        radioGroup = findViewById(R.id.radioGroup);

        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
        vendorId = sharedPreferenceHelper.getVendorId();

        paymentBtn.setText("Pay "+ Html.fromHtml("&#x20B9;") +" "+0+"/-");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Log.i("BACK", "Detected");
            }
        });

        cashRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashPay = true;
                amount = getAmount();
                paymentBtn.setText("Pay "+ Html.fromHtml("&#x20B9;") +" "+amount+"/-");
            }
        });

        onlineRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashPay = false;
                amount = getAmount();
                paymentBtn.setText("Pay "+ Html.fromHtml("&#x20B9;") +" "+amount+"/-");
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(Vendor_Recharge_Wallet.this, R.style.AlertDialogTheme);
        builder.setMessage("Cash payment needs admin approval")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                       /*Recharge code*/
                        paymentMode = "Cash";
                        RechargeTask rechargeTask = new RechargeTask();
                        rechargeTask.execute(vendorId, String.valueOf(amount), paymentMode);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(),"Payment Failed! Please try again..",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        //Creating dialog box
        alert = builder.create();
        //Setting the title manually
        alert.setTitle("Confirm Payment");

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadio = findViewById(selectedId);

                if (selectedRadio == null) {
                    Toast.makeText(getApplicationContext(), "Please select payment option!", Toast.LENGTH_SHORT).show();
                }else{
                    String text = selectedRadio.getText().toString();
                    if (text.equals("   Cash Payment")) {

                        amount = getAmount();
                        if (amount == 0)return;
                        // create an alert builder


                        alert.show();
                        Log.i("CashSelected", "Hello");

                        //confirmUserBooking(selectedDate, selectedTime, "Cash");
                    }else{

                        amount = getAmount();
                        if (amount == 0)return;
                        paymentMode = "Online";

                        Intent intent = new Intent(getApplicationContext(), VendorCoinOnlinePayment.class);
                        intent.putExtra(IntentsTags.VENDOR_CART_AMOUNT, String.valueOf(amount));
                        startActivity(intent);

                        /*RechargeTask rechargeTask = new RechargeTask();
                        rechargeTask.execute(vendorId, String.valueOf(amount), paymentMode);*/


                        Log.i("Online", "Payment");

                        /*SelectModeOfPayment.ProfileData profileData = new SelectModeOfPayment.ProfileData();
                        profileData.execute();*/
                    }

                }
            }
        });

    }

    private int getAmount() {
        int temp  = 0;
        if (amount_tv.getText().toString().equals("")) {
            Toast.makeText(this, "Invalid Amount", Toast.LENGTH_SHORT).show();
            amount_tv.requestFocus();
            return 0;
        }
        temp = Integer.valueOf(amount_tv.getText().toString());
        if (temp%10 != 0) {
            Toast.makeText(this, "Amount needs to be multiple of 10", Toast.LENGTH_SHORT).show();
            amount_tv.requestFocus();
            return 0;
        }
        return temp;
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, CreditHistoryActivity.class);
        startActivity(intent);
    }



    private class RechargeTask extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){
            progress = new ProgressDialog(Vendor_Recharge_Wallet.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait");
            progress.setIndeterminate(true);
            progress.show();

        }

        protected String doInBackground(String... strings){
            try{

                VendorDatabaseHelper databaseHelper = new VendorDatabaseHelper();
                /*vendorId Cart_Amount PAYMENT_MODE*/
                databaseHelper.purchaseCoins(Integer.valueOf(strings[0]), Integer.valueOf(strings[1]),  strings[2]);
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

            Toast.makeText(Vendor_Recharge_Wallet.this, "Transaction completed", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Vendor_Recharge_Wallet.this, VendorNewLeadActivity.class);
            startActivity(intent);
            fileList();

        }
    }
}

