package com.designtech9studio.puntersapp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.designtech9studio.puntersapp.BusinessImplement;
import com.designtech9studio.puntersapp.ConnectionClass;
import com.designtech9studio.puntersapp.Helpers.DataBaseHelper;
import com.designtech9studio.puntersapp.Helpers.IntentsTags;
import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.Helpers.VendorDatabaseHelper;
import com.designtech9studio.puntersapp.Model.CategoryMasterModel;
import com.designtech9studio.puntersapp.Model.SubCategoryModel;
import com.designtech9studio.puntersapp.R;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    EditText _txtUserName,_txtPassword, _txtEmailId, _txtPhone;
    Spinner catMasterSpanner, subSpanner;
    Button _btnRegister;
    String[] array;
    String[] statusName={"Select","Dehradun"};
    ArrayList<String> data_user = new ArrayList<String>();
    BusinessImplement insertClass = new BusinessImplement();

    List<CategoryMasterModel> categoryMasterModelArrayList = new ArrayList<>();
    List<SubCategoryModel> subCategoryModels = new ArrayList<>();

    String[] catStringList = {"Adarsh"};
    String[] subStringList = {"Adarsh"};


    ArrayAdapter<String> catArrayAdapter;
    ArrayAdapter<String> subArrayAdapter;

    int selectedCat = -1;
    int selectedSub = -1;


    private com.designtech9studio.puntersapp.ConnectionClass ConnectionClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        TextView signin = findViewById(R.id.signIn_text);
        catMasterSpanner = findViewById(R.id.spnCatMaster);
        subSpanner = findViewById(R.id.spnSubChild);


        subSpanner.setVisibility(View.GONE);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, Vendor_SignIn.class));
                finish();
            }
        });
        InitializeBind();

    }

    private void InitializeBind() {
        _txtUserName = (EditText) findViewById(R.id.txtUserName);
        _txtPassword = (EditText) findViewById(R.id.txtPassword);
        _txtEmailId = (EditText) findViewById(R.id.txtEmailId);
        _txtPhone = findViewById(R.id.txtMobileNo);

        _btnRegister = (Button) findViewById(R.id.register);


        MasterCatGetTask masterCatGetTask = new MasterCatGetTask();
        masterCatGetTask.execute();
        //setCatArrayAdapter();
        // getIMEI();



        /*_spnCountryID = (Spinner)findViewById(R.id.spnCountryID);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusName);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        _spnCountryID.setAdapter(dataAdapter);*/


        catMasterSpanner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Selected", "Position: " + position);
                /*if (position>0) {
                    selectedCat = position-1;
                    Log.i("CatName", categoryMasterModelArrayList.get(selectedCat).getCatName());
                    SubCatGetTask subCatGetTask = new SubCatGetTask();
                    subCatGetTask.execute(String.valueOf(categoryMasterModelArrayList.get(selectedCat).getCatId()));
                }*/

                selectedCat = position;
                Log.i("CatName", categoryMasterModelArrayList.get(selectedCat).getCatName());
                SubCatGetTask subCatGetTask = new SubCatGetTask();
                subCatGetTask.execute(String.valueOf(categoryMasterModelArrayList.get(selectedCat).getCatId()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("Selected", "Nothing: ");
            }
        });

        subSpanner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Selected", "Position: "+position);
                selectedSub = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ConnectionClass = new ConnectionClass();
        _btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveRecord();
            }
        });

    }

    private void SaveRecord()
    {
        String ErrorList ="";

        String email = _txtEmailId.getText().toString().trim();
        String username = _txtUserName.getText().toString().trim();
        String password = _txtPassword.getText().toString().trim();
        String mobilePhone = _txtPhone.getText().toString().trim();

        if(_txtUserName.getText().toString().trim().equals(""))
            ErrorList +="Enter User Name. \n";
        if(_txtPassword.getText().toString().trim().equals(""))
            ErrorList +="Enter Password. \n";
        if(_txtEmailId.getText().toString().trim().equals("") || !email.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$"))
            ErrorList +="Enter Valid Email Id. \n";
        if (selectedCat < 0)
            ErrorList +="Select Category. \n";

        if(ErrorList.length() > 0)
            Toast.makeText(this,ErrorList.toString(),Toast.LENGTH_LONG).show();
        else
        {
            /*Register Vendor*/
            CategoryMasterModel categoryMasterModel = categoryMasterModelArrayList.get(selectedCat);

            /*int catId, subCatId, childId;
    String uploadPic1, uploadPic2, uploadPic3, description;;
    double amount;*/
            int catId = categoryMasterModel.getCatId();

            double amount = 0.0;
            String description = categoryMasterModel.getDescription();
            String uploadPic1 = categoryMasterModel.getImage();

            String uploadPic3 = "";
            int childId = 0;
            int subCatId = 0;
            String uploadPic2 = "";
            if (selectedSub < 0) {
            }else{
                SubCategoryModel subCategoryModel = subCategoryModels.get(selectedSub);
                subCatId = subCategoryModel.getSubCatId();
                uploadPic2 = subCategoryModel.getImage();
            }



            Intent intent = new Intent(getApplicationContext(), VendorOtpActivity.class);
            intent.putExtra(IntentsTags.EMAIL, email);
            intent.putExtra(IntentsTags.PASSWORD, password);
            intent.putExtra(IntentsTags.USERNAME, username);
            intent.putExtra(IntentsTags.MOBILE, mobilePhone);

            intent.putExtra(IntentsTags.CAT_ID, String.valueOf(catId));
            intent.putExtra(IntentsTags.SUB_CAT_ID, String.valueOf(subCatId));
            intent.putExtra(IntentsTags.amount, String.valueOf(amount));
            intent.putExtra(IntentsTags.description, description);
            intent.putExtra(IntentsTags.uploadPic1, uploadPic1);
            intent.putExtra(IntentsTags.uploadPic2, uploadPic2);
            intent.putExtra(IntentsTags.uploadPic3, uploadPic3);
            intent.putExtra(IntentsTags.childId, String.valueOf(childId));



            startActivity(intent);

        }

    }

    private void clearText()
    {
        _txtEmailId.setText("");
        _txtPassword.setText("");
        _txtUserName.setText("");

        _btnRegister.setText("Save Record");

    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Vendor_SignIn.class);
        startActivity(intent);
    }

    private class MasterCatGetTask extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){
            progress = new ProgressDialog(RegistrationActivity.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait");
            progress.setIndeterminate(true);
            progress.show();
        }

        protected String doInBackground(String... strings){
            try{

                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                categoryMasterModelArrayList = dataBaseHelper.getAllServiceData();


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
            Log.i("Loading", "Cat");

            catStringList = new String[categoryMasterModelArrayList.size()];
            int index = 0;
            for (CategoryMasterModel model: categoryMasterModelArrayList) {
                catStringList[index] = model.getCatName();
                /*if (index==0) {
                    catStringList[index] = "Select Category";
                }else{

                }*/

                Log.i("Cat", catStringList[index]);
                index++;
            }
           // ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusName);

           setCatArrayAdapter();

        }
    }
    private class SubCatGetTask extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){
            progress = new ProgressDialog(RegistrationActivity.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait");
            progress.setIndeterminate(true);
            progress.show();
        }

        protected String doInBackground(String... strings){
            try{

                Log.i("CatId: ", strings[0]);
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                subCategoryModels = dataBaseHelper.getSubCategoryData(strings[0]);

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
            Log.i("Loading", "SubCat");

            subStringList = new String[subCategoryModels.size()];
            int index = 0;
            for (SubCategoryModel model: subCategoryModels) {
                subStringList[index] = model.getSubcategoryName();
                Log.i("SubCat", subStringList[index]);
                index++;
            }
            // ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusName);

            if (subCategoryModels.size() == 0) {

                selectedSub = -1;
                Toast.makeText(RegistrationActivity.this, "No Sub Category available", Toast.LENGTH_SHORT).show();
            }else{

                setSubCatAdapter();
            }


        }

    }


    private void setSubCatAdapter() {
        subSpanner.setVisibility(View.VISIBLE);
        subArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subStringList);
        subArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subSpanner.setAdapter(subArrayAdapter);
        Log.i("Sub", "BindingDone");

    }

    public void setCatArrayAdapter() {
        //String catStringList[] = {"A1","A1", "A1", "A1", "A1", "A1", "A1", "A1", "A1", "A1", "A1", "A1", "A1", "A1", "A1", "A1", "A1"};
        catArrayAdapter = new ArrayAdapter<>(this ,android.R.layout.simple_spinner_item, catStringList);
        catArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        catMasterSpanner.setAdapter(catArrayAdapter);
        Log.i("Cat", "BindingDone");
    }
}


