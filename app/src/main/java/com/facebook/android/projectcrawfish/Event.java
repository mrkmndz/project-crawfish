// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by markamendoza on 7/6/15.
 */
@ParseClassName(Event.CLASS_NAME)
public class Event extends ParseObject {
    public static final String CLASS_NAME = "Event";

    public static final String START_DATE = "START_DATE";
    public static final String END_DATE = "END_DATE";
    public static final String TITLE = "TITLE";
    public static final String LOCATION = "LOCATION";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String ALL_DAY = "ALL_DAY";

    public static final int STANDARD_DURATION_HOURS = 1;

    public Date getStartDate() {
        return (Date) get(START_DATE);
    }

    public void setStartDate(Date startDate) {
        put(START_DATE,startDate);
    }

    public Date getEndDate() {
        return (Date) get(END_DATE);
    }

    public void setEndDate(Date endDate) {
        put(END_DATE,endDate);
    }

    public String getTitle() {
        return (String) get(TITLE);
    }

    public void setTitle(String title) {
        put(TITLE,title);
    }

    public String getLocation() {
        return (String) get(LOCATION);
    }

    public void setLocation(String location) {
        put(LOCATION,location);
    }

    public String getDescription() {
        return (String) get(DESCRIPTION);
    }

    public void setDescription(String description) {
        put(DESCRIPTION,description);
    }

    public Boolean isAllDay(){
        return (Boolean) get(ALL_DAY);
    }

    public void setIsAllDay(Boolean isAllDay){
        put(ALL_DAY, isAllDay);
    }

    public static Event getLocalEvent(String eventID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Event.CLASS_NAME);
        query.fromLocalDatastore();
        try {
            return (Event) query.get(eventID);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
