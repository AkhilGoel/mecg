package com.example.akhil.mecg;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akhil on 23-03-2016.
 */
public class DownloadFile extends Activity {
    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    ListViewAdapter adapter;
    private List<MedicalFiles> medicallist = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_doc);
        if(isNetworkStatusAvialable(DownloadFile.this))
            new RemoteDataTask().execute();
        else {
            new AlertDialog.Builder(DownloadFile.this)
                    .setTitle("No Internet Connecion")
                    .setMessage("Please Check your internet connection.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            finish();
        }
    }
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(DownloadFile.this);
            mProgressDialog.setTitle("Loading Data from server");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create the array
            medicallist = new ArrayList<MedicalFiles>();
            try {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "Files");
                ob = query.find();
                for (ParseObject centre : ob) {

                    MedicalFiles map = new MedicalFiles();
                    map.setName((String) centre.get("Name"));
                    map.setCentre((String) centre.get("User"));
                    map.setObj(centre);
                    medicallist.add(map);
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            listview = (ListView) findViewById(R.id.listview);
            adapter = new ListViewAdapter(DownloadFile.this,medicallist);
            listview.setAdapter(adapter);
            mProgressDialog.dismiss();
        }
    }
    public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
                if(netInfos.isConnected())
                    return true;
        }
        return false;
    }
}

