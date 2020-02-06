package com.example.weatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Response;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.weatherapp.MESSAGE";
    private static final int REQUEST_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView temperatureField, summaryField, locationField;
    private TextView humidityField, windspeedField, visibilityField, pressureField;
    private ImageView iconField;
    String lattitude, longitude;
    String cityName, stateName, countryName;
    String wholeName;
    JSONObject weather;
    String temperature;
//    private static RequestQueue queue;
//    purple BB86FC
    Dictionary<String, String> dict = new Hashtable();

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//        setProgressBarIndeterminateVisibility(true);


        spinner = (ProgressBar)findViewById(R.id.progressBar_cyclic);
        spinner.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.GONE);

        temperatureField = findViewById(R.id.textView_T);
        summaryField = findViewById(R.id.textView_S);
        locationField = findViewById(R.id.textView_L);
        humidityField = findViewById(R.id.textView_H);
        windspeedField = findViewById(R.id.textView_W);
        visibilityField = findViewById(R.id.textView_V);
        pressureField = findViewById(R.id.textView_P);
        iconField = findViewById(R.id.imageView_icon);

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

//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        fusedLocationClient.getLastLocation()
//            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//                // Got last known location. In some rare situations this can be null.
//                if (location != null) {
//                    getCityInfo(location);
////                            temperatureField.setText(String.format("%s %s %s, %s, %s", lattitude, longitude, cityName, stateName, countryName));
////                    drawCard(lattitude, longitude);
//                }
//                }
//            });

        Context context = MainActivity.this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        HashSet<String> default_set = new HashSet<String>();
        Set<String> city_set = sharedPref.getStringSet("CITY_SET", default_set);

//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putStringSet("CITY_SET", city_set);
//        editor.apply();

        System.out.println("aaaaaaaaaa"+ city_set);

//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), message, city_name);

        viewPager = findViewById(R.id.main_viewpager);
        tabLayout = findViewById(R.id.main_tabs);

        for (int k = 0; k < city_set.size() + 1; k++) {
            tabLayout.addTab(tabLayout.newTab().setText(""));
        }
        PlansPagerAdapter adapter = new PlansPagerAdapter
                (getSupportFragmentManager(), city_set.size() + 1, city_set);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//Bonus Code : If your tab layout has more than 2 tabs then tab will scroll other wise they will take whole width of the screen
        if (tabLayout.getTabCount() == 2) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }

    }


    private void makeApiCall(String text) {
        ApiCall.make(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray array = responseObject.getJSONArray("predictions");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        stringList.add(row.getString("description"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the search menu action bar.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        // Get the search menu.
        MenuItem searchMenu = menu.findItem(R.id.action_search);
        // Get SearchView object.
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);
        searchView.setQueryHint(Html.fromHtml("<font color = #999999>" + "Search..." + "</font>"));
        // Disable Icon
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(false);
        ImageView searchViewIcon = searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        ViewGroup linearLayoutSearchView =(ViewGroup) searchViewIcon.getParent();
        linearLayoutSearchView.removeView(searchViewIcon);
        // Get SearchView autocomplete object.
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setBackgroundColor(Color.BLACK);
        searchAutoComplete.setTextColor(Color.WHITE);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);

        // auto Adapter!!!
        autoSuggestAdapter = new AutoSuggestAdapter(this,
                android.R.layout.simple_dropdown_item_1line);
        searchAutoComplete.setThreshold(1);
        searchAutoComplete.setAdapter(autoSuggestAdapter);
        searchAutoComplete.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        searchAutoComplete.setText(autoSuggestAdapter.getObject(position));
                    }
                });

        searchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(searchAutoComplete.getText())) {
                        makeApiCall(searchAutoComplete.getText().toString());
                    }
                }
                return false;
            }
        });


        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText("" + queryString);
//                Toast.makeText(MainActivity.this, "you clicked " + queryString, Toast.LENGTH_LONG).show();
            }
        });

        // Below event is triggered when submit search query.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                final String[] citys = query.split(",",5);
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
                                Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                                intent.putExtra("lat", lat);
                                intent.putExtra("lng", lng);
                                intent.putExtra("whole_name", query);
                                intent.putExtra("city", citys[0]);
                                startActivity(intent);

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
                MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        return super.onCreateOptionsMenu(menu);
    }

    public void getCityInfo(Location location) {
        double lat = (location.getLatitude());
        double lng = (location.getLongitude());
        lattitude = String.valueOf(lat);
        longitude =  String.valueOf(lng);
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
                cityName = addresses.get(0).getLocality();
                stateName = addresses.get(0).getAdminArea();
                countryName = addresses.get(0).getCountryCode();
                if (countryName.equals("US")) { countryName = "USA"; }
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
                    stateName = dict.get(stateName);

                    wholeName = String.format("%s, %s, %s", cityName, stateName, countryName);
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
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);
    }



    // test case
//    public void clickCard0(View view) {
//        // Do something in response to button
//        Intent intent = new Intent(this, DetailedActivity.class);
//        intent.putExtra(EXTRA_MESSAGE, weather.toString());
//        intent.putExtra("whole_name", wholeName);
//        intent.putExtra("city", cityName);
//        intent.putExtra("temperature", temperature);
//        startActivity(intent);
//    }
}
