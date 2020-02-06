package com.example.weatherapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Set;

public class PlansPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    String lat, lng;
    Set<String> city_set;
    String[] cityArray;
    public PlansPagerAdapter(FragmentManager fm, int NumOfTabs, Set<String> city_set) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.city_set = city_set;
        String[] myArray = new String[city_set.size()];
        city_set.toArray(myArray);
        this.cityArray = myArray;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = DynamicFragment.newInstance(position,"");
        } else {
            fragment = DynamicFragment.newInstance(position, cityArray[position-1]);
        }
        return fragment;
    }

//    public void removeTabPage(int position) {
//        if (!tabItems.isEmpty() && position<tabItems.size()) {
//            tabItems.remove(position);
//            notifyDataSetChanged();
//        }
//    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}