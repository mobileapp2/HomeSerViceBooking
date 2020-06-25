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

public class List_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    // Array of strings for ListView Title
    String[] listviewTitle = new String[]{
            "Facial", "Hair", "Manicure", "Pedicure",
            "Bleach", "Detan", "Hot Or Cold Waxing", "Brazilian Wax", "Threading"
    };


    int[] listviewImage = new int[]{
            R.drawable.facial_women1, R.drawable.hair_women1, R.drawable.manicure_women1, R.drawable.padicure_women1,
            R.drawable.bleach_women1, R.drawable.detan_women1, R.drawable.hot_waxing1, R.drawable.brazilian_wax1,R.drawable.threading1,
    };

    String[] listviewShortDescription = new String[]{
            "Description", "Description", "Description", "Description",
            "Description", "Description", "Description", "Description","Description",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 9; i++) {
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