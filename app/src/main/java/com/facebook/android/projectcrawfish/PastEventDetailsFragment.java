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

public class PastEventDetailsFragment extends EventDialog {

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

    boolean hasInflated = false;

    public static PastEventDetailsFragment newInstance(String EventID) {
        PastEventDetailsFragment fragment = new PastEventDetailsFragment();
        Bundle args = getBundleFromID(EventID);
        fragment.setArguments(args);
        return fragment;
    }

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

    private void updateUI() {
        mTitleView.setText(mTitle);
        mLocationView.setText(mLocationt);
        mDescriptionView.setText(mDetailst);
        mDurationView.setText(Event.DISPLAY_DATE_FORMAT.format(mStartTime) +
                " - " +
                Event.DISPLAY_DATE_FORMAT.format(mEndTime));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        //Update notificiations?
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
