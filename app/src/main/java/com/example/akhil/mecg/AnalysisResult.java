package com.example.akhil.mecg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * Created by Akhil on 29-04-2016 for Mecg
 */
public class AnalysisResult extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analysereslayout);
        Intent i = getIntent();
        TextView heartRate = (TextView) findViewById(R.id.txtHeart);
        TextView std = (TextView) findViewById(R.id.txtStd);
        TextView variance = (TextView) findViewById(R.id.txtVar);
        TextView cv = (TextView) findViewById(R.id.txtCv);
        TextView stddiff = (TextView) findViewById(R.id.txtStdDiff);
        TextView rmsdiff = (TextView) findViewById(R.id.txtRmsDiff);
        String res = i.getStringExtra("result");
        try {
            JSONObject obj = new JSONObject(res);
            heartRate.setText(""+obj.getDouble("Heart Rate"));
            std.setText(""+obj.getDouble("std_dev"));
            variance.setText(""+obj.getDouble("variance"));
            cv.setText("\n"+obj.getDouble("cv"));
            stddiff.setText("\n"+obj.getDouble("std_dev_diff"));
            rmsdiff.setText("\n"+obj.getDouble("rms_diff"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
