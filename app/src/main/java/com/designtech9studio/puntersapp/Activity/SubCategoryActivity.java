package com.designtech9studio.puntersapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.designtech9studio.puntersapp.Adapter.ChildCatAdapter;
import com.designtech9studio.puntersapp.Adapter.ImageAdapter;
import com.designtech9studio.puntersapp.Adapter.MainPageAdapter;
import com.designtech9studio.puntersapp.Fragment.Appliance_Repair;
import com.designtech9studio.puntersapp.Fragment.HomeDesign;
import com.designtech9studio.puntersapp.Fragment.HouseCleaning;
import com.designtech9studio.puntersapp.Fragment.Other_Services;
import com.designtech9studio.puntersapp.Fragment.Salon_At_Home;
import com.designtech9studio.puntersapp.Fragment.Shifting_Home;
import com.designtech9studio.puntersapp.Fragment.Spa_At_Home;
import com.designtech9studio.puntersapp.Fragment.Tution_Classes;
import com.designtech9studio.puntersapp.Helpers.DataBaseHelper;
import com.designtech9studio.puntersapp.Helpers.IntentsTags;
import com.designtech9studio.puntersapp.Helpers.LocalDatabaseHelper;
import com.designtech9studio.puntersapp.Model.BannerModel;
import com.designtech9studio.puntersapp.Model.CatSubChildModel;
import com.designtech9studio.puntersapp.Model.SubCategoryModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.designtech9studio.puntersapp.R;
import com.google.android.material.tabs.TabLayout;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SubCategoryActivity extends AppCompatActivity implements ChildCatAdapter.MyCallback {

    private Toolbar toolbar;
    private TabLayout tabLayout;

    List<SubCategoryModel> sub_cat_nameArray;
    List<ImageView> imageViewList = new ArrayList<>();
    List<CatSubChildModel> catSubChildModels;
    RecyclerView childRecyclerView, subCatImageRecyceler;
    TextView count_box_tv, totalAmount_tv;
    String id, subcatId;
    LocalDatabaseHelper localDatabaseHelper;
    ChildCatAdapter childCatAdapter;
    TextView childCatContinue_tv;
    List<String> imageUrl = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        setTitle("Sub Category");

        subCatImageRecyceler = findViewById(R.id.subCatImageRecyceler);
        childRecyclerView = findViewById(R.id.childRecyclerView);
        count_box_tv = findViewById(R.id.count_box);
        totalAmount_tv = findViewById(R.id.totalAmount);
        childCatContinue_tv = findViewById(R.id.childCatContinue_tv);
        tabLayout =  findViewById(R.id.tabs);

        localDatabaseHelper = new LocalDatabaseHelper(getApplicationContext());
        setCountBoxAndTotal();
        toolbar = findViewById(R.id.subCategorytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        subCatImageRecyceler.setLayoutManager(llm);

        final Intent intent = getIntent();
         id = intent.getStringExtra(IntentsTags.CAT_ID);
         subcatId = intent.getStringExtra(IntentsTags.SUB_CAT_ID);

        Log.i("Intent id", id);

        //DataBaseHelper dataBaseHelper = new DataBaseHelper();
       /* sub_cat_nameArray = dataBaseHelper.getSubCategoryData(id);

        if (subcatId!=null) {
            SubCategoryModel j=null;
            for (SubCategoryModel i:sub_cat_nameArray) {
                if (i.getSubCatId() == Integer.valueOf(subcatId)){
                    j = i;
                }
            }
            if (j!=null) {
                sub_cat_nameArray.remove(j);
                sub_cat_nameArray.add(0, j);
            }

        }*/



        SyncData orderData = new SyncData();
        orderData.execute("");


        /*loadDataInTabLayout();*/

        /*LinearLayout imageLayout = findViewById(R.id.subCatImage_linerLayout);
        final float scale = getResources().getDisplayMetrics().density;
        int dpWidthInPx  = (int) (300 * scale);
        int dpHeightInPx = (int) (150 * scale);
        for (int i=0;i<sub_cat_nameArray.size();i++) {



            ImageView imageView = new ImageView(getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpWidthInPx, dpHeightInPx);
            params.setMargins(0,0, 4, 0);
            imageView.setLayoutParams(params);
            Glide.with(getApplicationContext())
                    .load(sub_cat_nameArray.get(i).getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            imageLayout.addView(imageView);
        }*/

        childCatContinue_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemCount = Integer.valueOf(count_box_tv.getText().toString());
                if (itemCount == 0) {
                    Toast.makeText(SubCategoryActivity.this, "Please! Add Some Item In CART", Toast.LENGTH_LONG).show();

                }else {
                    Intent intent1 = new Intent(getApplicationContext(), CheckOutActivity.class);
                    startActivity(intent1);
                }

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

    @Override
    public void onItemClicked() {
        setCountBoxAndTotal();
    }

    private class SyncData extends AsyncTask<String, String, String >
    {

        ProgressDialog progress;
        protected void onPreExecute(){
            progress = new ProgressDialog(SubCategoryActivity.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait..");
            progress.setIndeterminate(true);
            progress.show();
        }

        protected String doInBackground(String... strings){
            try{
                DataBaseHelper dataBaseHelper = new DataBaseHelper();

                sub_cat_nameArray = dataBaseHelper.getSubCategoryData(id);

                if (subcatId!=null) {
                    SubCategoryModel j=null;
                    for (SubCategoryModel i:sub_cat_nameArray) {
                        if (i.getSubCatId() == Integer.valueOf(subcatId)){
                            j = i;
                        }
                    }
                    if (j!=null) {
                        sub_cat_nameArray.remove(j);
                        sub_cat_nameArray.add(0, j);
                    }

                }

                 imageUrl = BannerModel.getBannerImageList(dataBaseHelper.getBannerData());


                catSubChildModels = dataBaseHelper.getCatSubChildData(id);

                System.out.println("LOADING CART DATA");
                List<CatSubChildModel> cartData = localDatabaseHelper.fetchCartData();

                /*Loading cart data in adapter*/
                for (CatSubChildModel i : cartData) {
                    for (CatSubChildModel j: catSubChildModels) {
                        if (i.getSubChildId() == j.getSubChildId()) {
                            j.setQty(i.getQty());
                            break;
                        }
                    }
                }


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

            subCatImageRecyceler.setAdapter(imageAdapter);
            if (catSubChildModels.size()>0)
                loadSubSubCategory(sub_cat_nameArray.get(0).getSubCatId());
            else
                Toast.makeText(SubCategoryActivity.this, "Sorry, No Item to display", Toast.LENGTH_SHORT).show();

        }
    }


    private void loadDataInTabLayout() {

        for (int i = 0; i < sub_cat_nameArray.size(); i++) {
            SubCategoryModel model = sub_cat_nameArray.get(i);
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(model.getSubcategoryName());

            if (subcatId!=null && Integer.valueOf(subcatId) == model.getSubCatId())
                tab.select();

            tab.setTag(String.valueOf(model.getSubCatId()));
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
                System.out.println("Tab Selected SubCatId " + tab.getTag().toString());
                loadSubSubCategory(Integer.valueOf(tab.getTag().toString()));
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

    private void loadSubSubCategory(int subCatId) {

        List<CatSubChildModel> filteredList = new ArrayList<>();
        //filteredList.addAll(catSubChildModels);
        for (CatSubChildModel i: catSubChildModels) {
            if (i.getSubCatId() == subCatId) filteredList.add(i);
        }
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        childRecyclerView.setLayoutManager(llm);
        childCatAdapter = new ChildCatAdapter(filteredList, this);
        childCatAdapter.setOnItemClickListener(this);
        childRecyclerView.setAdapter(childCatAdapter);

        if (filteredList.size() == 0) {
            Toast.makeText(this, "Sorry, No Item To Display", Toast.LENGTH_SHORT).show();
        }


    }

    /*private void setupViewPager() {
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
    }*/
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
    }

    public void setCountBoxAndTotal() {
        int count = localDatabaseHelper.getTotalItemsInCart();
        int amount = localDatabaseHelper.getGrandTotal();
        count_box_tv.setText(String.valueOf(count));
        totalAmount_tv.setText(Html.fromHtml("&#8377") +" "  + String.valueOf(amount));
        if (childCatAdapter!=null)
        childCatAdapter.notifyDataSetChanged();


    }


    @Override
    protected void onRestart() {
        super.onRestart();

        List<CatSubChildModel> cartData = localDatabaseHelper.fetchCartData();

        /*setting child aty to 0*/
        for(CatSubChildModel i: catSubChildModels){
            i.setQty(0);
        }

        /*Loading cart data in adapter*/
        int id[] = new int[cartData.size()];
        int index = 0;
        for (CatSubChildModel i : cartData) {
            id[index] = i.getSubChildId();
            index++;

            for (CatSubChildModel j: catSubChildModels) {
                if (i.getSubChildId() == j.getSubChildId()) {
                    j.setQty(i.getQty());
                    break;
                }
            }
        }
        /*for (CatSubChildModel j: catSubChildModels) {
            boolean flag = false;
            for (int i=0;i<index; i++) {
                if (j.getSubChildId() == id[i]) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                j.setQty(0);
            }
        }*/


        loadSubSubCategory(sub_cat_nameArray.get(0).getSubCatId());

        childCatAdapter.notifyDataSetChanged();
        Log.i("Debug", "BackAction");
        setCountBoxAndTotal();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
