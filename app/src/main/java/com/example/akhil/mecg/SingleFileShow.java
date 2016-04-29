package com.example.akhil.mecg;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

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
    Button btnDown,btnPl,btnAnalyse;
    ParseFile file;
    ans mAns;
    DownloadFileAsync mDownload;
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
        btnAnalyse = (Button) findViewById(R.id.btnAnalyse);
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
                    mDownload = new DownloadFileAsync();
                    mDownload.execute(url);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        btnPl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plotData();
            }
        });
        btnAnalyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAns = new ans();
                mAns.execute();
            }
        });
    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SingleFileShow.this){
                @Override
                public boolean onKeyDown(int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        mDownload.cancel(true);
                        this.dismiss();
                        onPostResume();
                        return true;
                    }
                    return super.onKeyDown(keyCode, event);
                }
            };
            mProgressDialog.setTitle("Downloading File");
            mProgressDialog.setMessage("Downloading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

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
        @Override
        protected void onPostExecute(String unused) {
            mProgressDialog.dismiss();
            Toast.makeText(SingleFileShow.this,"Download Succesful",Toast.LENGTH_SHORT).show();
        }
    }

    public void plotData() {
        try {
            Intent intent = new Intent(SingleFileShow.this,PlotChart.class);
            intent.putExtra("FileName",name);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ans extends AsyncTask<Void,Void,String> {
        private ProgressDialog mProgressDialog;
        @Override
        protected String doInBackground(Void... params) {
            Double ans=null;
            String ss=null;
            try{

                HttpClient httpclient = new DefaultHttpClient();


                HttpPost httppost = new HttpPost("https://mecg.herokuapp.com/analyse");
                MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                File file = new File("/storage/sdcard0/download/"+name);
                if (file !=null) {
                    Log.d("EDIT USER PROFILE", "UPLOAD: file length = " + file.length());
                    Log.d("EDIT USER PROFILE", "UPLOAD: file exist = " + file.exists());
                    mpEntity.addPart("file", new FileBody(file));
                }
                httppost.setEntity(mpEntity);

                HttpResponse response = httpclient.execute(httppost);
                String result = EntityUtils.toString(response.getEntity());

                return result;
            }
            catch(IOException e){ return null;}

        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(SingleFileShow.this){
                @Override
                public boolean onKeyDown(int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        mAns.cancel(true);
                        this.dismiss();
                        onPostResume();
                        return true;
                    }
                    return super.onKeyDown(keyCode, event);
                }};
            mProgressDialog.setTitle("Analyzing Signal");
            mProgressDialog.setMessage("Analyzing...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s){
            mProgressDialog.dismiss();
            Intent i = new Intent(SingleFileShow.this,AnalysisResult.class);
            i.putExtra("result",s);
            startActivity(i);
        }
    }

}

