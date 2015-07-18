// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by markamendoza on 7/17/15.
 */
@ParseClassName(Swipe.CLASS_NAME)
public class Swipe extends ParseObject {
    public static final String CLASS_NAME = "Swipe";
    public static final String SWIPER = "SWIPER";
    public static final String SWIPEE = "SWIPEE";
    public static final String IS_LEFT_SWIPE = "IS_LEFT_SWIPE";
    public static final String EVENT = "EVENT";

    public ParseUser getSwiper(){
        return (ParseUser) get(SWIPER);
    }

    public static Swipe leftSwipe(ParseUser user, Event event){
        Swipe swipe = new Swipe();
        swipe.put(IS_LEFT_SWIPE,true);
        swipe.put(SWIPER,ParseUser.getCurrentUser());
        swipe.put(SWIPEE, user);
        swipe.put(EVENT,event);
        return swipe;
    }

    public static Swipe rightSwipe(ParseUser user, Event event){
        Swipe swipe = new Swipe();
        swipe.put(IS_LEFT_SWIPE,false);
        swipe.put(SWIPER,ParseUser.getCurrentUser());
        swipe.put(SWIPEE, user);
        swipe.put(EVENT,event);
        return swipe;
    }

    public ParseUser getSwipee(){
        return (ParseUser) get(SWIPEE);
    }

    public Event getEvent(){
        return (Event) get(EVENT);
    }
}
