package com.designtech9studio.puntersapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.designtech9studio.puntersapp.Adapter.AllServiceListAdapter;
import com.designtech9studio.puntersapp.Adapter.ImageAdapter;
import com.designtech9studio.puntersapp.Helpers.DataBaseHelper;
import com.designtech9studio.puntersapp.Model.BannerModel;
import com.designtech9studio.puntersapp.Model.CategoryMasterModel;
import com.designtech9studio.puntersapp.Model.SubCategoryModel;
import com.designtech9studio.puntersapp.R;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class SubCategorySearch extends AppCompatActivity {

    List<SubCategoryModel> allSubCategoryList = new ArrayList<>();
    private Toolbar toolbar;
    AllServiceListAdapter adapter;

    List<SubCategoryModel> filteredList = new ArrayList<>();
    EditText editText;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_search);
        editText = findViewById(R.id.searchTxt);
        recyclerView = findViewById(R.id.searchRecyler);
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Log.i("BACK", "Detected");
            }
        });

        SyncData orderData = new SyncData();
        orderData.execute("");

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Log.i("Serach", "1");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.i("Serach", "2");
                String search = s.toString();
                filteredList.clear();
                for (SubCategoryModel subCategoryModel: allSubCategoryList) {
                    if (subCategoryModel.getSubcategoryName().toLowerCase().contains(search.toLowerCase())) {
                        filteredList.add(subCategoryModel);
                    }
                }
                Log.i("Search", s.toString());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Log.i("Serach", "3");
            }
        });



    }
    private class SyncData extends AsyncTask<String, String, String >
    {

        ProgressDialog progress;
        protected void onPreExecute(){
            progress = new ProgressDialog(SubCategorySearch.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait..");
            progress.setIndeterminate(true);
            progress.show();
        }

        protected String doInBackground(String... strings){
            try{

                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                allSubCategoryList = dataBaseHelper.getAllSubCategoryData();
                System.out.println("LOADING SubCategory DATA");

            }catch (Exception e){
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));

            }
            return "";
        }
        protected void onPostExecute(String msg){
            progress.dismiss();
            filteredList.addAll(allSubCategoryList);
            setUpAdapter();

        }
    }

    private void setUpAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        adapter = new AllServiceListAdapter(filteredList, this);
        //childCatAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        if (filteredList.size() == 0) {
            Toast.makeText(this, "Sorry, No Item To Display", Toast.LENGTH_SHORT).show();
        }
    }
}
