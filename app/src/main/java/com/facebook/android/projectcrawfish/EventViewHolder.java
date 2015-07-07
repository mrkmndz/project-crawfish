// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;// Copyright 2004-present Facebook. All Rights Reserved.

import android.view.View;
import android.widget.TextView;

import com.parse.ParseObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EventViewHolder extends CustomViewPQA.CustomViewHolder{


    private final View mView;
    @Bind(R.id.list_item_event_name) TextView mTitleView;
    @Bind(R.id.list_item_event_date) TextView mTimeView;
    @Bind(R.id.list_item_event_location)TextView mLocationView;

    public EventViewHolder(View v, ClickEventListenerd listenerd) {
        super(v, listenerd);
        mView = v;
    }

    @Override
    void bindObject(final ParseObject obj) {
        final Event event = (Event) obj;

        ButterKnife.bind(this, mView);

        mTitleView.setText(event.getTitle());
        mTimeView.setText(event.getFormattedStartAndEnd());
        mLocationView.setText(event.getLocation());

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventViewHolder.this.onClick(obj);
            }
        });
    }
}
