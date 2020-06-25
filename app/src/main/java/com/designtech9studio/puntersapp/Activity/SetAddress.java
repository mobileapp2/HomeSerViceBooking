package com.designtech9studio.puntersapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.designtech9studio.puntersapp.Helpers.DataBaseHelper;
import com.designtech9studio.puntersapp.Helpers.IntentsTags;
import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.Model.AddressModel;
import com.designtech9studio.puntersapp.Model.VendorProfileModel;
import com.designtech9studio.puntersapp.R;

public class SetAddress extends AppCompatActivity {
    Toolbar toolbar;
    EditText street1_ed, street2_ed, landmark_ed, city_ed, state_ed, country_ed;
    TextView confirmButton;
    AddressModel addressModel;
    SharedPreferenceHelper sharedPreferenceHelper;
    DataBaseHelper dataBaseHelper;
    VendorProfileModel selectedVendor = null;
    double userLat, userLot;
    String msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_address);

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

        setTitle("Address");
        street1_ed.setText(addressModel.getStreetAddress1());
        street2_ed.setText(addressModel.getStreetAddress2());
        landmark_ed.setText("");
        city_ed.setText(addressModel.getCityName());
        state_ed.setText(addressModel.getStateName());
        country_ed.setText(addressModel.getCountryName());
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
                    Toast.makeText(SetAddress.this, "Please! fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                /*get all vendor in users city*/


                addressModel.setStreetAddress1(street1);
                addressModel.setStreetAddress2(street2);
                addressModel.setLandmark(landmark);
                addressModel.setCityName(city);
                addressModel.setStateName(state);
                addressModel.setCountryName(country);

                System.out.println("Send Data to database");
                System.out.println(addressModel.toString());

                sharedPreferenceHelper.setAddress(addressModel);

                Intent intent = new Intent(getApplicationContext(),ClientHomepageActivity.class);
                intent.putExtra(IntentsTags.NEW_ADDRESS, "New");
                startActivity(intent);
                finish();

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
}
