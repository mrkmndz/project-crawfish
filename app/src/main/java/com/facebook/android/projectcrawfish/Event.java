// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

    public void checkIn(final CheckInCallback callback) {
        ParseQuery<Attendance> query =  ParseQuery.getQuery(Attendance.class);
        query.whereEqualTo(Attendance.EVENT, this);
        query.whereEqualTo(Attendance.USER, ParseUser.getCurrentUser());
        query.include(Attendance.EVENT);
        query.findInBackground(new FindCallback<Attendance>() {
            @Override
            public void done(List<Attendance> list, ParseException e) {
                if(list==null||list.size()==0){
                    final Attendance att = new Attendance();
                    att.setEvent(Event.this);
                    att.setUser(ParseUser.getCurrentUser());
                    att.setHasLeft(false);
                    att.setMAC();
                    att.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            callback.checkedIn(att);
                        }
                    });
                } else {
                    final Attendance att = list.get(0);
                    att.setHasLeft(false);
                    att.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            callback.checkedIn(att);
                        }
                    });
                }
            }
        });

    }

    public interface CheckInCallback{
        void checkedIn(Attendance attendance);
    }

    public Proxy toProxy(){
        Proxy proxy = new Proxy();
        proxy.mID = getObjectId();
        proxy.mTitle = getTitle();
        proxy.mLocation = getLocation();
        proxy.mDescription = getDescription();
        proxy.mIsAllDay = isAllDay();
        proxy.mStartDate = getStartDate();
        proxy.mEndDate = getEndDate();
        return proxy;
    }
    public static class Proxy implements Serializable {
        private String mID;
        private String mTitle;
        private String mLocation;
        private String mDescription;
        private boolean mIsAllDay;
        private Date mStartDate;
        private Date mEndDate;

        public Event toPO(){
            Event event;
            if (mID==null) {
                event = new Event();
            } else {
                event = ParseObject.createWithoutData(Event.class, mID);
            }
            event.setTitle(mTitle);
            event.setLocation(mLocation);
            event.setDescription(mDescription);
            event.setIsAllDay(mIsAllDay);
            event.setStartDate(mStartDate);
            event.setEndDate(mEndDate);
            return event;
        }
    }

}
