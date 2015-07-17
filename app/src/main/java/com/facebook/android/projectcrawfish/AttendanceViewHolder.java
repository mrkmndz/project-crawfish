// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by markamendoza on 7/17/15.
 */
public class AttendanceViewHolder extends CustomViewPQA.CustomViewHolder<Attendance> {
    private final View mView;
    @Bind(R.id.list_item_event_name)
    TextView mTitleView;
    @Bind(R.id.list_item_event_date) TextView mTimeView;
    @Bind(R.id.list_item_event_location)TextView mLocationView;

    public AttendanceViewHolder(View v, ClickEventListener<Attendance> listener) {
        super(v, listener);
        mView = v;
    }

    @Override
    void bindObject(final Attendance attendance) {
        final Event event = attendance.getEvent();

        ButterKnife.bind(this, mView);

        mTitleView.setText(event.getTitle());
        mTimeView.setText(event.getFormattedStartAndEnd());
        mLocationView.setText(event.getLocation());

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttendanceViewHolder.this.onClick(attendance);
            }
        });
    }
}
