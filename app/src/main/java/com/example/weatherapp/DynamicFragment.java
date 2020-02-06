package com.example.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

public class DynamicFragment extends Fragment{
    View view;
    public static final String EXTRA_MESSAGE = "com.example.weatherapp.MESSAGE";
    private static final int REQUEST_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView temperatureField, summaryField, locationField;
    private TextView humidityField, windspeedField, visibilityField, pressureField;
    private ImageView iconField, favorField;
    String lattitude, longitude;
    String cityName, stateName, countryName;
    String wholeName;
    JSONObject weather;
    String temperature;
    private ProgressBar spinner;
    private View card0,card1,card2;
    private TextView progress_text;

    //    private static RequestQueue queue;
//    purple BB86FC
    Dictionary<String, String> dict = new Hashtable();



    public static DynamicFragment newInstance(int val, String whole_name) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        args.putInt("position", val);

        args.putString("whole_name", whole_name);
        fragment.setArguments(args);
        return fragment;
    }

    int position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
//        OnClickFragments.registerTagFragment(view, this);

        spinner = (ProgressBar)view.findViewById(R.id.progressBar_cyclic);
        progress_text = view.findViewById(R.id.progressBar_text);
        spinner.setVisibility(View.VISIBLE);
        progress_text.setVisibility(View.VISIBLE);

        card0 = view.findViewById(R.id.card_view0);
        card1 = view.findViewById(R.id.card_view1);
        card2 = view.findViewById(R.id.scroll_view0);
        card0.setVisibility(View.GONE);
        card1.setVisibility(View.GONE);
        card2.setVisibility(View.GONE);



//        spinner.bringToFront();
//        spinner.setVisibility(View.GONE);

        View card = view.findViewById(R.id.card_view0);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailedActivity.class);
                intent.putExtra(EXTRA_MESSAGE, weather.toString());
                intent.putExtra("whole_name", wholeName);
                intent.putExtra("city", cityName);
                intent.putExtra("temperature", temperature);
                startActivity(intent);
            }
        });


        View card_favor = view.findViewById(R.id.cardview_addfavor);
        card_favor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = view.getContext();
                SharedPreferences sharedPref = context.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
//                sharedPref.edit().clear().apply();
                HashSet<String> default_set = new HashSet<String>();
                Set<String> city_set = sharedPref.getStringSet("CITY_SET", default_set);
                System.out.println("aaaaaaaaaa"+ city_set);
                SharedPreferences.Editor editor = sharedPref.edit();
//                if (city_set.contains(wholeName)) {
                city_set.remove(wholeName);
                favorField.setImageResource(R.drawable.add_favor);
                Toast.makeText(getActivity(), wholeName + " was removed from favorites", Toast.LENGTH_LONG).show();
//                } else {
//                    city_set.add(wholeName);
//                    favorField.setImageResource(R.drawable.del_favor);
//                    Toast.makeText(getActivity(), wholeName + " was added to favorites", Toast.LENGTH_LONG).show();
//                }
                editor.putStringSet("CITY_SET", city_set);
                editor.apply();


//                sharedPref.edit().clear().apply();

                System.out.println("aaaaaaaaaa"+ city_set);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });


        position = getArguments().getInt("position", 0);
        wholeName = getArguments().getString("whole_name");

        temperatureField = view.findViewById(R.id.textView_T);
        summaryField = view.findViewById(R.id.textView_S);
        locationField = view.findViewById(R.id.textView_L);
        humidityField = view.findViewById(R.id.textView_H);
        windspeedField = view.findViewById(R.id.textView_W);
        visibilityField = view.findViewById(R.id.textView_V);
        pressureField = view.findViewById(R.id.textView_P);
        iconField = view.findViewById(R.id.imageView_icon);
        favorField = view.findViewById(R.id.imageView_favor);
        getDefaultFavorIcon();


        dict.put("Alabama", "AL");
        dict.put("Alaska", "Ak");
        dict.put("Arizona","AZ");
        dict.put("Arkansas","AR");
        dict.put("California","CA");
        dict.put("Colorado","CO");
        dict.put("Connecticut","CT");
        dict.put("Delaware","DE");
        dict.put("District Of Columbia","DC");
        dict.put("Florida","FL");
        dict.put("Georgia","GA");
        dict.put("Hawaii","HI");
        dict.put("Idaho","ID");
        dict.put("Illinois","IL");
        dict.put("Indiana","IN");
        dict.put("Iowa","IA");
        dict.put("Kansas","KS");
        dict.put("Kentucky","KY");
        dict.put("Louisiana","LA");
        dict.put("Maine","ME");
        dict.put("Maryland","MD");
        dict.put("Massachusetts","MA");
        dict.put("Michigan","MI");
        dict.put("Minnesota","MN");
        dict.put("Mississippi","MS");
        dict.put("Missouri","MO");
        dict.put("Montana","MT");
        dict.put("Nebraska","NE");
        dict.put("Nevada","NV");
        dict.put("New Hampshire","NH");
        dict.put("New Jersey","NJ");
        dict.put("New Mexico","NW");
        dict.put("New York","NY");
        dict.put("North Carolina","NC");
        dict.put("North Dakota","ND");
        dict.put("Ohio","OH");
        dict.put("Oklahoma","OK");
        dict.put("Oregon","OR");
        dict.put("Pennsylvania","PA");
        dict.put("Rhode Island","RI");
        dict.put("South Carolina","SC");
        dict.put("South Dakota","SD");
        dict.put("Tennessee","TN");
        dict.put("Texas","TX");
        dict.put("Utah","UT");
        dict.put("Vermont","VT");
        dict.put("Virginia","VA");
        dict.put("Washington","WA");
        dict.put("West Virginia","WV");
        dict.put("Wisconsin","WI");
        dict.put("Wyoming","WY");




        if (position == 0) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                getCityInfo(location);
