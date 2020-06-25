package com.designtech9studio.puntersapp.Activity;

import android.os.Bundle;

import com.designtech9studio.puntersapp.Fragment.Appliance_Repair;
import com.designtech9studio.puntersapp.Fragment.Bleach_Fragment;
import com.designtech9studio.puntersapp.Fragment.Brazilian_Wax_Fragment;
import com.designtech9studio.puntersapp.Fragment.Detan_Fragment;
import com.designtech9studio.puntersapp.Fragment.Facial_Fragment;
import com.designtech9studio.puntersapp.Fragment.Hair_Fragment;
import com.designtech9studio.puntersapp.Fragment.HomeDesign;
import com.designtech9studio.puntersapp.Fragment.Hot_Cold_Waxing_Fragment;
import com.designtech9studio.puntersapp.Fragment.HouseCleaning;
import com.designtech9studio.puntersapp.Fragment.Manicure_Fragment;
import com.designtech9studio.puntersapp.Fragment.Other_Services;
import com.designtech9studio.puntersapp.Fragment.Pedicure_Fragment;
import com.designtech9studio.puntersapp.Fragment.Salon_At_Home;
import com.designtech9studio.puntersapp.Fragment.Shifting_Home;
import com.designtech9studio.puntersapp.Fragment.Spa_At_Home;
import com.designtech9studio.puntersapp.Fragment.Tution_Classes;
import com.designtech9studio.puntersapp.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class Salon_Women extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allservice);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Facial_Fragment(), "Facial");
        adapter.addFrag(new Hair_Fragment(), "Hair");
        adapter.addFrag(new Manicure_Fragment(), "Manicure");
        adapter.addFrag(new Pedicure_Fragment(), "Padicure");
        adapter.addFrag(new Bleach_Fragment(), "Bleach");
        adapter.addFrag(new Detan_Fragment(), "Detan");
        adapter.addFrag(new Hot_Cold_Waxing_Fragment(), "Hot And Cold Waxing");
        adapter.addFrag(new Brazilian_Wax_Fragment(), "Brazilian Wax");
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
    }
}
