package com.example.application.demowhatapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("2d683f1e26d3c9e9eee541ed34c6cfcb4ab03b76")
                .clientKey("a5e3c37ed2bbc6e715b4a6d0be752f43e9a7c1b3")
                .server("http://18.220.117.137:80/parse/")
                .build()
        );
//    F1nE5e0y6Wi9

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
