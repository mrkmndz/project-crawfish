// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;

/**
 * Created by markamendoza on 7/9/15.
 */
public class UpcomingEventListFragment extends EventList {

    private OnFragmentInteractionListener mListener;

    interface OnFragmentInteractionListener{
        void checkIntoEvent(Event event);
    }

    @Override
    protected ParseQuery<ParseObject> query() {
                ParseQuery<ParseObject> query = new ParseQuery<>(Event.CLASS_NAME);
                query.orderByAscending(Event.START_DATE);
                query.whereGreaterThan(Event.END_DATE, new Date());//Not over
                return query;
    }

    @Override
    protected void onReceiveClickEvent(Event event) {
        mListener.checkIntoEvent(event);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener=null;
    }


}
