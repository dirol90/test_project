package com.tsymbalyukstudio.test_project;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static int currentPos;
    public ViewPager mViewPager;
    public List<CustomFragment> customFragments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.main_layout);
        customFragments.add(new CustomFragment(customFragments.size() + 1, mViewPager, customFragments));
        mViewPager.setAdapter(new SamplePagerAdapter(getSupportFragmentManager()));

    }

    public class SamplePagerAdapter extends FragmentPagerAdapter {

        SamplePagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return customFragments.get(position);
        }

        @Override
        public int getCount() {
            return customFragments.size();
        }

        @Override
        public int getItemPosition(Object object) {
            int index = customFragments.indexOf (object);

            if (index == -1)
                return POSITION_NONE;
            else
                return index;
        }
    }


    @Override
    public void onNewIntent(Intent intent) {
        int extraInt = intent.getIntExtra("position", 0);
        mViewPager.setCurrentItem(extraInt-1, true);
        System.out.println(extraInt);
    }
}
