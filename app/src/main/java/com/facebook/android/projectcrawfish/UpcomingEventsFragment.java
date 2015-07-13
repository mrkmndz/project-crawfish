// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by markamendoza on 7/9/15.
 */
public class UpcomingEventsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private UpcomingEventListFragment mListFragment;

    public void refreshList() {
        mListFragment.refreshList();
    }

    interface OnFragmentInteractionListener {
        void createNewEvent();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming_events_tab, container, false);
        ButterKnife.bind(this, view);
        FragmentManager manager = getChildFragmentManager();
        mListFragment = (UpcomingEventListFragment) manager.findFragmentById(R.id.list_fragment_container);
        if (mListFragment == null) {
            mListFragment = new UpcomingEventListFragment();
            manager.beginTransaction()
                    .add(R.id.list_fragment_container, mListFragment)
                    .commit();
        }
        return view;
    }

    @OnClick(R.id.new_event_button)
    public void onFABClick() {
        mListener.createNewEvent();
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
