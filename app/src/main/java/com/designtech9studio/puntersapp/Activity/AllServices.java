package com.designtech9studio.puntersapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.designtech9studio.puntersapp.Adapter.AllServiceListAdapter;
import com.designtech9studio.puntersapp.Adapter.ChildCatAdapter;
import com.designtech9studio.puntersapp.Adapter.ImageAdapter;
import com.designtech9studio.puntersapp.Fragment.Appliance_Repair;
import com.designtech9studio.puntersapp.Fragment.HomeDesign;
import com.designtech9studio.puntersapp.Fragment.HouseCleaning;
import com.designtech9studio.puntersapp.Fragment.Other_Services;
import com.designtech9studio.puntersapp.Fragment.Salon_At_Home;
import com.designtech9studio.puntersapp.Fragment.Shifting_Home;
import com.designtech9studio.puntersapp.Fragment.Spa_At_Home;
import com.designtech9studio.puntersapp.Fragment.Tution_Classes;
import com.designtech9studio.puntersapp.Helpers.Constant;
import com.designtech9studio.puntersapp.Helpers.DataBaseHelper;
import com.designtech9studio.puntersapp.Helpers.IntentsTags;
import com.designtech9studio.puntersapp.Model.BannerModel;
import com.designtech9studio.puntersapp.Model.CatSubChildModel;
import com.designtech9studio.puntersapp.Model.CategoryMasterModel;
import com.designtech9studio.puntersapp.Model.SubCategoryModel;
import com.designtech9studio.puntersapp.R;
import com.google.android.material.tabs.TabLayout;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class AllServices extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    DataBaseHelper dataBaseHelper;
    List<CategoryMasterModel> allCatData;
    List<SubCategoryModel> allSubCategoryList;
    RecyclerView recyclerView, imageRecyclerView;
    AllServiceListAdapter adapter;
    List<String> imageUrl = new ArrayList<>();
    String catId;
    int tabIndexTobeSelected=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_services_new);

        imageRecyclerView =findViewById(R.id.allServiceImageRecyceler);
        recyclerView = findViewById(R.id.allServiceRecyclerView);
        tabLayout = findViewById(R.id.allServiceNewTabs);
        toolbar =  findViewById(R.id.allServicestoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        catId = getIntent().getStringExtra(IntentsTags.CAT_ID);
        /*Fetch all CAT Data*/
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        imageRecyclerView.setLayoutManager(llm);


        dataBaseHelper = new DataBaseHelper();

        /*allCatData = dataBaseHelper.getAllServiceData();
        Log.i("DataFetch", "Completed");
        for (int i=0;i<allCatData.size();i++) {
            //imageUrl.add(allCatData.get(i).getImage());
            if (catId!=null && allCatData.get(i).getCatId() == Integer.valueOf(catId)) {
                tabIndexTobeSelected = i;
            }
        }*/

        /*imageUrl = BannerModel.getBannerImageList(dataBaseHelper.getBannerData());
        ImageAdapter imageAdapter = new ImageAdapter(imageUrl, getApplicationContext());
        imageRecyclerView.setAdapter(imageAdapter);*/

       /* if (tabIndexTobeSelected!=-1) {
            CategoryMasterModel model = allCatData.get(tabIndexTobeSelected);
            allCatData.remove(tabIndexTobeSelected);
            allCatData.add(0,model);

        }*/
        /*Add add to the tab*/
        /*loadDataInTabLayout();*/

        SyncData orderData = new SyncData();
        orderData.execute("");


       /* viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);*/
    }

    private class SyncData extends AsyncTask<String, String, String >
    {

        ProgressDialog progress;
        protected void onPreExecute(){
            progress = new ProgressDialog(AllServices.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait..");
            progress.setIndeterminate(true);
            progress.show();
        }

        protected String doInBackground(String... strings){
            try{

                allCatData = dataBaseHelper.getAllServiceData();
                Log.i("DataFetch", "Completed");
                for (int i=0;i<allCatData.size();i++) {
                    //imageUrl.add(allCatData.get(i).getImage());
                    if (catId!=null && allCatData.get(i).getCatId() == Integer.valueOf(catId)) {
                        tabIndexTobeSelected = i;
                    }
                }

                if (tabIndexTobeSelected!=-1) {
                    CategoryMasterModel model = allCatData.get(tabIndexTobeSelected);
                    allCatData.remove(tabIndexTobeSelected);
                    allCatData.add(0,model);

                }

                imageUrl = BannerModel.getBannerImageList(dataBaseHelper.getBannerData());
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
            loadDataInTabLayout();
            ImageAdapter imageAdapter = new ImageAdapter(imageUrl, getApplicationContext());
            imageRecyclerView.setAdapter(imageAdapter);
            loadSubcategory(allCatData.get(0).getCatId());

        }
    }
    private void loadSubcategory(int catId) {

        List<SubCategoryModel> filteredList = new ArrayList<>();
        //filteredList.addAll(catSubChildModels);
        for (SubCategoryModel i: allSubCategoryList) {
            if (i.getCatID() == catId) filteredList.add(i);
        }
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


    private void loadDataInTabLayout() {

        for (int i = 0; i < allCatData.size(); i++) {
            CategoryMasterModel model = allCatData.get(i);
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(model.getCatName());
            tab.setTag(String.valueOf(model.getCatId()));
            tabLayout.addTab(tab);
        }
        if (tabLayout.getTabCount() == 3) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }



        System.out.println();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println("Tab Selected");
                int x = tabLayout.getSelectedTabPosition();
                System.out.println("Tab Selected CatId " + tab.getTag().toString());
                loadSubcategory(Integer.valueOf(tab.getTag().toString()));
                //int sub_sub_id = Integer.parseInt(sub_cat_IdArray[x]);
                //System.out.println("SUB_SUB_CAT_ID id : " + sub_sub_id);
                //loadSubSubCategory(sub_sub_id);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /*private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Salon_At_Home(), "Salon At Home");
        adapter.addFrag(new Spa_At_Home(), "Spa At Home");
        adapter.addFrag(new Appliance_Repair(), "Appliance Repair");
        adapter.addFrag(new HomeDesign(), "Home Design");
        adapter.addFrag(new HouseCleaning(), "House Cleaning");
        adapter.addFrag(new Shifting_Home(), "Shifting Home");
        adapter.addFrag(new Tution_Classes(), "Tution Classes");
        adapter.addFrag(new Other_Services(), "Other Services");
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }*/
}
