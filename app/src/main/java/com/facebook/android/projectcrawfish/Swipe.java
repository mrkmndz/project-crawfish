// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;


/**
 * Created by markamendoza on 7/17/15.
 */
@ParseClassName(Swipe.CLASS_NAME)
public class Swipe extends ParseObject implements SaveCallback {
    public static final String CLASS_NAME = "Swipe";
    public static final String SWIPER = "SWIPER";
    public static final String SWIPEE = "SWIPEE";
    public static final String IS_LEFT_SWIPE = "IS_LEFT_SWIPE";
    public static final String EVENT = "EVENT";

    public ParseUser getSwiper() {
        return (ParseUser) get(SWIPER);
    }

    @Override
    public void done(ParseException e) {
        try {
            this.unpin();
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
    }

    public static void sendSwipe(boolean isLeftSwipe, ParseUser user, final Event event) {
        final Swipe swipe = new Swipe();
        swipe.put(IS_LEFT_SWIPE, isLeftSwipe);
        swipe.put(SWIPER, ParseUser.getCurrentUser());
        swipe.put(SWIPEE, user);
        swipe.put(EVENT, event);
        Turnstile.get().checkout();
        swipe.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Turnstile.get().checkin();
            }
        });
    }

    public ParseUser getSwipee() {
        return (ParseUser) get(SWIPEE);
    }

    public Event getEvent() {
        return (Event) get(EVENT);
    }
}
