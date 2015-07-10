// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

import butterknife.OnClick;

public class EventListAdapter extends ParseQueryAdapter<ParseObject> {

    private final ClickEventListener mListener;

    public EventListAdapter(Context context, QueryFactory<ParseObject> queryFactory, ClickEventListener listener) {
        super(context, queryFactory);
        mListener = listener;
    }

    interface ClickEventListener{
        void OnClick(Event event);
    }

    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.event_list_item, null);
        }

        super.getItemView(object, v, parent);

        final Event event = (Event) object;

        TextView titleView = (TextView) v.findViewById(R.id.list_item_event_name);
        TextView timeView = (TextView) v.findViewById(R.id.list_item_event_date);
        TextView locationView = (TextView) v.findViewById(R.id.list_item_event_location);

        titleView.setText(event.getTitle());
        timeView.setText(event.getFormattedStartAndEnd());
        locationView.setText(event.getLocation());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnClick(event);
            }
        });

        return v;
    }

}
