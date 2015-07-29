// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Application;
import android.content.Context;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.Utils;
import com.facebook.FacebookSdk;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import static com.facebook.android.projectcrawfish.Constants.TAG;

//import com.parse.ParseCrashReporting;

/**
 * Created by markamendoza on 7/6/15.
 */
public class CrawfishApp extends Application {
    private Cloudinary cloudinary;

    public Cloudinary getCloudinary() {
        return cloudinary;
    }

    public static CrawfishApp getInstance(Context context) {
        return (CrawfishApp)context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        L.setTag(TAG);
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        //ParseCrashReporting.enable(this);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "zZ00GLyDLVPJVen8KHQUpiGx2eAKbg8EUajBz87H", "0rGGU81Rr8elNfvTq2e64Uque11lvYGPoxImCEN1");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseFacebookUtils.initialize(this);
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(Attendance.class);
        ParseObject.registerSubclass(Swipe.class);

        initCloudinary();
        initUIL();
    }



    private void initUIL() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
    }

    private void initCloudinary() {
        // Cloudinary: creating a cloudinary instance using meta-data from manifest

        cloudinary = new Cloudinary(Utils.cloudinaryUrlFromContext(this));
        L.i("Cloudinary initialized");
    }
}
