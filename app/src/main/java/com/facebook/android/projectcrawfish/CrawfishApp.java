// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

//import com.parse.ParseCrashReporting;

/**
 * Created by markamendoza on 7/6/15.
 */
public class CrawfishApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        //ParseCrashReporting.enable(this);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "zZ00GLyDLVPJVen8KHQUpiGx2eAKbg8EUajBz87H", "0rGGU81Rr8elNfvTq2e64Uque11lvYGPoxImCEN1");
        ParseFacebookUtils.initialize(this);
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(Attendance.class);
        ParseObject.registerSubclass(Swipe.class);
    }
}
