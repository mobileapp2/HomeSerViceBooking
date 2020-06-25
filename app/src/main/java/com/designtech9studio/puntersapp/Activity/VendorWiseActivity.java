package com.designtech9studio.puntersapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.designtech9studio.puntersapp.Helpers.DataBaseHelper;
import com.designtech9studio.puntersapp.Helpers.VendorDatabaseHelper;
import com.designtech9studio.puntersapp.Model.CatSubChildModel;
import com.designtech9studio.puntersapp.Model.CategoryMasterModel;
import com.designtech9studio.puntersapp.Model.SubCategoryModel;
import com.designtech9studio.puntersapp.R;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class VendorWiseActivity extends AppCompatActivity {

    Spinner catMasterSpanner, subSpanner, childSpinner;

    List<CategoryMasterModel> categoryMasterModelArrayList = new ArrayList<>();
    List<SubCategoryModel> subCategoryModels = new ArrayList<>();
    List<CatSubChildModel> childModels = new ArrayList<>();

    String[] catStringList = {"Adarsh"};
    String[] subStringList = {"Adarsh"};
    String[] childStringList = {"Adsrs"};


    ArrayAdapter<String> catArrayAdapter;
    ArrayAdapter<String> subArrayAdapter;
    ArrayAdapter<String> childArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_wise);
        catMasterSpanner = findViewById(R.id.spnCatMaster);
        subSpanner = findViewById(R.id.spnSubChild);
        childSpinner = findViewById(R.id.spnChild);


        catMasterSpanner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Selected", "Position: " + position);
                if (position>0) {
                    SubCatGetTask subCatGetTask = new SubCatGetTask();
                    subCatGetTask.execute(String.valueOf(categoryMasterModelArrayList.get(position-1).getCatId()));
                }
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
                ChildGetTask childGetTask = new ChildGetTask();
                childGetTask.execute(String.valueOf(subCategoryModels.get(position).getSubCatId()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       /* MasterCatGetTask masterCatGetTask = new MasterCatGetTask();
        masterCatGetTask.execute();*/

    }
    private class MasterCatGetTask extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){
            progress = new ProgressDialog(VendorWiseActivity.this,R.style.AppCompatAlertDialogStyle);
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

            catStringList = new String[categoryMasterModelArrayList.size()+1];
            int index = 0;
            for (CategoryMasterModel model: categoryMasterModelArrayList) {
                if (index==0) {
                    catStringList[index] = "Select Category";
                }else{
                    catStringList[index] = model.getCatName();
                }

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
            progress = new ProgressDialog(VendorWiseActivity.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait");
            progress.setIndeterminate(true);
            progress.show();
        }

        protected String doInBackground(String... strings){
            try{

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

            setSubCatAdapter();

        }

    }

    private class ChildGetTask extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){
            progress = new ProgressDialog(VendorWiseActivity.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait");
            progress.setIndeterminate(true);
            progress.show();
        }

        protected String doInBackground(String... strings){
            try{

                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                childModels = dataBaseHelper.getCatSubChildData(strings[0]);

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
            Log.i("Loading", "Child");

            childStringList = new String[childModels.size()];
            int index = 0;
            for (CatSubChildModel model: childModels) {
                childStringList[index] = model.getSubChildName();
                Log.i("SubCat", childStringList[index]);
                index++;
            }
            // ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusName);

            setChildAdater();

        }


    }

    private void setChildAdater() {
        childSpinner.setVisibility(View.VISIBLE);
        childArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, childStringList);
        childArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        childSpinner.setAdapter(childArrayAdapter);
        Log.i("child", "BindingDone");
    }

    private void setSubCatAdapter() {
        subSpanner.setVisibility(View.VISIBLE);
        subArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subStringList);
        subArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subSpanner.setAdapter(subArrayAdapter);
        Log.i("Sub", "BindingDone");

    }

    public void setCatArrayAdapter() {
        catArrayAdapter = new ArrayAdapter<>(this ,android.R.layout.simple_spinner_item, catStringList);
        catArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catMasterSpanner.setAdapter(catArrayAdapter);
        Log.i("Cat", "BindingDone");
    }
}
