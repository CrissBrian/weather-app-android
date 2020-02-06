package com.example.weatherapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private String title[] = {"TODAY", "WEEKLY", "PHOTOS"};
    private String weather_data, city_name;
    public ViewPagerAdapter(FragmentManager manager, String weather, String city) {
        super(manager);
        weather_data = weather;
        city_name = city;
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = TodayFragment.getInstance(position, weather_data);
                break;
            case 1:
                fragment = TabFragment.getInstance(position, weather_data);
                break;
            case 2:
                fragment = PhotoFragment.getInstance(position, city_name);
                break;
        }
        return fragment;
//        return TabFragment.getInstance(position);
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
