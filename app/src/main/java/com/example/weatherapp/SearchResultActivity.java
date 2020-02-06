package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

public class SearchResultActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.weatherapp.MESSAGE";
    private static final int REQUEST_LOCATION = 1;

    private TextView temperatureField, summaryField, locationField;
    private TextView humidityField, windspeedField, visibilityField, pressureField;
    private ImageView iconField, favorField;
    String lattitude, longitude;
    String cityName, stateName, countryName;
    String wholeName;
    JSONObject weather;
    String temperature;
//    Dictionary<String, String> dict = new Hashtable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Intent intent = getIntent();
        wholeName = intent.getStringExtra("whole_name");
        lattitude = intent.getStringExtra("lat");
        longitude = intent.getStringExtra("lng");
        cityName  = intent.getStringExtra("city");

        temperatureField = findViewById(R.id.textView_T);
        summaryField = findViewById(R.id.textView_S);
        locationField = findViewById(R.id.textView_L);
        humidityField = findViewById(R.id.textView_H);
        windspeedField = findViewById(R.id.textView_W);
        visibilityField = findViewById(R.id.textView_V);
        pressureField = findViewById(R.id.textView_P);
        iconField = findViewById(R.id.imageView_icon);

        favorField = findViewById(R.id.imageView_favor);
        getDefaultFavorIcon();

        drawCard(lattitude, longitude);

        Toolbar myChildToolbar = findViewById(R.id.main_toolbar);
        // set my title!
        myChildToolbar.setTitle(wholeName);
        setSupportActionBar(myChildToolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

    }

    public void getDefaultFavorIcon() {
        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
//        sharedPref.edit().clear().apply();
        HashSet<String> default_set = new HashSet<String>();
        Set<String> city_set = sharedPref.getStringSet("CITY_SET", default_set);
        if (city_set.contains(wholeName)) {
            favorField.setImageResource(R.drawable.del_favor);
        } else {
            favorField.setImageResource(R.drawable.add_favor);
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

//                            wholeName = String.format("%s, %s, %s", cityName, stateName, countryName);
                            locationField.setText(wholeName);
                            iconField.setImageResource(getResources().getIdentifier(icon, "drawable", getPackageName()));
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

                                TextView dateField = findViewById(getResources().getIdentifier("textView_D" + i, "id", getPackageName()));
                                TextView lowwField = findViewById(getResources().getIdentifier("textView_L" + i, "id", getPackageName()));
                                TextView highField = findViewById(getResources().getIdentifier("textView_H" + i, "id", getPackageName()));
                                ImageView smallIconField = findViewById(getResources().getIdentifier("imageView_S" + i, "id", getPackageName()));
//                                TextView dateField = findViewById(getResources().getIdentifier("textView_D" + i, "id", getPackageName()));
                                dateField.setText(date);
                                lowwField.setText(loww);
                                highField.setText(high);
                                smallIconField.setImageResource(getResources().getIdentifier(small_icon, "drawable", getPackageName()));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
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
        MySingleton.getInstance(SearchResultActivity.this).addToRequestQueue(jsonObjectRequest);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void clickAddFavor(View view) {
        // Do something in response to button
        Context context = view.getContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
//        sharedPref.edit().clear().apply();
        HashSet<String> default_set = new HashSet<String>();
        Set<String> city_set = sharedPref.getStringSet("CITY_SET", default_set);
        System.out.println("aaaaaaaaaa"+ city_set);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (city_set.contains(wholeName)) {
            city_set.remove(wholeName);
            favorField.setImageResource(R.drawable.add_favor);
            Toast.makeText(SearchResultActivity.this, wholeName + " was removed from favorites", Toast.LENGTH_LONG).show();
        } else {
            city_set.add(wholeName);
            favorField.setImageResource(R.drawable.del_favor);
            Toast.makeText(SearchResultActivity.this, wholeName + " was added to favorites", Toast.LENGTH_LONG).show();
        }
        System.out.println("aaaaaaaaaa"+ city_set);
        editor.putStringSet("CITY_SET", city_set);
        editor.apply();
    }

    // test case
    public void clickCard0(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, SearchDetailActivity.class);
        intent.putExtra(EXTRA_MESSAGE, weather.toString());
        intent.putExtra("whole_name", wholeName);
        intent.putExtra("city", cityName);
        intent.putExtra("temperature", temperature);
        startActivity(intent);
    }
}


