package com.example.weatherapp;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.picasso.Picasso;

import java.io.SyncFailedException;
import java.util.ArrayList;


public class PhotoFragment extends Fragment {

    int position;
    private ImageView photoField;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    public static Fragment getInstance(int position, String city) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        bundle.putString("city", city);
        PhotoFragment tabFragment = new PhotoFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("pos");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String city = getArguments().getString("city");
        String url = "http://weather-angular-258405.appspot.com/search?city=" + city;
//        final TextView field = view.findViewById(R.id.textView_photo);



        final ArrayList<PhotoItem> photoList = new ArrayList<>();
//        photoList.add(new PhotoItem(R.drawable.clear_day,"12314"));
//        photoList.add(new PhotoItem(R.drawable.clear_day,"1235132"));
//        photoList.add(new PhotoItem(R.drawable.clear_day,"12125632"));
//        photoList.add(new PhotoItem(R.drawable.clear_day,"21341234"));
        photoField = view.findViewById(R.id.imageView_photo);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray photo = response.getJSONArray("items");
                        for (int i = 0; i < 8; i++) {
                            JSONObject temp = photo.getJSONObject(i);
                            String photo_url = temp.getString("link");
                            photoList.add(new PhotoItem(R.drawable.clear_day, photo_url));
//                            System.out.println("aaaaaaaaaa"+photo_url);
//                            ImageView ivBasicImage = (ImageView) view.findViewById(R.id.ivBasicImage);
//                            Picasso.get().load(photo_url).into(photoField);
//                            field.setText(photo_link);
                        }
                        mAdapter = new PhotoAdapter(photoList);
                        recyclerView.setAdapter(mAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
             }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                System.out.println(error);
            }
         });




        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);

    }
}

