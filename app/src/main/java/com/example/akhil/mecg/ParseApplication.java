package com.example.akhil.mecg;
import com.parse.Parse;
import com.parse.ParseACL;

import com.parse.ParseUser;

import android.app.Application;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here
//        Parse.initialize(this,"3pFQNaNXxZobFCDZojasr3ednifpisuXLyQLPt6z","05KDOuTuKdUg5HIzE4nGtBaB7i9mStPLY523fk9g");
        Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId("3pFQNaNXxZobFCDZojasr3ednifpisuXLyQLPt6z")
                        .clientKey("05KDOuTuKdUg5HIzE4nGtBaB7i9mStPLY523fk9g")
                        .server("https://parseapi.back4app.com/")
                        .build()
        );

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }

}