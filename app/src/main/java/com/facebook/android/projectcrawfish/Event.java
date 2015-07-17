// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


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

    public static final SimpleDateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
    public static final SimpleDateFormat DISPLAY_TIME_FORMAT = new SimpleDateFormat("K:mm a", Locale.US);

    public Date getStartDate() {
        try {
            return (Date) fetchIfNeeded().get(START_DATE);
        } catch (ParseException e) {
            throw new RuntimeException("Something bad");
        }
    }

    public void setStartDate(Date startDate) {
        put(START_DATE,startDate);
    }

    public Date getEndDate() {
        try {
            return (Date) fetchIfNeeded().get(END_DATE);
        } catch (ParseException e) {
            throw new RuntimeException("Something bad");
        }
    }

    public void setEndDate(Date endDate) {
        put(END_DATE,endDate);
    }

    public String getTitle() {
        try {
            return (String) fetchIfNeeded().get(TITLE);
        } catch (ParseException e) {
            throw new RuntimeException("Something bad");
        }
    }

    public void setTitle(String title) {
        put(TITLE,title);
    }

    public String getLocation() {
        try {
            return (String) fetchIfNeeded().get(LOCATION);
        } catch (ParseException e) {
            throw new RuntimeException("Something bad");
        }
    }

    public void setLocation(String location) {
        put(LOCATION,location);
    }

    public String getDescription() {
        try {
            return (String) fetchIfNeeded().get(DESCRIPTION);
        } catch (ParseException e) {
            throw new RuntimeException("Something bad");
        }
    }

    public void setDescription(String description) {
        put(DESCRIPTION,description);
    }

    public Boolean isAllDay(){
        try {
            return (Boolean) fetchIfNeeded().get(ALL_DAY);
        } catch (ParseException e) {
            throw new RuntimeException("Something bad");
        }
    }

    public void setIsAllDay(Boolean isAllDay){
        put(ALL_DAY, isAllDay);
    }

    public String getFormattedStartTime(){
        return DISPLAY_TIME_FORMAT.format(this.getStartDate());
    }

    public String getFormattedStartDate(){
        return DISPLAY_DATE_FORMAT.format(this.getStartDate());
    }

    public String getFormattedEndTime(){
        return DISPLAY_TIME_FORMAT.format(this.getEndDate());
    }
    public String getFormattedEndDate(){
        return DISPLAY_DATE_FORMAT.format(this.getEndDate());
    }


    public String getFormattedStartAndEnd() {
        return getFormattedStartDate()+" "+getFormattedStartTime()+
                "-"+getFormattedEndDate()+" "+getFormattedEndTime();
    }

    public void checkIn() {
        Attendance att = new Attendance();
        att.setEvent(this);
        att.setUser(ParseUser.getCurrentUser());
        try {
            att.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
