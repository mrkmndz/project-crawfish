// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    private UpcomingEventListFragment mListFragment;
    private Boolean mIsCheckedIn;
    private String mCheckedInAttendanceID;

    @Bind(R.id.switcher)
    ViewSwitcher mSwitcher;

    @Bind(R.id.checked_in_view)
    ProgressSwitcher mProgressSwitcher;

    @Bind(R.id.checked_into_title)
    TextView mCheckedInTitle;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    private String mCachedEventName;

    public void refreshList() {
        mListFragment.refreshList();
    }

    public void confirmCheckIn(Attendance attendance) {
        mCheckedInAttendanceID = attendance.getObjectId();
        mCachedEventName = attendance.getEvent().getTitle();
        mCheckedInTitle.setText(mCachedEventName);
        mProgressSwitcher.showContent();
    }

    public void startCheckIn() {
        mIsCheckedIn=true;
        showCheckedIn();
        mProgressSwitcher.showBar();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            ParseQuery<Attendance> query = ParseQuery.getQuery(Attendance.class);
            query.whereEqualTo(Attendance.USER, ParseUser.getCurrentUser());
            query.whereEqualTo(Attendance.HAS_LEFT, false);
            query.include(Attendance.EVENT);

            query.findInBackground(new FindCallback<Attendance>() {
                @Override
                public void done(List<Attendance> list, ParseException e) {
                    if (list==null ||list.size()==0){
                        mIsCheckedIn = false;
                    } else {
                        mIsCheckedIn = true;
                        Attendance attendance = list.get(0);
                        mCachedEventName = attendance.getEvent().getTitle();
                        mCheckedInAttendanceID = attendance.getObjectId();
                    }
                    onLoaded();
                }
            });

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

        if (mIsCheckedIn == null){
            showCheckedIn();
            mProgressSwitcher.showBar();
        } else {
            onLoaded();
        }

        mToolbar.setTitle("Check In");
        mToolbar.setLogo(R.mipmap.ic_launcher);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);

        ActionBar ab = activity.getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        return view;
    }

    private void onLoaded(){
        if (mIsCheckedIn){
            showCheckedIn();
            mCheckedInTitle.setText(mCachedEventName);
        } else {
            showList();
        }
        mProgressSwitcher.showContent();
    }

    @OnClick(R.id.check_out_button)
    public void onClickCheckOut(){
        mProgressSwitcher.showBar();
        Attendance attendance = ParseObject.createWithoutData(Attendance.class, mCheckedInAttendanceID);
        attendance.setHasLeft(true);
        attendance.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                mIsCheckedIn = false;
                showList();
                mProgressSwitcher.showContent();
                mListener.onCheckOut();
            }
        });
    }

    void showList(){
        if (mSwitcher == null) return;
        if (mSwitcher.getNextView().getId()==R.id.list_fragment_container){
            mSwitcher.showNext();
        }
    }
    void showCheckedIn(){
        if (mSwitcher == null) return;
        if (mSwitcher.getNextView().getId()==R.id.checked_in_view){
            mSwitcher.showNext();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_CHECKED_IN, mIsCheckedIn);
        if(mIsCheckedIn) {
            outState.putString(ATTENDANCE_ID, mCheckedInAttendanceID);
            outState.putString(CACHED_EVENT_NAME,mCachedEventName);
        }
    }

    private OnFragmentInteractionListener mListener;

    interface OnFragmentInteractionListener {
        void onCheckOut();
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
