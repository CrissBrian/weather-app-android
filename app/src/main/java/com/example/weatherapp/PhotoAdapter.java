package com.example.weatherapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private ArrayList<PhotoItem> mPhotoList;

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
//        public TextView mTextView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView_photo);
//            mTextView = itemView.findViewById(R.id.textView_photo);
        }
    }

    public PhotoAdapter(ArrayList<PhotoItem> photoList) {
        mPhotoList = photoList;

    }
    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo, viewGroup, false);
        PhotoViewHolder pvh = new PhotoViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder photoViewHolder, int i) {
        PhotoItem currentItem = mPhotoList.get(i);
//        photoViewHolder.mImageView.setImageResource(currentItem.getImageResource());
//        photoViewHolder.mTextView.setText(currentItem.getText());
        Picasso.get().load(currentItem.getText()).into(photoViewHolder.mImageView);

    }


    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }


}
