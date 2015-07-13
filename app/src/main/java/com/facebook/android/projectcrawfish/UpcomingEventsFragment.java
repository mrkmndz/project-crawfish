// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by markamendoza on 7/9/15.
 */
public class UpcomingEventsFragment extends Fragment {

    public static final int CHECK_IN = 2;
    public static final String IS_CHECKED_IN = "IS_CHECKED_IN";
    public static final String EVENT_ID = "EVENT_ID";
    public static final String CACHED_EVENT_NAME = "CACHED_EVENT_NAME";

    private OnFragmentInteractionListener mListener;
    private UpcomingEventListFragment mListFragment;
    private boolean mIsCheckedIn;
    private String mCheckedInEventID;

    @Bind(R.id.switcher)
    ViewSwitcher mSwitcher;

    @Bind(R.id.checked_into_title)
    TextView mCheckedInTitle;
    private String mCachedEventName;

    public void refreshList() {
        mListFragment.refreshList();
    }

    public void confirmCheckIn(String eventID, String cachedTitle) {
        mCachedEventName = cachedTitle;
        mCheckedInEventID=eventID;
        mIsCheckedIn=true;
        updateUI();
    }

    interface OnFragmentInteractionListener {
        void createNewEvent();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            mIsCheckedIn = false;
        } else {
            mIsCheckedIn = savedInstanceState.getBoolean(IS_CHECKED_IN);
            if (mIsCheckedIn){
                mCheckedInEventID = savedInstanceState.getString(EVENT_ID);
                mCachedEventName = savedInstanceState.getString(CACHED_EVENT_NAME);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TODO check for already checkedin
        View view = inflater.inflate(R.layout.fragment_upcoming_events_tab, container, false);
        ButterKnife.bind(this, view);
        FragmentManager manager = getChildFragmentManager();
        mListFragment = (UpcomingEventListFragment) manager.findFragmentById(R.id.list_fragment_container);
        if (mListFragment == null) {
            mListFragment = new UpcomingEventListFragment();
            manager.beginTransaction()
                    .add(R.id.list_fragment_container, mListFragment)
                    .commit();
        }
        updateUI();
        return view;
    }

    @OnClick(R.id.new_event_button)
    public void onFABClick() {
        mListener.createNewEvent();
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

    @OnClick(R.id.check_out_button)
    public void onClickCheckOut(){
        //TODO send to Parse
        mIsCheckedIn=false;
        updateUI();
    }

    void updateUI(){
            if (mIsCheckedIn) {
                mCheckedInTitle.setText(mCachedEventName);
                showCheckedIn();
            } else {
                showList();
            }
    }

    void showList(){
        if (mSwitcher.getNextView().getId()==R.id.list_fragment_container){
            mSwitcher.showNext();
        }
    }
    void showCheckedIn(){
        if (mSwitcher.getNextView().getId()==R.id.checked_in_view){
            mSwitcher.showNext();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_CHECKED_IN,mIsCheckedIn);
        if(mIsCheckedIn) {
            outState.putString(EVENT_ID, mCheckedInEventID);
            outState.putString(CACHED_EVENT_NAME,mCachedEventName);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
