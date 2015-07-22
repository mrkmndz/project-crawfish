// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;// Copyright 2004-present Facebook. All Rights Reserved.

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PastEventDetailsFragment extends EventDialog implements FindCallback<Attendance> {

    private static final String PDIS = "PDIS";
    @Bind(R.id.past_event_name)
    TextView mTitleView;
    @Bind(R.id.past_event_location)
    TextView mLocationView;
    @Bind(R.id.past_event_description)
    TextView mDescriptionView;
    @Bind(R.id.past_event_duration)
    TextView mDurationView;
    @Bind(R.id.fragment_past_event_deck_button)
    Button mDeckButton;

    @Bind(R.id.button_switcher)
    ProgressSwitcher mSwitcher;

    private ArrayList<CardSwipeActivity.ProfileDisplayInstance> mPDIs;

    public static PastEventDetailsFragment newInstance(Event event) {

        PastEventDetailsFragment fragment = new PastEventDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(PROXY, event.toProxy());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        Event.Proxy proxy = (Event.Proxy) arguments.getSerializable(PROXY);
        assert proxy != null;
        mEvent = proxy.toPO();
        //noinspection unchecked
        mPDIs = (ArrayList<CardSwipeActivity.ProfileDisplayInstance>) arguments.getSerializable(PDIS);
    }

    @Override
    public void done(List<Attendance> list, ParseException e) {
        mPDIs = new ArrayList<>();
        for (Attendance att : list) {
            CardSwipeActivity.ProfileDisplayInstance pdi = new CardSwipeActivity.ProfileDisplayInstance(att);
            mPDIs.add(pdi);
        }
        mSwitcher.showContent();
        updateButton();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PROXY, mEvent.toProxy());
        outState.putSerializable(PDIS, mPDIs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_past_event_details, container, false);
        ButterKnife.bind(this, v);
        if (mPDIs == null) {
            queryForPDIs();
        } else {
            mSwitcher.showContent();
            updateButton();
        }
        Turnstile.get().registerListener(new Turnstile.OnZeroListener() {
            @Override
            public void onZero() {
                queryForPDIs();
            }

            @Override
            public void onNonZero() {
                mSwitcher.showBar();
            }
        });
        updateText();
        return v;
    }

    private void queryForPDIs(){
        mSwitcher.showBar();
        ParseQuery<Swipe> innerQueryA = ParseQuery.getQuery(Swipe.class);
        innerQueryA.whereEqualTo(Swipe.SWIPER, ParseUser.getCurrentUser());
        innerQueryA.whereEqualTo(Swipe.IS_LEFT_SWIPE, false);//Everyone you have right swiped

        ParseQuery<Swipe> innerQueryB = ParseQuery.getQuery(Swipe.class);
        innerQueryB.whereEqualTo(Swipe.SWIPER, ParseUser.getCurrentUser());
        innerQueryB.whereEqualTo(Swipe.EVENT, mEvent);//Everyone you have swiped at this event

        List<ParseQuery<Swipe>> innerQueries = new ArrayList<>();
        innerQueries.add(innerQueryA);
        innerQueries.add(innerQueryB);
        ParseQuery<Swipe> innerQuery = ParseQuery.or(innerQueries);

        final ParseQuery<Attendance> query = ParseQuery.getQuery(Attendance.class);
        query.whereEqualTo(Attendance.EVENT, mEvent);
        query.whereNotEqualTo(Attendance.USER, ParseUser.getCurrentUser());
        query.whereDoesNotMatchKeyInQuery(Attendance.USER, Swipe.SWIPEE, innerQuery);
        query.include(Attendance.USER);

        query.findInBackground(PastEventDetailsFragment.this);
    }

    private void updateText() {
        mTitleView.setText(mEvent.getTitle());
        mLocationView.setText(mEvent.getLocation());
        mDescriptionView.setText(mEvent.getDescription());
        mDescriptionView.setMovementMethod(new ScrollingMovementMethod());
        mDurationView.setText(mEvent.getFormattedStartAndEnd());
    }

    private void updateButton(){
        int numberOfCards = mPDIs.size();
        mDeckButton.setEnabled(numberOfCards!= 0);
        mDeckButton.setText("You have " + numberOfCards + " cards remaining");
    }

    @OnClick(R.id.fragment_past_event_deck_button)
    public void openDeckClick(View view) {
        mListener.openSwipes(mPDIs);
    }

    private OnFragmentInteractionListener mListener;

    public void refresh() {
        mSwitcher.showBar();
        if (Turnstile.get().isClear()) queryForPDIs();
    }

    interface OnFragmentInteractionListener {
        void openSwipes(ArrayList<CardSwipeActivity.ProfileDisplayInstance> PDIs);
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
