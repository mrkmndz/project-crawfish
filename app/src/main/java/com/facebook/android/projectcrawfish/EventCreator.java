// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.parse.ParseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;


public class EventCreator extends Fragment {
//TODO rotation save
// TODO rotation layout
// TODO Upgrade Compliance code

    private Event mEvent;

    @Bind(R.id.start_date) Button mStartDateButton;
    @Bind(R.id.start_time) Button mStartTimeButton;
    @Bind(R.id.end_date) Button mEndDateButton;
    @Bind(R.id.end_time) Button mEndTimeButton;

    public static final int REQUEST_START_DATE = 0;
    public static final int REQUEST_START_TIME = 1;
    public static final int REQUEST_END_DATE = 2;
    public static final int REQUEST_END_TIME = 3;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("K:mm a", Locale.US);


    public static final String DIALOG_DATE = "DialogDate";
    private boolean mIsAllDay;

    private static final int STANDARD_DURATION_HOURS = 1;

    public Event getEvent() {
        return mEvent;
    }

    public static EventCreator newInstance() {
        Event e = new Event();

        Date now = new Date();
        e.setStartDate(now);

        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.HOUR,STANDARD_DURATION_HOURS);
        e.setEndDate(cal.getTime());

        EventCreator fragment = new EventCreator();
        fragment.mEvent=e;
        return fragment;
    }

    public EventCreator() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event_creator, container, false);
        ButterKnife.bind(this, v);
        updateDates();
        return v;
    }

    private void updateDates(){
        mStartDateButton.setText(dateFormat.format(mEvent.getStartDate()));
        mEndDateButton.setText(dateFormat.format(mEvent.getEndDate()));

        mStartTimeButton.setText(timeFormat.format(mEvent.getStartDate()));
        mEndTimeButton.setText(timeFormat.format(mEvent.getEndDate()));
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode){
            case REQUEST_START_DATE:
            case REQUEST_START_TIME:
                Date startDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                mEvent.setStartDate(startDate);
                break;
            case REQUEST_END_DATE:
            case REQUEST_END_TIME:
                Date endDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                mEvent.setEndDate(endDate);
                break;
        }
        if(mEvent.getEndDate().before(mEvent.getStartDate())){
            Calendar cal = Calendar.getInstance();
            cal.setTime(mEvent.getStartDate());
            cal.add(Calendar.HOUR,STANDARD_DURATION_HOURS);
            mEvent.setEndDate(cal.getTime());
        }
        updateDates();
    }

    @OnClick(R.id.start_date)
    public void startDateClick(View view) {
        FragmentManager manager = getFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(mEvent.getStartDate());
        dialog.setTargetFragment(EventCreator.this, REQUEST_START_DATE);
        dialog.show(manager, DIALOG_DATE);
    }

    @OnClick(R.id.end_date)
    public void endDateClick(View view) {
        FragmentManager manager = getFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(mEvent.getEndDate());
        dialog.setTargetFragment(EventCreator.this, REQUEST_END_DATE);
        dialog.show(manager, DIALOG_DATE);
    }

    @OnClick(R.id.start_time)
    public void startTimeClick(View view){
        FragmentManager manager = getFragmentManager();
        TimePickerFragment dialog = TimePickerFragment.newInstance(mEvent.getStartDate());
        dialog.setTargetFragment(EventCreator.this, REQUEST_START_TIME);
        dialog.show(manager, DIALOG_DATE);
    }

    @OnClick(R.id.end_time)
    public void endTimeClick(View view){
        FragmentManager manager = getFragmentManager();
        TimePickerFragment dialog = TimePickerFragment.newInstance(mEvent.getEndDate());
        dialog.setTargetFragment(EventCreator.this, REQUEST_END_TIME);
        dialog.show(manager, DIALOG_DATE);
    }

    @OnCheckedChanged(R.id.all_day_switch)
    public void onAllDay(Switch s, boolean isChecked){
        mIsAllDay = isChecked;
        if(isChecked) {
            mStartTimeButton.setVisibility(View.GONE);
            mEndTimeButton.setVisibility(View.GONE);
        } else {
            mStartTimeButton.setVisibility(View.VISIBLE);
            mEndTimeButton.setVisibility(View.VISIBLE);
        }
    }

}
