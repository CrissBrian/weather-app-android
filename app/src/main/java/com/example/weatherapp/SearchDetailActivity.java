package com.example.weatherapp;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class SearchDetailActivity extends AppCompatActivity {
    //    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String whole_name, temperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        whole_name = intent.getStringExtra("whole_name");
        String city_name = intent.getStringExtra("city");
        temperature = intent.getStringExtra("temperature");



        Toolbar myChildToolbar = findViewById(R.id.main_toolbar);
        // set my title!
        myChildToolbar.setTitle(whole_name);
        setSupportActionBar(myChildToolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), message, city_name);
        viewPager.setAdapter(adapter);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.today);
        tabLayout.getTabAt(1).setIcon(R.drawable.weekly);
        tabLayout.getTabAt(2).setIcon(R.drawable.photos);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_twitter:
//                Context context = getApplicationContext();
//                CharSequence text = "Hello toast!";
//                int duration = Toast.LENGTH_SHORT;
//                Toast toast = Toast.makeText(context, text, duration);
//                toast.show();
                String urlStr = "https://twitter.com/intent/tweet?text=";
                String query = "Check Out " + whole_name + "'s Weather! It is " + temperature + "℉! #CSCI571WeatherSearch";
                query = URLEncoder.encode(query);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlStr + query));
                startActivity(browserIntent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


}
