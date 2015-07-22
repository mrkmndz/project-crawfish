// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by markamendoza on 7/9/15.
 */
public class UpcomingEventListFragment extends ListFragment<Event>{

    @Override
    protected ParseQuery<Event> getQuery() {
        ParseQuery<Event> query = new ParseQuery<>(Event.CLASS_NAME);
        query.orderByAscending(Event.START_DATE);
        Date now = new Date();
        query.whereLessThan(Event.START_DATE, now);
        query.whereGreaterThan(Event.END_DATE, now);//Not over
        return query;
    }

    @Override
    protected CustomViewPQA.CustomViewHolderFactory<Event> getHolderFactory() {
        return new CustomViewPQA.CustomViewHolderFactory<Event>() {
            @Override
            public CustomViewPQA.CustomViewHolder<Event> create(View v, CustomViewPQA.CustomViewHolder.ClickEventListener<Event> listener) {
                return new EventViewHolder(v, listener);
            }
        };
    }

    @Override
    protected int getListItemResID() {
        return R.layout.event_list_item;
    }

    private OnFragmentInteractionListener mListener;

    @Override
    public void OnClick(Event obj) {
        mListener.openUpcomingEventDetails(obj);
    }


    interface OnFragmentInteractionListener{
        void openUpcomingEventDetails(Event event);
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
