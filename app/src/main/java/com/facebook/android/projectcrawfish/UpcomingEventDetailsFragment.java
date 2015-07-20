// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
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

    private Event mCachedEvent;

    public static UpcomingEventDetailsFragment newInstance(Event event) {
        UpcomingEventDetailsFragment fragment = new UpcomingEventDetailsFragment();
        fragment.setArguments(getBundleFromEvent(event));
        fragment.mCachedEvent = event;
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_upcoming_event_details, null);
        ButterKnife.bind(this, v);
        updateUI();
        return v;
    }

    private void updateUI() {
        mEventName.setText(mTitle);
        mEventLocation.setText(mLocation);
        mEventDuration.setText(Event.DISPLAY_DATE_FORMAT.format(mStartTime) +
                " - " +
                Event.DISPLAY_DATE_FORMAT.format(mEndTime));
        mEventDescription.setText(mDetails);
        mEventDescription.setMovementMethod(new ScrollingMovementMethod());
    }

    @OnClick(R.id.check_in_button)
    public void onClickCheckIn() {
        mListener.checkInToEvent(mID);
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
