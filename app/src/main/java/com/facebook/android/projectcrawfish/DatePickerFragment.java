// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private static final String ARG_DATE = "date";
        public static final String EXTRA_DATE = "com.facebook.projectcrawfish.date";

        private Calendar mCalendar;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Date date = (Date) getArguments().getSerializable(ARG_DATE);

            mCalendar = Calendar.getInstance();
            mCalendar.setTime(date);
            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH);
            int day = mCalendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);

        }

        public static DatePickerFragment newInstance(Date date){
            Bundle args = new Bundle();
            args.putSerializable(ARG_DATE, date);

            DatePickerFragment add = new DatePickerFragment();
            add.setArguments(args);
            return add;
        }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH,monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if (getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, mCalendar.getTime());

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }
}
