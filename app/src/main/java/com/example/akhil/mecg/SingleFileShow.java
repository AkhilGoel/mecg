package com.example.akhil.mecg;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Akhil on 23-03-2016.
 */
public class SingleFileShow extends Activity {
    String name;
    String centre;
    String id;
    List<ParseObject> ob;
    Button btnDown,btnPl;
    String[] signalDataArr;
    ParseFile file;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singlefileview);
        Intent i = getIntent();
        name = i.getStringExtra("name");
        centre = i.getStringExtra("centre");
        id = i.getStringExtra("fileid");
        TextView txtname = (TextView) findViewById(R.id.txtFileName);
        TextView txtcentre = (TextView) findViewById(R.id.txtCentreName);
        txtname.setText(name);
        txtcentre.setText(centre);
        btnDown = (Button) findViewById(R.id.btnDownload);
        btnPl = (Button) findViewById(R.id.btnPlot);
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Files");
                query.whereEqualTo("objectId", id);
                try {
                    ob = query.find();
                    for (ParseObject obj : ob)
                        file = (ParseFile) obj.get("Data");
                    String url = file.getUrl();
                    new DownloadFileAsync().execute(url);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        btnPl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                }
                            });

                            try {
                                Thread.sleep(35);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                }).start();

                 plotData();
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Downloading file..");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;

            try {

                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream("/sdcard/download/" + name);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
            }
            return null;

        }

        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
        }
    }

    public void plotData() {
//        String inputPath = "mnt/sdcard/download/" + name;
//        AssetManager assetManager = getAssets();
//        InputStream input;
        try {
//            input = assetManager.open(inputPath);
//            int size = input.available();
//            byte[] buffer = new byte[size];
//            input.read(buffer);
//            input.close();
//            String text = new String(buffer);
            Intent intent = new Intent(SingleFileShow.this,PlotChart.class);
            intent.putExtra("FileName",name);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

