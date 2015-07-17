// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by markamendoza on 7/17/15.
 */
@ParseClassName(Attendance.CLASS_NAME)
public class Attendance extends ParseObject {
    public static final String CLASS_NAME = "Attendance";
    public static final String USER = "USER";
    public static final String EVENT = "EVENT";

    public void setUser(ParseUser user) {
        put(USER,user);
    }
    public ParseUser getUser() {
        return (ParseUser) get(USER);
    }

    public void setEvent(Event event) {
        put(EVENT,event);
    }

    public Event getEvent() {
        return (Event) get(EVENT);
    }

}
