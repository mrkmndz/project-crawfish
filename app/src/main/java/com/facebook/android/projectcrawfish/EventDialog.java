// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by markamendoza on 7/13/15.
 */
public abstract class EventDialog extends DialogFragment {
    public static final String TITLE = "TITLE";
    public static final String LOCATION = "LOCATION";
    public static final String DETAILS = "DETAILS";
    public static final String START_TIME = "START_TIME";
    public static final String END_TIME = "END_TIME";
    public static final String ALL_DAY = "ALL_DAY";
    public static final String ID = "ID";

    protected String mTitle;
    protected String mLocationt;
    protected String mDetailst;
    protected Date mStartTime;
    protected Date mEndTime;
    protected boolean mIsAllDay;
    protected String mID;

    protected static Bundle getBundleFromID(String eventID){
        if (eventID==null) throw  new RuntimeException("Needs an Event ID");
        Bundle bundle = new Bundle();
        ParseQuery<Event> query = new ParseQuery<>(Event.CLASS_NAME);
        try {
            Event event = query.get(eventID);
            bundle.putString(ID, eventID);
            bundle.putString(TITLE, event.getTitle());
            bundle.putString(LOCATION, event.getLocation());
            bundle.putString(DETAILS, event.getDescription());
            bundle.putSerializable(START_TIME, event.getStartDate());
            bundle.putSerializable(END_TIME, event.getEndDate());
            bundle.putBoolean(ALL_DAY, event.isAllDay());
            return bundle;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Bad Connection");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle;
        if (savedInstanceState != null) {
            bundle = savedInstanceState;
        } else {
            bundle = getArguments();
        }
        mID = bundle.getString(ID);
        mTitle = bundle.getString(TITLE);
        mLocationt = bundle.getString(LOCATION);
        mDetailst = bundle.getString(DETAILS);
        mStartTime = (Date) bundle.getSerializable(START_TIME);
        mEndTime = (Date) bundle.getSerializable(END_TIME);
        mIsAllDay = bundle.getBoolean(ALL_DAY);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ID,mID);
        outState.putString(TITLE,mTitle);
        outState.putString(LOCATION,mLocationt);
        outState.putString(DETAILS,mDetailst);
        outState.putSerializable(START_TIME,mStartTime);
        outState.putSerializable(END_TIME,mEndTime);
        outState.putBoolean(ALL_DAY,mIsAllDay);
    }
}