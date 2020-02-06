package com.example.weatherapp;

public class PhotoItem {
    private int mImageResource;
    private String mText;


    public PhotoItem(int imageResource, String text) {
        mImageResource = imageResource;
        mText = text;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getText() {
        return mText;
    }

}
