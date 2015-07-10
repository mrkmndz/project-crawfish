// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;


public class EventEditorFragment extends DialogFragment {
    public static final String TITLE = "TITLE";
    public static final String LOCATION = "LOCATION";
    public static final String DETAILS = "DETAILS";
    public static final String START_TIME = "START_TIME";
    public static final String END_TIME = "END_TIME";
    public static final String ALL_DAY = "ALL_DAY";
    public static final String ID = "ID";
    // TODO rotation layout
// TODO Upgrade Compliance code


    @Bind(R.id.start_date)
    Button mStartDateButton;
    @Bind(R.id.start_time)
    Button mStartTimeButton;
    @Bind(R.id.end_date)
    Button mEndDateButton;
    @Bind(R.id.end_time)
    Button mEndTimeButton;

    @Bind(R.id.event_title)
    EditText mTitleEditText;
    @Bind(R.id.location)
    EditText mLocationEditText;
    @Bind(R.id.description)
    EditText mDescriptionEditText;

    @Bind(R.id.all_day_switch)
    Switch mAllDay;

    public static final int REQUEST_START_DATE = 0;
    public static final int REQUEST_START_TIME = 1;
    public static final int REQUEST_END_DATE = 2;
    public static final int REQUEST_END_TIME = 3;

    public static final String DIALOG_DATE = "DialogDate";
    private String mTitle;
    private String mLocationt;
    private String mDetailst;
    private Date mStartTime;
    private Date mEndTime;
    private boolean mIsAllDay;
    private String mID;


    public static EventEditorFragment newInstance(String EventID) {
        EventEditorFragment fragment = new EventEditorFragment();
        Bundle args = new Bundle();
        if (EventID == null) {
            args.putString(TITLE, "");
            args.putString(LOCATION, "");
            args.putString(DETAILS, "");
            Date now = new Date();
            args.putSerializable(START_TIME, now);
            Calendar cal = new GregorianCalendar();
            cal.setTime(now);
            cal.add(Calendar.HOUR, Event.STANDARD_DURATION_HOURS);
            args.putSerializable(END_TIME, cal.getTime());
            args.putBoolean(ALL_DAY, false);
        } else {
            ParseQuery<Event> query = new ParseQuery<>(Event.CLASS_NAME);
            try {
                Event event = query.get(EventID);
                args.putString(ID,EventID);
                args.putString(TITLE, event.getTitle());
                args.putString(LOCATION, event.getLocation());
                args.putString(DETAILS, event.getDescription());
                args.putSerializable(START_TIME, event.getStartDate());
                args.putSerializable(END_TIME, event.getEndDate());
                args.putBoolean(ALL_DAY, event.isAllDay());
            } catch (ParseException e) {
                e.printStackTrace();
                throw new RuntimeException("Bad Connection");
            }
        }
        fragment.setArguments(args);
        return fragment;
    }

    public void saveToParse() throws ParseException {
        Event event;
        if (mID==null){
            event = new Event();
        } else{
          ParseQuery<Event> query = new ParseQuery<>(Event.CLASS_NAME);
            event = query.get(mID);
        }
        event.setTitle(mTitle);
        event.setDescription(mDetailst);
        event.setLocation(mLocationt);
        event.setStartDate(mStartTime);
        event.setEndDate(mEndTime);
        event.setIsAllDay(mIsAllDay);
        event.save();
    }

