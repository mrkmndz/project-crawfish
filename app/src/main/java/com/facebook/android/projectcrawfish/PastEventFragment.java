// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

/**
 * Created by mariazlatkova on 7/6/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PastEventFragment extends Fragment {

    // TODO add time of passed event to view

    @Bind(R.id.fragment_past_event_deck_button) Button mPastEventDeckButton;
    @Bind(R.id.fragment_past_event_connections_button) Button mPastEventAttendeeButton;

    @Bind(R.id.event_title) TextView mEventTitle;
    @Bind(R.id.location) TextView mLocation;
    @Bind(R.id.description) TextView mDescription;


    private static final String ARG_PAST_EVENT_ID = "past_event_id";

    public static final int REQUEST_DECK = 0;
    public static final int REQUEST_CONNECTIONS = 1;

    private Event mPastEvent;
    private ContactListFragment mContactListFrag;

    public Event getEvent() {
        return mPastEvent;
    }

    public static PastEventFragment newInstance(String PastEventID) {
        Bundle args = new Bundle();
        args.putString(ARG_PAST_EVENT_ID, PastEventID);

        PastEventFragment fragment = new PastEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public PastEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPastEvent = Event.getLocalEvent(getArguments().getString(ARG_PAST_EVENT_ID));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_past_event, container, false);
        ButterKnife.bind(this, v);
        // updateUI();
        return v;
    }

    private void updateUI() {

        mEventTitle.setText(mPastEvent.getTitle());
        mLocation.setText(mPastEvent.getLocation());
        mDescription.setText(mPastEvent.getDescription());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO OnClick for deck and attendee buttons to display list view/swipe fragment
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CONNECTIONS:
                //
            case REQUEST_DECK:
        }
    }

    @OnClick(R.id.fragment_past_event_connections_button)
    public void viewConnectionsClick(View view) {

        Intent i = new Intent(getActivity(), ContactListActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.fragment_past_event_deck_button)
    public void viewDeckClick(View view) {
        Intent i = new Intent(getActivity(), CardSwipeActivity.class);
        startActivity(i);
    }

}
