package com.example.weatherapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class TodayFragment extends Fragment {
    int position;

    private TextView windspeedField, pressureField, precipitationField, temperatureField, humidityField, visibilityField, cloudField, ozoneField, icontextField;
    private ImageView iconField;

    public static Fragment getInstance(int position, String weather) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        bundle.putString("weather", weather);
        TodayFragment tabFragment = new TodayFragment();
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
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String weather = getArguments().getString("weather");

        windspeedField = view.findViewById(R.id.textView_TW);
        pressureField = view.findViewById(R.id.textView_TP);
        precipitationField = view.findViewById(R.id.textView_TPr);
        temperatureField = view.findViewById(R.id.textView_TT);
        iconField = view.findViewById(R.id.imageView_TI);
        icontextField = view.findViewById(R.id.textView_TI);
        humidityField = view.findViewById(R.id.textView_TH);
        visibilityField = view.findViewById(R.id.textView_TV);
        cloudField = view.findViewById(R.id.textView_TC);
        ozoneField = view.findViewById(R.id.textView_TO);


        try { JSONObject weather_data = new JSONObject(weather);
            JSONObject current = weather_data.getJSONObject("currently");
            String temperature = Integer.toString(current.getInt("temperature"));
            String icon = current.getString("icon").replace('-', '_');

            String humidity = Integer.toString((int)(current.getDouble("humidity") * 100));
            String precipitation = String.format("%.2f", current.getDouble("precipIntensity"));
            String windspeed = String.format("%.2f", current.getDouble("windSpeed"));
            String visibility = String.format("%.2f", current.getDouble("visibility"));
            String pressure = String.format("%.2f", current.getDouble("pressure"));
            String cloud = Integer.toString((int)(current.getDouble("cloudCover") * 100));

            icontextField.setText(changeIcon(icon));
            windspeedField.setText(String.format("%s mph", windspeed));
            pressureField.setText(String.format("%s mb", pressure));
            precipitationField.setText(String.format("%s mmph", precipitation));
            temperatureField.setText(String.format("%s℉", temperature));
            humidityField.setText(String.format("%s%%", humidity));
            visibilityField.setText(String.format("%s km", visibility));
            cloudField.setText(String.format("%s%%", cloud));
            ozoneField.setText(String.format("%s DU", visibility));
            iconField.setImageResource(getResources().getIdentifier(icon, "drawable", getActivity().getPackageName()));


        } catch (JSONException e) { e.printStackTrace(); }
    }

    private String changeIcon(String icon) {
        icon = icon.replace('_', ' ');
        String[] icons = icon.split(" ", 5);
        if (icons[0].equals("partly")) {
            return icons[1] + ' ' + icons[2];
        } else if (icons[0].equals("clear")) {
            return icons[0] + ' ' + icons[1];
        } else {
            return icons[0];
        }
    }

}