    public EventEditorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle;
        if (savedInstanceState != null) {
            bundle = savedInstanceState;
        } else {
            bundle = getArguments();
        }
        mID = bundle.getString(ID);
        mTitle = bundle.getString(TITLE);
        mLocationt = bundle.getString(LOCATION);
        mDetailst = bundle.getString(DETAILS);
        mStartTime = (Date) bundle.getSerializable(START_TIME);
        mEndTime = (Date) bundle.getSerializable(END_TIME);
        mIsAllDay = bundle.getBoolean(ALL_DAY);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ID,mID);
        outState.putString(TITLE,mTitle);
        outState.putString(LOCATION,mLocationt);
        outState.putString(DETAILS,mDetailst);
        outState.putSerializable(START_TIME,mStartTime);
        outState.putSerializable(END_TIME,mEndTime);
        outState.putBoolean(ALL_DAY,mIsAllDay);
    }

    boolean hasInflated=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (hasInflated) return null;
        View v = inflater.inflate(R.layout.fragment_event_creator, container, false);
        ButterKnife.bind(this, v);
        updateUI();
        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
            View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_event_creator, null);
            ButterKnife.bind(this, v);
            updateUI();
        hasInflated=true;
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Event Details")
                .setPositiveButton("Check In",null)
                .setNegativeButton("Cancel", null)
                .create();
    }

    private void updateUI() {
        mStartDateButton.setText(Event.DISPLAY_DATE_FORMAT.format(mStartTime));
        mEndDateButton.setText(Event.DISPLAY_DATE_FORMAT.format(mEndTime));

        mStartTimeButton.setText(Event.DISPLAY_TIME_FORMAT.format(mStartTime));
        mEndTimeButton.setText(Event.DISPLAY_TIME_FORMAT.format(mEndTime));

        mTitleEditText.setText(mTitle);
        mLocationEditText.setText(mLocationt);
        mDescriptionEditText.setText(mDetailst);

        mAllDay.setChecked(mIsAllDay);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_START_DATE:
            case REQUEST_START_TIME:
                mStartTime = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                break;
            case REQUEST_END_DATE:
            case REQUEST_END_TIME:
                mEndTime = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                break;
        }
        if (mEndTime.before(mStartTime)) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(mEndTime);
            cal.add(Calendar.HOUR, Event.STANDARD_DURATION_HOURS);
            mEndTime=cal.getTime();
        }
        updateUI();
    }

    @OnClick(R.id.start_date)
    public void startDateClick(View view) {
        FragmentManager manager = getFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(mStartTime);
        dialog.setTargetFragment(EventEditorFragment.this, REQUEST_START_DATE);
        dialog.show(manager, DIALOG_DATE);
    }

    @OnClick(R.id.end_date)
    public void endDateClick(View view) {
        FragmentManager manager = getFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(mEndTime);
        dialog.setTargetFragment(EventEditorFragment.this, REQUEST_END_DATE);
        dialog.show(manager, DIALOG_DATE);
    }

    @OnClick(R.id.start_time)
    public void startTimeClick(View view) {
        FragmentManager manager = getFragmentManager();
        TimePickerFragment dialog = TimePickerFragment.newInstance(mStartTime);
        dialog.setTargetFragment(EventEditorFragment.this, REQUEST_START_TIME);
        dialog.show(manager, DIALOG_DATE);
    }

    @OnClick(R.id.end_time)
    public void endTimeClick(View view) {
        FragmentManager manager = getFragmentManager();
        TimePickerFragment dialog = TimePickerFragment.newInstance(mEndTime);
        dialog.setTargetFragment(EventEditorFragment.this, REQUEST_END_TIME);
        dialog.show(manager, DIALOG_DATE);
    }

    @OnTextChanged(R.id.event_title)
    void onTitleChanged(CharSequence text) {
        mTitle=text.toString();
    }

    @OnTextChanged(R.id.location)
    void onLocationChanged(CharSequence text) {
        mLocationt=text.toString();
    }

    @OnTextChanged(R.id.description)
    void onDescriptionChanged(CharSequence text) {
        mDetailst=text.toString();
    }

    @OnCheckedChanged(R.id.all_day_switch)
    public void onAllDay(Switch s, boolean isChecked) {
        mIsAllDay = isChecked;
        if (isChecked) {
            mStartTimeButton.setVisibility(View.GONE);
            mEndTimeButton.setVisibility(View.GONE);
        } else {
            mStartTimeButton.setVisibility(View.VISIBLE);
            mEndTimeButton.setVisibility(View.VISIBLE);
        }
    }

}
