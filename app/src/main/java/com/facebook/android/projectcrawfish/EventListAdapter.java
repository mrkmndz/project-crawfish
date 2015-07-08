// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

public class EventListAdapter extends ParseQueryAdapter<ParseObject> {

    public EventListAdapter(Context context, QueryFactory<ParseObject> queryFactory) {
        super(context, queryFactory);
    }

    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.event_list_item, null);
        }

        super.getItemView(object, v, parent);

        Event event = (Event) object;

        TextView titleView = (TextView) v.findViewById(R.id.list_item_event_name);
        TextView timeView = (TextView) v.findViewById(R.id.list_item_event_date);
        TextView locationView = (TextView) v.findViewById(R.id.list_item_event_location);

        titleView.setText(event.getTitle());
        timeView.setText(event.getFormattedStartAndEnd());
        locationView.setText(event.getLocation());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO load checkin dialog
            }
        });

        return v;
    }

}
