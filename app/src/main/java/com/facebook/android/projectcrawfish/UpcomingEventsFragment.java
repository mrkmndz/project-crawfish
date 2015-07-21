// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by markamendoza on 7/9/15.
 */
public class UpcomingEventsFragment extends Fragment{

    public static final int CHECK_IN = 2;
    public static final String IS_CHECKED_IN = "IS_CHECKED_IN";
    public static final String ATTENDANCE_ID = "ATTENDANCE_ID";
    public static final String CACHED_EVENT_NAME = "CACHED_EVENT_NAME";

    private OnFragmentInteractionListener mListener;
    private UpcomingEventListFragment mListFragment;
    private boolean mIsCheckedIn;
    private String mCheckedInAttendanceID;

    @Bind(R.id.switcher)
    ViewSwitcher mSwitcher;

    @Bind(R.id.checked_in_view)
    ProgressSwitcher mProgressSwitcher;

    @Bind(R.id.checked_into_title)
    TextView mCheckedInTitle;
    private String mCachedEventName;

    public void refreshList() {
        mListFragment.refreshList();
    }

    public void confirmCheckIn(Attendance attendance) {
        mCheckedInAttendanceID = attendance.getObjectId();
        mCachedEventName = attendance.getEvent().getTitle();
        updateUI();
        mProgressSwitcher.showContent();
    }

    public void startCheckIn() {
        mIsCheckedIn=true;
        showCheckedIn();
        mProgressSwitcher.showBar();
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
                mCheckedInAttendanceID = savedInstanceState.getString(ATTENDANCE_ID);
                mCachedEventName = savedInstanceState.getString(CACHED_EVENT_NAME);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        mProgressSwitcher.showBar();
        showCheckedIn();
        ParseQuery<Attendance> query = ParseQuery.getQuery(Attendance.class);
        query.whereEqualTo(Attendance.USER, ParseUser.getCurrentUser());
        query.whereEqualTo(Attendance.HAS_LEFT, false);
        query.include(Attendance.EVENT);
        query.findInBackground(new FindCallback<Attendance>() {
            @Override
            public void done(List<Attendance> list, ParseException e) {
                if (list==null ||list.size()==0){
                    mIsCheckedIn = false;
                    showList();
                } else {
                    mIsCheckedIn = true;
                    Attendance attendance = list.get(0);
                    mCachedEventName = attendance.getEvent().getTitle();
                    mCheckedInAttendanceID = attendance.getObjectId();
                    showCheckedIn();
                }
                mProgressSwitcher.showContent();
            }
        });
        return view;
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
        mProgressSwitcher.showBar();
        Attendance attendance = ParseObject.createWithoutData(Attendance.class, mCheckedInAttendanceID);
        attendance.setHasLeft(true);
        attendance.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                mIsCheckedIn = true;
                showList();
                mProgressSwitcher.showContent();
            }
        });
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

    void updateUI(){
        mCheckedInTitle.setText(mCachedEventName);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_CHECKED_IN,mIsCheckedIn);
        if(mIsCheckedIn) {
            outState.putString(ATTENDANCE_ID, mCheckedInAttendanceID);
            outState.putString(CACHED_EVENT_NAME,mCachedEventName);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
