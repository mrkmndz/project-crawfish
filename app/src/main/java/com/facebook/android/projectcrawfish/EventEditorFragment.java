// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;


public class EventEditorFragment extends Fragment {
    private static final String ARG = "ID";
    //TODO rotation save
// TODO rotation layout
// TODO Upgrade Compliance code

    private Event mEvent;
    public Event getEvent() {
        return mEvent;
    }

    private boolean mIsAllDay;

    @Bind(R.id.start_date) Button mStartDateButton;
    @Bind(R.id.start_time) Button mStartTimeButton;
    @Bind(R.id.end_date) Button mEndDateButton;
    @Bind(R.id.end_time) Button mEndTimeButton;

    @Bind(R.id.event_title) EditText mEventTitle;
    @Bind(R.id.location) EditText mLocation;
    @Bind(R.id.description) EditText mDescription;

    @Bind(R.id.all_day_switch) Switch mAllDay;

    public static final int REQUEST_START_DATE = 0;
    public static final int REQUEST_START_TIME = 1;
    public static final int REQUEST_END_DATE = 2;
    public static final int REQUEST_END_TIME = 3;

    public static final String DIALOG_DATE = "DialogDate";


    public static EventEditorFragment newInstance(String EventID) {
        EventEditorFragment fragment = new EventEditorFragment();
        Bundle args = new Bundle();
        args.putString(ARG, EventID);
        fragment.setArguments(args);
        return fragment;
    }

    public EventEditorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEvent = Event.getLocalEvent(getArguments().getString(ARG));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event_creator, container, false);
        ButterKnife.bind(this, v);
        updateUI();
        return v;
    }

    private void updateUI(){
        mStartDateButton.setText(mEvent.getFormattedStartDate());
        mEndDateButton.setText(mEvent.getFormattedEndDate());

        mStartTimeButton.setText(mEvent.getFormattedStartTime());
        mEndTimeButton.setText(mEvent.getFormattedEndTime());

        mEventTitle.setText(mEvent.getTitle());
        mLocation.setText(mEvent.getLocation());
        mDescription.setText(mEvent.getDescription());

        mAllDay.setChecked(mEvent.isAllDay());
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
            cal.add(Calendar.HOUR,Event.STANDARD_DURATION_HOURS);
            mEvent.setEndDate(cal.getTime());
        }
        updateUI();
    }

    @OnClick(R.id.start_date)
    public void startDateClick(View view) {
        FragmentManager manager = getFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(mEvent.getStartDate());
        dialog.setTargetFragment(EventEditorFragment.this, REQUEST_START_DATE);
        dialog.show(manager, DIALOG_DATE);
    }

    @OnClick(R.id.end_date)
    public void endDateClick(View view) {
        FragmentManager manager = getFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(mEvent.getEndDate());
        dialog.setTargetFragment(EventEditorFragment.this, REQUEST_END_DATE);
        dialog.show(manager, DIALOG_DATE);
    }

    @OnClick(R.id.start_time)
    public void startTimeClick(View view){
        FragmentManager manager = getFragmentManager();
        TimePickerFragment dialog = TimePickerFragment.newInstance(mEvent.getStartDate());
        dialog.setTargetFragment(EventEditorFragment.this, REQUEST_START_TIME);
        dialog.show(manager, DIALOG_DATE);
    }

    @OnClick(R.id.end_time)
    public void endTimeClick(View view){
        FragmentManager manager = getFragmentManager();
        TimePickerFragment dialog = TimePickerFragment.newInstance(mEvent.getEndDate());
        dialog.setTargetFragment(EventEditorFragment.this, REQUEST_END_TIME);
        dialog.show(manager, DIALOG_DATE);
    }

    @OnTextChanged(R.id.event_title)
    void onTitleChanged(CharSequence text) {
        mEvent.setTitle(text.toString());
    }
    @OnTextChanged(R.id.location)
    void onLocationChanged(CharSequence text) {
        mEvent.setLocation(text.toString());
    }
    @OnTextChanged(R.id.description)
    void onDescriptionChanged(CharSequence text) {
        mEvent.setDescription(text.toString());
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
