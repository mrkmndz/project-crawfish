// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;// Copyright 2004-present Facebook. All Rights Reserved.

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PastEventDetailsFragment extends EventDialog implements FindCallback<Attendance> {

    private static final String PDIS = "PDIS";
    public static final String ATTENDANCE_ID = "ATTENDANCE_ID";
    @Bind(R.id.past_event_name)
    TextView mTitleView;
    @Bind(R.id.past_event_location)
    TextView mLocationView;
    @Bind(R.id.past_event_description)
    TextView mDescriptionView;
    @Bind(R.id.past_event_duration)
    TextView mDurationView;
    @Bind(R.id.fragment_past_event_deck_button)
    Button mDeckButton;

    @Bind(R.id.button_switcher)
    ProgressSwitcher mSwitcher;

    private ArrayList<CardSwipeActivity.ProfileDisplayInstance> mPDIs;
    private String mAttendanceID;

    public static PastEventDetailsFragment newInstance(Attendance attendance) {

        PastEventDetailsFragment fragment = new PastEventDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(PROXY, attendance.getEvent().toProxy());
        args.putString(ATTENDANCE_ID, attendance.getObjectId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mAttendanceID = arguments.getString(ATTENDANCE_ID, null);
        Event.Proxy proxy = (Event.Proxy) arguments.getSerializable(PROXY);
        assert proxy != null;
        mEvent = proxy.toPO();
        //noinspection unchecked
        mPDIs = (ArrayList<CardSwipeActivity.ProfileDisplayInstance>) arguments.getSerializable(PDIS);
    }

    @Override
    public void done(List<Attendance> list, ParseException e) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PROXY, mEvent.toProxy());
        outState.putSerializable(PDIS, mPDIs);
    }

    @Override
    public View onCreateCustomView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_past_event_details, container, false);
        ButterKnife.bind(this, v);
        if (mPDIs == null) {
            queryForPDIs();
        } else {
            mSwitcher.showContent();
            updateButton();
        }
        updateText();
        return v;
    }

    private void queryForPDIs(){
        mSwitcher.showBar();
        HashMap<String, Object> params = new HashMap<>();

        params.put("attendanceID", mAttendanceID);
        ParseCloud.callFunctionInBackground("getSortedProfiles", params, new FunctionCallback<List<Map<String,String>>>() {
            @Override
            public void done(List<Map<String, String>> maps, ParseException e) {
                mPDIs = new ArrayList<>();
                for (Map<String,String> map : maps) {
                    PublicProfile profile = new PublicProfile(map);
                    CardSwipeActivity.ProfileDisplayInstance pdi = new CardSwipeActivity.ProfileDisplayInstance(mEvent, profile);
                    mPDIs.add(pdi);
                }
                mSwitcher.showContent();
                updateButton();
            }
        });
    }

    private void updateText() {
        mTitleView.setText(mEvent.getTitle());
        mLocationView.setText(mEvent.getLocation());
        mDescriptionView.setText(mEvent.getDescription());
        mDescriptionView.setMovementMethod(new ScrollingMovementMethod());
        mDurationView.setText(mEvent.getFormattedStartAndEnd());
    }

    private void updateButton(){
        int numberOfCards = mPDIs.size();
        mDeckButton.setEnabled(numberOfCards!= 0);
        mDeckButton.setText("You have " + numberOfCards + " cards remaining");
    }

    @OnClick(R.id.fragment_past_event_deck_button)
    public void openDeckClick(View view) {
        mListener.openSwipes(mPDIs);
    }

    private OnFragmentInteractionListener mListener;

    public void refresh() {
        mSwitcher.showBar();
        if (Turnstile.get().isClear()){
            queryForPDIs();
        } else {
            Turnstile.get().registerListener(new Turnstile.OnZeroListener() {
                @Override
                public void onZero() {
                    queryForPDIs();
                    Turnstile.get().deregisterListener(this);
                }

                @Override
                public void onNonZero() {
                    //
                }
            });
        }
    }

    interface OnFragmentInteractionListener {
        void openSwipes(ArrayList<CardSwipeActivity.ProfileDisplayInstance> PDIs);
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
