package com.example.weatherapp;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TabFragment extends Fragment {

    int position;
    private TextView summaryField;
    private ImageView iconField;
    LineChart mylinechart;



    public static Fragment getInstance(int position, String weather) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        bundle.putString("weather", weather);
        TabFragment tabFragment = new TabFragment();
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
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String weather = getArguments().getString("weather");

        summaryField = view.findViewById(R.id.textView_WC);
        iconField = view.findViewById(R.id.imageView_WC);

        ///// chart set up /////
        mylinechart = view.findViewById(R.id.line_chart);
        mylinechart.setNoDataText("");
        mylinechart.setDrawGridBackground(false);
        // set font info
        Legend legend = mylinechart.getLegend();
        legend.setTextColor(Color.WHITE);
        legend.setTextSize(16);
        ArrayList<Entry> data_loww = new ArrayList<Entry>();
        ArrayList<Entry> data_high = new ArrayList<Entry>();
        ArrayList<String> xLabel = new ArrayList<>();

        try { JSONObject weather_data = new JSONObject(weather);
            JSONObject daily = weather_data.getJSONObject("daily");
            String icon = daily.getString("icon").replace('-', '_');
            String summary = daily.getString("summary");
            summaryField.setText(summary);
            iconField.setImageResource(getResources().getIdentifier(icon, "drawable", getActivity().getPackageName()));

            JSONArray daily_data = daily.getJSONArray("data");

            for (int i = 0; i < 8; i++) {
                JSONObject temp = daily_data.getJSONObject(i);
                int loww = temp.getInt("temperatureLow");
                int high = temp.getInt("temperatureHigh");
                data_loww.add(new Entry(i,loww));
                data_high.add(new Entry(i,high));
                xLabel.add(Integer.toString(i));
            }

        } catch (JSONException e) { e.printStackTrace(); }

        mylinechart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabel));
        XAxis xAxis = mylinechart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = mylinechart.getAxisLeft();
        YAxis rightAxis = mylinechart.getAxisRight();
        leftAxis.setTextColor(Color.WHITE);
        rightAxis.setTextColor(Color.WHITE);


        LineDataSet temp_loww = new LineDataSet(data_loww, "Minimum Temperature");
        LineDataSet temp_high = new LineDataSet(data_high, "Maximum Temperature");
        temp_loww.setColor(0xFF986ED3); //purple
        temp_high.setColor(0xFFFF9800); //orange
        ArrayList<ILineDataSet> data_sets = new ArrayList<>();
        data_sets.add(temp_loww);
        data_sets.add(temp_high);
        LineData data = new LineData(data_sets);
        mylinechart.setData(data);
        mylinechart.invalidate();
    }

//    private ArrayList<Entry> dataValues(){
//        ArrayList<Entry> dataVals = new ArrayList<Entry>();
//        dataVals.add(new Entry(0,20));
//        dataVals.add(new Entry(2,28));
//        dataVals.add(new Entry(3,34));
//        dataVals.add(new Entry(5,24));
//        dataVals.add(new Entry(7,34));
//        return dataVals;
//    }
//    private ArrayList<Entry> dataValues2(){
//        ArrayList<Entry> dataVals = new ArrayList<Entry>();
//        dataVals.add(new Entry(0,10));
//        dataVals.add(new Entry(2,12));
//        dataVals.add(new Entry(3,14));
//        dataVals.add(new Entry(5,14));
//        dataVals.add(new Entry(7,14));
//        return dataVals;
//    }
}

