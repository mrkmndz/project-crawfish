// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by markamendoza on 7/13/15.
 */
public class UpcomingEventDetailsFragment extends EventDialog {

    @Bind(R.id.upcoming_event_name)
    TextView mEventName;
    @Bind(R.id.upcoming_event_duration)
    TextView mEventDuration;
    @Bind(R.id.upcoming_event_location)
    TextView mEventLocation;
    @Bind(R.id.upcoming_event_description)
    TextView mEventDescription;


    public static UpcomingEventDetailsFragment newInstance(Event event) {
        UpcomingEventDetailsFragment fragment = new UpcomingEventDetailsFragment();
        fragment.setArguments(getBundleFromEvent(event));
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateCustomView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_upcoming_event_details, container, false);
        ButterKnife.bind(this, v);
        updateUI();
        return v;
    }

    private void updateUI() {
        mEventName.setText(mEvent.getTitle());
        mEventLocation.setText(mEvent.getLocation());
        mEventDuration.setText(mEvent.getFormattedStartAndEnd());
        mEventDescription.setText(mEvent.getDescription());
        mEventDescription.setMovementMethod(new ScrollingMovementMethod());
    }

    @OnClick(R.id.check_in_button)
    public void onClickCheckIn() {
        mListener.checkInToEvent(mEvent.getObjectId());
        this.dismiss();
    }

    private OnFragmentInteractionListener mListener;

    interface OnFragmentInteractionListener {
        void checkInToEvent(String eventID);
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
}