//                            temperatureField.setText(String.format("%s %s %s, %s, %s", lattitude, longitude, cityName, stateName, countryName));
                                drawCard(lattitude, longitude);
                            }
                        }
            });
        } else {

            final String[] citys = wholeName.split(",",5);
            String url = "http://weather-angular-258405.appspot.com/geo?street=&city=" + citys[0] + "&state=";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject location = response.getJSONArray("results").getJSONObject(0)
                                        .getJSONObject("geometry").getJSONObject("location");
                                String lat = location.getString("lat");
                                String lng = location.getString("lng");
                                cityName = citys[0];
                                drawCard(lat, lng);

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
            // Access the RequestQueue through your singleton class.
            MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
        }



//        c = view.findViewById(R.id.c);
//        c.setText("" + val);
        return view;
    }

    public void getDefaultFavorIcon() {
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
//        sharedPref.edit().clear().apply();
        HashSet<String> default_set = new HashSet<String>();
        Set<String> city_set = sharedPref.getStringSet("CITY_SET", default_set);
        if (position == 0) {
            favorField.setVisibility(View.INVISIBLE);
        }
        if (city_set.contains(wholeName)) {
            favorField.setImageResource(R.drawable.del_favor);
        } else {
            favorField.setImageResource(R.drawable.add_favor);
        }
    }



    public void getCityInfo(Location location) {
        double lat = (location.getLatitude());
        double lng = (location.getLongitude());
        lattitude = String.valueOf(lat);
        longitude =  String.valueOf(lng);
        Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
                cityName = addresses.get(0).getLocality();
                stateName = addresses.get(0).getAdminArea();
                stateName = dict.get(stateName);
                countryName = addresses.get(0).getCountryCode();
                if (countryName.equals("US")) { countryName = "USA"; }
                wholeName = String.format("%s, %s, %s", cityName, stateName, countryName);
//                countryName = addresses.get(0).getCountryName();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawCard(String lat, String lng) {
        String url = "http://weather-angular-258405.appspot.com/darksky?lat=" + lat + "&lng=" + lng;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // CURRENT //
                            weather = response;
                            String timeZone = response.getString("timezone");
                            JSONObject current = response.getJSONObject("currently");
                            temperature = Integer.toString(current.getInt("temperature"));
                            String icon = current.getString("icon").replace('-', '_');
                            String summary = current.getString("summary");
                            String humidity = Integer.toString((int)(current.getDouble("humidity") * 100));
                            String windspeed = String.format("%.2f", current.getDouble("windSpeed"));
                            String visibility = String.format("%.2f", current.getDouble("visibility"));
                            String pressure = String.format("%.2f", current.getDouble("pressure"));
                            temperatureField.setText(String.format("%s℉", temperature));
                            summaryField.setText(summary);

                            locationField.setText(wholeName);
                            iconField.setImageResource(getResources().getIdentifier(icon, "drawable", getActivity().getPackageName()));
//                                                iconField.setImageResource(R.drawable.cloudy);
                            humidityField.setText(String.format("%s%%", humidity));
                            windspeedField.setText(String.format("%s mph", windspeed));
                            visibilityField.setText(String.format("%s km", visibility));
                            pressureField.setText(String.format("%s mb", pressure));

                            // DAILY //
                            JSONArray daily = response.getJSONObject("daily").getJSONArray("data");

                            for (int i = 0; i < 8; i++){
                                JSONObject temp = daily.getJSONObject(i);
                                String date = temp.getString("time");
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(new Date(Long.parseLong(date) * 1000));
                                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                                sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
                                date = sdf.format(calendar.getTime());

                                String loww = Integer.toString(temp.getInt("temperatureLow"));
                                String high = Integer.toString(temp.getInt("temperatureHigh"));
                                String small_icon = temp.getString("icon").replace('-', '_');

                                TextView dateField = view.findViewById(getResources().getIdentifier("textView_D" + i, "id", getActivity().getPackageName()));
                                TextView lowwField = view.findViewById(getResources().getIdentifier("textView_L" + i, "id", getActivity().getPackageName()));
                                TextView highField = view.findViewById(getResources().getIdentifier("textView_H" + i, "id", getActivity().getPackageName()));
                                ImageView smallIconField = view.findViewById(getResources().getIdentifier("imageView_S" + i, "id", getActivity().getPackageName()));
//                                TextView dateField = findViewById(getResources().getIdentifier("textView_D" + i, "id", getPackageName()));
                                dateField.setText(date);
                                lowwField.setText(loww);
                                highField.setText(high);
                                smallIconField.setImageResource(getResources().getIdentifier(small_icon, "drawable", getActivity().getPackageName()));
                            }
                            spinner.setVisibility(View.GONE);
                            progress_text.setVisibility(View.GONE);

                            card0.setVisibility(View.VISIBLE);
                            card1.setVisibility(View.VISIBLE);
                            card2.setVisibility(View.VISIBLE);


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
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
    }


}
