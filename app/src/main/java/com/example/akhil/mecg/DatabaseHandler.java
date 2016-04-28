package com.example.akhil.mecg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akhil on 09-04-2016 for Mecg
 */
public class DatabaseHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DataManager";
    private static final String TABLE_PLOTS = "plot";
    private static final String KEY_ID = "id";
    private static final String KEY_VALUE = "value";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLOTS_TABLE = "CREATE TABLE " + TABLE_PLOTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_VALUE + " DOUBLE"
                + ")";
        db.execSQL(CREATE_PLOTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLOTS);
        onCreate(db);
    }
    // Adding new PLOT
    void addPlot(PlotData plot) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_VALUE, plot.getValue()); // plot Value

        // Inserting Row
        db.insert(TABLE_PLOTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single PLOT
    PlotData getPlot(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PLOTS, new String[] { KEY_ID,
                        KEY_VALUE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        PlotData plot = new PlotData(Integer.parseInt(cursor.getString(0)),
                Double.parseDouble(cursor.getString(1)));
        // return PlotData
        return plot;
    }

    // Getting All plots
    public List<PlotData> getAllPlots() {
        List<PlotData> plotList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PLOTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PlotData plot = new PlotData();
                plot.setId(Integer.parseInt(cursor.getString(0)));
                plot.setValue(Double.parseDouble(cursor.getString(1)));
                plotList.add(plot);
            } while (cursor.moveToNext());
        }

        // return plot list
        return plotList;
    }

    // Updating single plot
    public int updateplot(PlotData plot) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_VALUE, plot.getValue());

        // updating row
        return db.update(TABLE_PLOTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(plot.getId()) });
    }

    // Deleting single plot
    public void deletePlot(PlotData plot) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLOTS, KEY_ID + " = ?",
                new String[] { String.valueOf(plot.getId()) });
        db.close();
    }


    // Getting plots Count
    public int getPlotsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PLOTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
