// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;


public class EventEditorFragment extends EventDialog {

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

    @Bind(R.id.switcher)
    ProgressSwitcher mSwitcher;

    public static final int REQUEST_START_DATE = 0;
    public static final int REQUEST_START_TIME = 1;
    public static final int REQUEST_END_DATE = 2;
    public static final int REQUEST_END_TIME = 3;

    public static final String DIALOG_DATE = "DialogDate";

    public static EventEditorFragment newInstance(Event event) {
        EventEditorFragment fragment = new EventEditorFragment();
        Bundle args = (event == null) ? getBundleWithNoID() : getBundleFromEvent(event);
        fragment.setArguments(args);
        return fragment;
    }

    static private Bundle getBundleWithNoID() {
        Bundle bundle = new Bundle();
        Event newEvent = new Event();
        newEvent.setTitle("");
        newEvent.setLocation("");
        newEvent.setDescription("");
        newEvent.setIsAllDay(false);
        Date now = new Date();
        newEvent.setStartDate(now);
        Calendar cal = new GregorianCalendar();
        cal.setTime(now);
        cal.add(Calendar.HOUR, Event.STANDARD_DURATION_HOURS);
        newEvent.setEndDate(cal.getTime());
        bundle.putSerializable(PROXY, newEvent.toProxy());
        return bundle;
    }

    @Override
    public View onCreateCustomView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_event_creator, container, false);
        ButterKnife.bind(this, v);
        mSwitcher.showContent();
        updateUI();
        return v;
    }

    private void updateUI() {
        mStartDateButton.setText(Event.DISPLAY_DATE_FORMAT.format(mEvent.getStartDate()));
        mEndDateButton.setText(Event.DISPLAY_DATE_FORMAT.format(mEvent.getEndDate()));

        mStartTimeButton.setText(Event.DISPLAY_TIME_FORMAT.format(mEvent.getStartDate()));
        mEndTimeButton.setText(Event.DISPLAY_TIME_FORMAT.format(mEvent.getEndDate()));

        mTitleEditText.setText(mEvent.getTitle());
        mLocationEditText.setText(mEvent.getLocation());
        mDescriptionEditText.setText(mEvent.getDescription());

        mAllDay.setChecked(mEvent.isAllDay());
    }

    public void saveToParse(){
        mSwitcher.showBar();
        mEvent.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                mSwitcher.showContent();
                mListener.done();
            }
        });
    }

    private OnFragmentInteractionListener mListener;

    interface OnFragmentInteractionListener {
        void done();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_START_DATE:
            case REQUEST_START_TIME:
                mEvent.setStartDate( (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE));
                break;
            case REQUEST_END_DATE:
            case REQUEST_END_TIME:
                mEvent.setEndDate((Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE));
                break;
        }
        if (mEvent.getEndDate().before(mEvent.getStartDate())) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(mEvent.getStartDate());
            cal.add(Calendar.HOUR, Event.STANDARD_DURATION_HOURS);
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
    public void startTimeClick(View view) {
        FragmentManager manager = getFragmentManager();
        TimePickerFragment dialog = TimePickerFragment.newInstance(mEvent.getStartDate());
        dialog.setTargetFragment(EventEditorFragment.this, REQUEST_START_TIME);
        dialog.show(manager, DIALOG_DATE);
    }

    @OnClick(R.id.end_time)
    public void endTimeClick(View view) {
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
        mEvent.setLocation( text.toString());
    }

    @OnTextChanged(R.id.description)
    void onDescriptionChanged(CharSequence text) {
        mEvent.setDescription(text.toString());
    }

    @OnCheckedChanged(R.id.all_day_switch)
    public void onAllDay(Switch s, boolean isChecked) {
        mEvent.setIsAllDay(isChecked);
        if (isChecked) {
            mStartTimeButton.setVisibility(View.GONE);
            mEndTimeButton.setVisibility(View.GONE);
        } else {
            mStartTimeButton.setVisibility(View.VISIBLE);
            mEndTimeButton.setVisibility(View.VISIBLE);
        }
    }

}
