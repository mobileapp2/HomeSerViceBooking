package com.designtech9studio.puntersapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.designtech9studio.puntersapp.Helpers.DataBaseHelper;
import com.designtech9studio.puntersapp.Helpers.DateGenerator;
import com.designtech9studio.puntersapp.Helpers.DateHelper;
import com.designtech9studio.puntersapp.Helpers.IntentsTags;
import com.designtech9studio.puntersapp.Helpers.LocalDatabaseHelper;
import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.Model.AddressModel;
import com.designtech9studio.puntersapp.Model.CatSubChildModel;
import com.designtech9studio.puntersapp.Model.ProfileModel;
import com.designtech9studio.puntersapp.R;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MySchedule extends AppCompatActivity implements View.OnClickListener {

    Button one, two, three, four, five, six, seven, eight, nine, locationChange;
    TextView locationAddress, proceed  ;
    RecyclerView dateViewer;
    Button button1,button2,button3,button4,button5,button6,button7, button8,button9,button10, button11,button12;
    int selectedDayButtonId=-1, selectedTimeButtonId=-1;
    int selectedDayIndex=-1;
    SharedPreferenceHelper sharedPreferenceHelper;
    DataBaseHelper dataBaseHelper = new DataBaseHelper();

    Toolbar toolbar;
    String vendorId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment);
        setTitle("Booking");

        sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());


        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        six = findViewById(R.id.six);
        seven = findViewById(R.id.seven);
        eight = findViewById(R.id.eight);
        proceed = findViewById(R.id.proceed);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        button10 = findViewById(R.id.button10);
        button11 = findViewById(R.id.button11);
        button12 = findViewById(R.id.button12);


        toolbar =  findViewById(R.id.scheduleToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

                Log.i("BACK", "Detected");
            }
        });

        vendorId = getIntent().getStringExtra(IntentsTags.VENDOR_ID);
        System.out.println("MySchesule: VendorId: " + vendorId );



        List<DateHelper> dateHelpers = DateGenerator.generateDate();
        List<DateHelper> dateWithMonthNumber = DateGenerator.generateDateWithMonthNumber();

        button1.setText(dateHelpers.get(0).toString());
        button2.setText(dateHelpers.get(1).toString());
        button3.setText(dateHelpers.get(2).toString());
        button4.setText(dateHelpers.get(3).toString());
        button5.setText(dateHelpers.get(4).toString());
        button6.setText(dateHelpers.get(5).toString());
        button7.setText(dateHelpers.get(6).toString());
        button8.setText(dateHelpers.get(7).toString());
        button9.setText(dateHelpers.get(8).toString());
        button10.setText(dateHelpers.get(9).toString());
        button11.setText(dateHelpers.get(10).toString());
        button12.setText(dateHelpers.get(11).toString());

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedDayIndex ==-1) {
                    Toast.makeText(getApplicationContext(), "Please Select Date", Toast.LENGTH_SHORT).show();
                }else if (selectedTimeButtonId==-1){
                    Toast.makeText(getApplicationContext(), "Please Select Time", Toast.LENGTH_SHORT).show();
                }else{
                    /*Get selected day and time*/
                    Button selectedDayButton = findViewById(selectedDayButtonId);
                    Button selectedTimeButton = findViewById(selectedTimeButtonId);

                    String date = DateGenerator.getDate(selectedDayIndex);
                    Log.i("DATE", date.toString());


                    String selectedTimeString = selectedTimeButton.getText().toString().trim();

                    Log.i("Time", selectedTimeString);

                    String temp[] = selectedTimeString.split("-");
                    String temp2[] = temp[0].split(" ");
                    String temp3 = temp2[0] + ":00 " + temp2[1];

                    selectedTimeString = temp3;


                    Log.i("Time", selectedTimeString);


                    Intent intent = new Intent(getApplicationContext(), SelectModeOfPayment.class);
                    intent.putExtra(IntentsTags.DATE, date);
                    intent.putExtra(IntentsTags.TIME, selectedTimeString);
                    intent.putExtra(IntentsTags.VENDOR_ID, vendorId);
                    startActivity(intent);


                    /*DataTransferHelper.dateOfBooking = date;
                    DataTransferHelper.timeOfBooking = selectedTimeString;*/


                   /* Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                    startActivity(intent);*/
                }

            }
        });

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button10.setOnClickListener(this);
        button11.setOnClickListener(this);
        button12.setOnClickListener(this);

    }




   /* private void confirmUserBooking(String selectedDate, String selectedTime, String modeOfPayment) {

        String vendorId = sharedPreferenceHelper.getUserid();
        //ProfileModel profileModel = dataBaseHelper.getProfileData(vendorId);
        AddressModel addressModel = sharedPreferenceHelper.getAddress();
        String streetAddress = addressModel.toString();
        LocalDatabaseHelper localDatabaseHelper = new LocalDatabaseHelper(getApplicationContext());
        int grossTotal = localDatabaseHelper.getGrandTotal();

        List<CatSubChildModel> cartData =  localDatabaseHelper.fetchCartData();
        dataBaseHelper.setBookingTable(vendorId, cartData, grossTotal, selectedDate, selectedTime, streetAddress, modeOfPayment, "Active");
        Log.i("Insertion", "Copleted");
        localDatabaseHelper.flushCart();
    }*/

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.one|| id==R.id.two || id==R.id.three || id==R.id.four || id==R.id.five || id==R.id.six || id==R.id.seven || id==R.id.eight ) {

            selectedTimeButtonId = view.getId();



            if (selectedDayButtonId == R.id.button1) {
                Date date = new Date();
                System.out.println(date);
                SimpleDateFormat simpDate;
                simpDate = new SimpleDateFormat("kk:mm:ss");
                System.out.println(simpDate.format(date));
                int hr = Integer.parseInt(simpDate.format(date).split(":")[0]);
                int min = Integer.parseInt(simpDate.format(date).split(":")[1]);

                System.out.println("Hour: " + hr);
                if (hr>10 || (hr==10 && min > 0))one.setEnabled(false);
                if (hr>11 || (hr==11 && min > 0))two.setEnabled(false);
                if (hr>13 || (hr==13 && min > 0))three.setEnabled(false);
                if (hr>14 || (hr==14 && min > 0))four.setEnabled(false);
                if (hr>15 || (hr==15 && min > 0))five.setEnabled(false);
                if (hr>16 || (hr==16 && min > 0))six.setEnabled(false);
                if (hr>17 || (hr==17 && min > 0))seven.setEnabled(false);
                if (hr>18 || (hr==18 && min > 0))eight.setEnabled(false);

                if (one.isEnabled()) {
                    one.setBackgroundResource(R.drawable.time_button_background);
                    one.setTextColor(Color.BLACK);
                }
                if (two.isEnabled()) {
                    one.setBackgroundResource(R.drawable.time_button_background);
                    one.setTextColor(Color.BLACK);
                }
                if (three.isEnabled()) {
                    three.setBackgroundResource(R.drawable.time_button_background);
                    three.setTextColor(Color.BLACK);
                } if (four.isEnabled()) {
                    four.setBackgroundResource(R.drawable.time_button_background);
                    four.setTextColor(Color.BLACK);
                }
                if (five.isEnabled()) {
                    five.setBackgroundResource(R.drawable.time_button_background);
                    five.setTextColor(Color.BLACK);
                }
                if (six.isEnabled()) {
                    six.setBackgroundResource(R.drawable.time_button_background);
                    six.setTextColor(Color.BLACK);
                }
                if (seven.isEnabled()) {
                    seven.setBackgroundResource(R.drawable.time_button_background);
                    seven.setTextColor(Color.BLACK);
                }
                if (eight.isEnabled()) {
                    eight.setBackgroundResource(R.drawable.time_button_background);
                    eight.setTextColor(Color.BLACK);
                }


            }else{
                one.setBackgroundResource(R.drawable.time_button_background);
                two.setBackgroundResource(R.drawable.time_button_background);
                three.setBackgroundResource(R.drawable.time_button_background);
                four.setBackgroundResource(R.drawable.time_button_background);
                five.setBackgroundResource(R.drawable.time_button_background);
                six.setBackgroundResource(R.drawable.time_button_background);
                seven.setBackgroundResource(R.drawable.time_button_background);
                eight.setBackgroundResource(R.drawable.time_button_background);

                //nine.setBackgroundResource(R.drawable.time_button_background);
                one.setTextColor(Color.BLACK);
                two.setTextColor(Color.BLACK);
                three.setTextColor(Color.BLACK);
                four.setTextColor(Color.BLACK);
                five.setTextColor(Color.BLACK);
                six.setTextColor(Color.BLACK);
                seven.setTextColor(Color.BLACK);
                eight.setTextColor(Color.BLACK);
            }





            /*
            *  if (hr>10 || (hr==10 && min > 0))one.setEnabled(false);
                    if (hr>11 || (hr==11 && min > 0))two.setEnabled(false);
                    if (hr>13 || (hr==13 && min > 0))three.setEnabled(false);
                    if (hr>14 || (hr==14 && min > 0))four.setEnabled(false);
                    if (hr>15 || (hr==15 && min > 0))five.setEnabled(false);
                    if (hr>16 || (hr==16 && min > 0))six.setEnabled(false);
                    if (hr>17 || (hr==17 && min > 0))seven.setEnabled(false);
                    if (hr>18 || (hr==18 && min > 0))eight.setEnabled(false);*/
            switch (view.getId())
            {
                case R.id.one:
                    one.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    one.setTextColor(Color.WHITE);
                    return;
                case R.id.two:
                    two.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    two.setTextColor(Color.WHITE);
                    return;
                case R.id.three:
                    three.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    three.setTextColor(Color.WHITE);

                    return;
                case R.id.four:
                    four.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    four.setTextColor(Color.WHITE);

                    return;
                case R.id.five:
                    five.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    five.setTextColor(Color.WHITE);

                    return;
                case R.id.six:
                    six.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    six.setTextColor(Color.WHITE);

                    return;
                case R.id.seven:
                    seven.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    seven.setTextColor(Color.WHITE);

                    return;
                case R.id.eight:
                    eight.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    eight.setTextColor(Color.WHITE);
                    return;

            }


            if (selectedDayButtonId == R.id.button1) {
                Date date = new Date();
                System.out.println(date);
                SimpleDateFormat simpDate;
                simpDate = new SimpleDateFormat("kk:mm:ss");
                System.out.println(simpDate.format(date));
                int hr = Integer.parseInt(simpDate.format(date).split(":")[0]);
                int min = Integer.parseInt(simpDate.format(date).split(":")[1]);

                System.out.println("Hour: " + hr);
                if (hr>10 || (hr==10 && min > 0))one.setEnabled(false);
                if (hr>11 || (hr==11 && min > 0))two.setEnabled(false);
                if (hr>13 || (hr==13 && min > 0))three.setEnabled(false);
                if (hr>14 || (hr==14 && min > 0))four.setEnabled(false);
                if (hr>15 || (hr==15 && min > 0))five.setEnabled(false);
                if (hr>16 || (hr==16 && min > 0))six.setEnabled(false);
                if (hr>17 || (hr==17 && min > 0))seven.setEnabled(false);
                if (hr>18 || (hr==18 && min > 0))eight.setEnabled(false);

            }
        }else{
            button1.setBackgroundResource(R.drawable.categoty_card_edges);
            button2.setBackgroundResource(R.drawable.categoty_card_edges);
            button3.setBackgroundResource(R.drawable.categoty_card_edges);
            button4.setBackgroundResource(R.drawable.categoty_card_edges);
            button5.setBackgroundResource(R.drawable.categoty_card_edges);
            button6.setBackgroundResource(R.drawable.categoty_card_edges);
            button7.setBackgroundResource(R.drawable.categoty_card_edges);
            button8.setBackgroundResource(R.drawable.categoty_card_edges);
            button9.setBackgroundResource(R.drawable.categoty_card_edges);
            button10.setBackgroundResource(R.drawable.categoty_card_edges);
            button11.setBackgroundResource(R.drawable.categoty_card_edges);
            button12.setBackgroundResource(R.drawable.categoty_card_edges);

            button1.setTextColor(Color.BLACK);
            button2.setTextColor(Color.BLACK);
            button3.setTextColor(Color.BLACK);
            button4.setTextColor(Color.BLACK);
            button5.setTextColor(Color.BLACK);
            button6.setTextColor(Color.BLACK);
            button7.setTextColor(Color.BLACK);
            button8.setTextColor(Color.BLACK);
            button9.setTextColor(Color.BLACK);
            button10.setTextColor(Color.BLACK);
            button11.setTextColor(Color.BLACK);
            button12.setTextColor(Color.BLACK);




            selectedDayButtonId = view.getId();

            if (selectedDayButtonId == R.id.button1 ){
                Log.i("Selected", "Button 1 Selected: " + R.id.button1);

            }else{
                one.setEnabled(true);
                two.setEnabled(true);
                three.setEnabled(true);
                four.setEnabled(true);
                five.setEnabled(true);
                six.setEnabled(true);
                seven.setEnabled(true);
                eight.setEnabled(true);
            }
            switch (view.getId()){
                case R.id.button1:
                    selectedDayIndex=0;
                    button1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button1.setTextColor(Color.WHITE);

                    Date date = new Date();
                    System.out.println(date);
                    SimpleDateFormat simpDate;
                    simpDate = new SimpleDateFormat("kk:mm:ss");
                    System.out.println(simpDate.format(date));
                    int hr = Integer.parseInt(simpDate.format(date).split(":")[0]);
                    int min = Integer.parseInt(simpDate.format(date).split(":")[1]);

                    System.out.println("Hour: " + hr);
                    if (hr>10 || (hr==10 && min > 0))one.setEnabled(false);
                    if (hr>11 || (hr==11 && min > 0))two.setEnabled(false);
                    if (hr>13 || (hr==13 && min > 0))three.setEnabled(false);
                    if (hr>14 || (hr==14 && min > 0))four.setEnabled(false);
                    if (hr>15 || (hr==15 && min > 0))five.setEnabled(false);
                    if (hr>16 || (hr==16 && min > 0))six.setEnabled(false);
                    if (hr>17 || (hr==17 && min > 0))seven.setEnabled(false);
                    if (hr>18 || (hr==18 && min > 0))eight.setEnabled(false);

                    return;
                case R.id.button2:
                    selectedDayIndex=1;
                    button2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button2.setTextColor(Color.WHITE);

                    return;
                case R.id.button3:
                    selectedDayIndex=2;
                    button3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button3.setTextColor(Color.WHITE);

                    return;
                case R.id.button4:
                    selectedDayIndex=3;
                    button4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button4.setTextColor(Color.WHITE);

                    return;
                case R.id.button5:
                    selectedDayIndex=4;
                    button5.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button5.setTextColor(Color.WHITE);

                    return;
                case R.id.button6:
                    selectedDayIndex=5;
                    button6.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button6.setTextColor(Color.WHITE);

                    return;
                case R.id.button7:
                    selectedDayIndex=6;
                    button7.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button7.setTextColor(Color.WHITE);

                    return;
                case R.id.button8:
                    selectedDayIndex=7;
                    button8.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button8.setTextColor(Color.WHITE);

                    return;
                case R.id.button9:
                    selectedDayIndex=8;
                    button9.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button9.setTextColor(Color.WHITE);

                    return;
                case R.id.button10:
                    selectedDayIndex=9;
                    button10.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button10.setTextColor(Color.WHITE);

                    return;
                case R.id.button11:
                    selectedDayIndex=10;
                    button11.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button11.setTextColor(Color.WHITE);

                    return;
                case R.id.button12:
                    selectedDayIndex=11;
                    button12.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button12.setTextColor(Color.WHITE);

                    return;
                default:
                    return;
            }
        }

    }
}
