// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by markamendoza on 7/13/15.
 */
public class UpcomingEventDetailsFragment extends EventDialog {

    @Bind(R.id.text_blob) TextView mTextView;

    public static UpcomingEventDetailsFragment newInstance(String EventID){
        UpcomingEventDetailsFragment fragment = new UpcomingEventDetailsFragment();
        fragment.setArguments(getBundleFromID(EventID));
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_upcoming_event_details, null);
        ButterKnife.bind(this, v);
        updateUI();
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }

    private void updateUI() {
        mTextView.setText(mTitle + " @ " + mLocationt);
    }

    @OnClick(R.id.check_in_button)
    public void onClickCheckIn(){
        mListener.confirmCheckIn(mID,mTitle);
        this.dismiss();
    }

    private OnFragmentInteractionListener mListener;

    interface OnFragmentInteractionListener {
        void confirmCheckIn(String eventID, String cachedTitle);
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
