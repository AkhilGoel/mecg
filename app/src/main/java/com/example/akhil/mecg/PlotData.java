package com.example.akhil.mecg;

/**
 * Created by Akhil on 09-04-2016 for Mecg
 */
public class PlotData {
    int _id;
    double value;

    public PlotData(){

    }
    // constructor
    public PlotData(int id, double value){
        this._id = id;
        this.value = value;
    }

    // constructor
    public PlotData(double value){
        this.value = value;
    }
    // getting ID
    public int getId(){
        return this._id;
    }

    // setting id
    public void setId(int id){
        this._id = id;
    }

    // getting name
    public double getValue(){
        return this.value;
    }

    // setting name
    public void setValue(double value){
        this.value = value;
    }
}
