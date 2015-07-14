// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.IconButton;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseQuery;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactDetailsFragment extends ProfileDialog {



    IconButton mContactFb;
    IconButton mContactLinkedIn;
    @Bind(R.id.contact_name)
    TextView mNameField;
    @Bind(R.id.contact_position)
    TextView mPositionField;
    @Bind(R.id.contact_number)
    TextView mNumberField;
    @Bind(R.id.contact_email)
    TextView mEmailField;



    public static ContactDetailsFragment newInstance(String ContactID) {
        ContactDetailsFragment frag = new ContactDetailsFragment();
        Bundle args = getBundleFromID(ContactID);
        frag.setArguments(args);
        return frag;
    }

    boolean hasInflated = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (hasInflated) return null;
        View v = inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.bind(this, v);

        mContactFb = (IconButton) v.findViewById(R.id.contact_fb);
        mContactLinkedIn = (IconButton) v.findViewById(R.id.contact_linkedin);

        updateUI();

        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_contact, null);
        ButterKnife.bind(this, v);
        updateUI();
        hasInflated = true;
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }

    private void updateUI() {
        mNameField.setText(mFirstName + " " + mLastName);
        mPositionField.setText(mPosition);
        mNumberField.setText(mNumber);
        mEmailField.setText(mEmail);
    }



    @OnClick(R.id.contact_fb)
    public void onFbClick(View view) {
        // TODO Connect FB profile
    }

    @OnClick(R.id.contact_linkedin)
    public void onLinkedInClick(View view) {
        // TODO Connect LinkedIn Profile
    }
}
