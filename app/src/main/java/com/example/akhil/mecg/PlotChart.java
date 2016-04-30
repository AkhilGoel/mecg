package com.example.akhil.mecg;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Akhil on 31-03-2016 for Mecg
 */
public class PlotChart extends Activity implements OnChartValueSelectedListener{

    private RelativeLayout mainLayout;
    int FLAG = 0,STOP=0;
    LineChart mChart;
    File fl;
    Scanner scan;
    ArrayList<Double> list = new ArrayList<>();
    Button btnStart,btnPause;
    Thread run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plot_mp);
        final DatabaseHandler db = new DatabaseHandler(this);
        Intent intent = getIntent();
        String name = intent.getStringExtra("FileName");
        fl = new File("/storage/sdcard0/download/"+name );
        try {
            scan = new Scanner(fl);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(int i=0;i<2100;i++) {
            if(scan.hasNextDouble())
                list.add(scan.nextDouble());
        }
        //Call to get max peaks
        final ArrayList<Integer> max_pos = getPeaks();
        final TextView hr = (TextView) findViewById(R.id.txtHr);
        //call for heart rate
        final Integer heartR = getHeartRate(max_pos);
        int max=max_pos.get(0);
        for(int i=0;i<max_pos.size();i++){
            if(list.get(max_pos.get(i))>list.get(max))
                max = max_pos.get(i);
        }
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        mChart = (LineChart) findViewById(R.id.chart);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDescription("");
        mChart.setNoDataTextDescription("No Data Available");
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setPinchZoom(true);
        mChart.setBackgroundColor(Color.LTGRAY);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);

        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);
        l.setEnabled(false);

        XAxis x1 = mChart.getXAxis();
        x1.setTextColor(Color.WHITE);
        x1.setAvoidFirstLastClipping(true);
        x1.setDrawAxisLine(false);


        YAxis y1 = mChart.getAxisLeft();
        y1.setTextColor(Color.WHITE);
        y1.setAxisMaxValue((float) (list.get(max) + 0.3));
        y1.setAxisMinValue(-1f);
        y1.setDrawGridLines(true);

        YAxis y2 = mChart.getAxisRight();
        y2.setEnabled(false);
        btnStart = (Button)findViewById(R.id.btnStart);
        btnPause = (Button)findViewById(R.id.btnPause);

        btnPause.setEnabled(false);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                STOP=0;
                btnPause.setEnabled(true);
                btnStart.setEnabled(false);
                    run = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            for (int i = 0; i < list.size(); i++) {
                                if(FLAG == 0) {
                                    final int finalI = i;
                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            double value = list.get(finalI);
                                            addEntry((float) value, max_pos);
                                        }
                                    });

                                    try {
                                        Thread.sleep(35);
                                    } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                                else{
                                    if(STOP==0)
                                        i--;
                                    else
                                        break;
                                }
                            }
                        }
                    });
                    run.start();
                hr.setText(String.valueOf(heartR));
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FLAG==1){
                    FLAG=0;
                    btnPause.setText("PAUSE");
                }
                else{
                    FLAG=1;
                    btnPause.setText("RESUME");
                }
            }
        });



    }
    void addEntry(float val,ArrayList<Integer> max_pos){
        LineData data = mChart.getData();
        if(data != null)
        {
            LineDataSet set = (LineDataSet) data.getDataSetByIndex(0);
            if(set==null){
                set = createSet();
                data.addDataSet(set);
            }
            data.addXValue("");
            data.addEntry(new Entry(val, set.getEntryCount()), 0);
            int[] color = new int[set.getEntryCount()];
            for(int i=0;i<color.length;i++){
                if(max_pos.contains(i))
                    color[i] = Color.RED;
                else
                    color[i] = ColorTemplate.getHoloBlue();
            }
            set.setColors(color);
            mChart.notifyDataSetChanged();
            mChart.setVisibleXRange(0,250);
            mChart.moveViewToX(data.getXValCount()-251);
        }
    }
    LineDataSet createSet(){
        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(ColorTemplate.getHoloBlue());
        set.setDrawCircles(false);
        set.setLineWidth(4f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(ColorTemplate.getHoloBlue());
        set.setValueTextColor(ColorTemplate.getHoloBlue());
        set.setValueTextSize(4f);
        set.setDrawValues(false);
        return set;
    }
    ArrayList<Integer> getPeaks() {
        ArrayList<Integer> max_pos = new ArrayList<>();
        int SIZE = list.size();
        for (int i = 0; i < SIZE; i +=1 ) {
            if (list.get(i)>=0.5) {
                int max = i;
                i = i + 1;
                while (list.get(i) >= 0.5){
                    if (list.get(i) > list.get(max))
                        max = i;
                i = i + 1;
                }
                max_pos.add(max);
            }
        }
        return max_pos;
    }

    Integer getHeartRate(ArrayList<Integer> max_pos){
        Integer hr=0;
        int avg = 0;
        for(int i=0;i<max_pos.size()-1;i++){
            avg = avg + max_pos.get(i+1)-max_pos.get(i);
        }
        hr = avg/(max_pos.size()-1);
        hr = (30000/hr);
        return hr;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
