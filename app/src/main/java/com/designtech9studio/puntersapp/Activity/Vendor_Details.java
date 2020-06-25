package com.designtech9studio.puntersapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.designtech9studio.puntersapp.BusinessImplement;
import com.designtech9studio.puntersapp.ConnectionClass;
import com.designtech9studio.puntersapp.Helpers.DataBaseHelper;
import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.Helpers.VendorDatabaseHelper;
import com.designtech9studio.puntersapp.Model.SubCategoryModel;
import com.designtech9studio.puntersapp.R;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

public class Vendor_Details extends AppCompatActivity {

    EditText _txtCOmpanyName,_txtGstNo, _txtAdharCard,_txtpanCardNo,_txtAddress,_txtMobileNo, _txtFirstName, _txtLastName;
    Spinner _spnCountryID;
    Button _btnRegister;
    String[] array;
    String[] statusName={"Select","India"};
    ArrayList<String> data_user = new ArrayList<String>();
    BusinessImplement insertClass = new BusinessImplement();;
    private com.designtech9studio.puntersapp.ConnectionClass ConnectionClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor__details);
        InitializeComponent();

    }

    private void InitializeComponent()
    {

        _txtCOmpanyName = (EditText) findViewById(R.id.txtCompanyName);
        _txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        _txtLastName = (EditText) findViewById(R.id.txtLastName);
        _txtGstNo = (EditText) findViewById(R.id.txtGstNumber);
        _txtAdharCard = (EditText) findViewById(R.id.txtAdharCard);
        _txtpanCardNo = (EditText) findViewById(R.id.txtpanCardNo);
        _txtAddress = (EditText)findViewById(R.id.txtAddress);
        _txtMobileNo = (EditText)findViewById(R.id.txtMobileNo);
        _btnRegister = (Button) findViewById(R.id.btnSubmit);
        // getIMEI();
        _spnCountryID = (Spinner)findViewById(R.id.spnCountryID);
        /*ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusName);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/

//        _spnCountryID.setAdapter(dataAdapter);
        ConnectionClass = new ConnectionClass();
        _btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateTask updateTask = new UpdateTask();
                updateTask.execute();
            }
        });
    }
    private class UpdateTask extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){
            progress = new ProgressDialog(Vendor_Details.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait");
            progress.setIndeterminate(true);
            progress.show();

        }

        protected String doInBackground(String... strings){
            try{
                SaveRecord();
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
            Log.i("Loading", "Completed");
            Intent intent = new Intent(getApplicationContext(), VendorNewLeadActivity.class);
            startActivity(intent);
            finish();


        }

    }


    public void SaveRecord()
    {
        String ErrorList ="";

        if(_txtCOmpanyName.getText().toString().trim().equals(""))
            ErrorList +="Enter Company Name. \n";
        if(_txtGstNo.getText().toString().trim().equals(""))
            ErrorList +="Enter GST No. \n";
        if(_txtAdharCard.getText().toString().trim().equals(""))
            ErrorList +="Enter Adhar Card No.  \n";

        if(_txtLastName.getText().toString().trim().equals(""))
            ErrorList +="Enter Last Name. \n";
        if(_txtFirstName.getText().toString().trim().equals(""))
            ErrorList +="Enter First Name. \n";
        if(_txtpanCardNo.getText().toString().trim().equals(""))
            ErrorList +="Enter Pan Card No.  \n";
        /*if(_txtAddress.getText().toString().trim().equals(""))
            ErrorList +="Enter Address.  \n";
        if(_txtMobileNo.getText().toString().trim().equals(""))
            ErrorList +="Enter Mobile No.  \n";*/
        if(ErrorList.length() > 0)
            Toast.makeText(this,ErrorList.toString(),Toast.LENGTH_LONG).show();
        else
        {
            if(ConnectionClass == null)
                Toast.makeText(this, "Error in connection with SQL server", Toast.LENGTH_LONG).show();
            else
            {

                String Result = "";
                if(_btnRegister.getText().toString().equals("Save Record"))
                {

                    /*Uodate vendor*/
                    String companyName = _txtCOmpanyName.getText().toString().trim();
                    String firstName = _txtFirstName.getText().toString().trim();
                    String lastName = _txtLastName.getText().toString().trim();
                    String vendorName = firstName + " " + lastName;

                    String gst = _txtGstNo.getText().toString().trim();
                    String addhar = _txtAdharCard.getText().toString().trim();
                    String pancard = _txtpanCardNo.getText().toString().trim();
                    SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
                    String vendorId = sharedPreferenceHelper.getVendorId();

                    VendorDatabaseHelper vendorDatabaseHelper = new VendorDatabaseHelper();
                    vendorDatabaseHelper.updateVendorIdentityRecord(gst, pancard, addhar, vendorId);
                    vendorDatabaseHelper.updateVendorProfileName(firstName, lastName, vendorName, companyName, vendorId);



                    //Result = insertClass.SaveRecordVendorDetails(0,_txtFirstName.getText().toString().trim(),_txtLastName.getText().toString().trim(),"ghffgh",_txtCOmpanyName.getText().toString().trim(),_txtGstNo.getText().toString().trim(),_txtAdharCard.getText().toString().trim(),_txtpanCardNo.getText().toString().trim(),_txtMobileNo.getText().toString().trim(),_txtAddress.getText().toString().trim());

                }

            }

        }

    }

    private void clearText()
    {
        _txtCOmpanyName.setText("");
        _txtAddress.setText("");
        _txtAdharCard.setText("");
        _txtGstNo.setText("");
        _txtMobileNo.setText("");
        _txtpanCardNo.setText("");

        _btnRegister.setText("Save Record");

    }
}
