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
import com.parse.ParseUser;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PastEventList extends ListFragment<Attendance>  {



    @Override
    protected ParseQuery<Attendance> getQuery() {
        ParseQuery<Attendance> query = new ParseQuery<>(Attendance.CLASS_NAME);
        query.whereEqualTo(Attendance.USER, ParseUser.getCurrentUser());
        query.include(Attendance.EVENT);
        return query;
    }

    @Override
    protected CustomViewPQA.CustomViewHolderFactory<Attendance> getHolderFactory() {
        return new CustomViewPQA.CustomViewHolderFactory<Attendance>() {
            @Override
            public CustomViewPQA.CustomViewHolder<Attendance> create(
                    View v,
                    CustomViewPQA.CustomViewHolder.ClickEventListener<Attendance> listener
            ) {
                return new AttendanceViewHolder(v, listener);
            }
        };
    }

    @Override
    protected int getListItemResID() {
        return R.layout.event_list_item;
    }

    private OnFragmentInteractionListener mListener;

    @Override
    public void OnClick(Attendance attendance) {
        mListener.openPastEventDetails(attendance);
    }

    interface OnFragmentInteractionListener {
        void openPastEventDetails(Attendance attendance);
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
        mListener = null;
    }
}
