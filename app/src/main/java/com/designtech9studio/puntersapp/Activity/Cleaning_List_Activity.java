package com.designtech9studio.puntersapp.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.designtech9studio.puntersapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cleaning_List_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    // Array of strings for ListView Title
    String[] listviewTitle = new String[]{
            "Pest Control", "Home Cleaning", "Bathroom Cleaning", "Kitchen Cleaning",
            "Sofa Cleaning", "Carpet Cleaning", "Water Tank", "Car Wash And Cleaning",
    };


    int[] listviewImage = new int[]{
            R.drawable.pest_control1, R.drawable.home_clean1, R.drawable.bathroom_clean1, R.drawable.kitchen_clean1,
            R.drawable.sofa, R.drawable.carpenter_home1, R.drawable.water_tank_clean1, R.drawable.car_washing1,
    };

    String[] listviewShortDescription = new String[]{
            "Description", "Description", "Description", "Description",
            "Description", "Description", "Description", "Description",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning__list_);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 8; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", listviewTitle[i]);
            hm.put("listview_discription", listviewShortDescription[i]);
            hm.put("listview_image", Integer.toString(listviewImage[i]));
            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title", "listview_discription"};
        int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_item_short_description};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_activity, from, to);
        ListView androidListView = (ListView) findViewById(R.id.list_view);
        androidListView.setAdapter(simpleAdapter);
    }
}
