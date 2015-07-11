// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;// Copyright 2004-present Facebook. All Rights Reserved.

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseQuery;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PastEventDetailsFragment extends DialogFragment {

    public static final String ID = "ID";
    public static final String TITLE = "TITLE";
    public static final String LOCATION = "LOCATION";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String DURATION = "DURATION";

    @Bind(R.id.past_event_name)
    TextView mTitleView;
    @Bind(R.id.past_event_location)
    TextView mLocationView;
    @Bind(R.id.past_event_description)
    TextView mDescriptionView;
    @Bind(R.id.past_event_duration)
    TextView mDurationView;

    public static final int REQUEST_DECK = 0;
    public static final int REQUEST_CONNECTIONS = 1;

    private String mID;
    private String mTitle;
    private String mLocation;
    private String mDescription;
    private String mDuration;

    public static PastEventDetailsFragment newInstance(String EventID) {
        try {
            PastEventDetailsFragment fragment = new PastEventDetailsFragment();
            Bundle args = new Bundle();

            ParseQuery<Event> query = new ParseQuery<>(Event.CLASS_NAME);
            Event event = query.get(EventID);
            args.putString(ID, EventID);
            args.putString(TITLE, event.getTitle());
            args.putString(LOCATION, event.getLocation());
            args.putString(DESCRIPTION, event.getDescription());
            args.putString(DURATION, event.getFormattedStartAndEnd());

            fragment.setArguments(args);
            return fragment;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Bad Connection");
        }
    }

    public PastEventDetailsFragment() {
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
        mLocation = bundle.getString(LOCATION);
        mDescription = bundle.getString(DESCRIPTION);
        mDuration = bundle.getString(DURATION);
    }

    boolean hasInflated = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (hasInflated) return null;
        View v = inflater.inflate(R.layout.fragment_past_event_details, container, false);
        ButterKnife.bind(this, v);
        updateUI();
        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_past_event_details, null);
        ButterKnife.bind(this, v);
        updateUI();
        hasInflated = true;
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }

    private void updateUI() {

        mTitleView.setText(mTitle);
        mLocationView.setText(mLocation);
        mDescriptionView.setText(mDescription);
        mDurationView.setText(mDuration);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ID, mID);
        outState.putString(TITLE, mTitle);
        outState.putString(LOCATION, mLocation);
        outState.putString(DESCRIPTION, mDescription);
        outState.putString(DURATION, mDuration);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CONNECTIONS:
                // TODO Display connections from event
            case REQUEST_DECK:
                // TODO Display remaining deck swipes
        }
    }

    @OnClick(R.id.fragment_past_event_deck_button)
    public void openDeckClick(View view) {
        Intent intent = new Intent(getActivity(), CardSwipeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.fragment_past_event_connections_button)
    public void openConnectionsClick(View view) {
        Intent intent = new Intent(getActivity(), ContactListActivity.class);
        startActivity(intent);
    }
}
