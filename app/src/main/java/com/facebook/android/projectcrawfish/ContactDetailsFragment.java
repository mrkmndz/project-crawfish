// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.IconButton;
import android.widget.TextView;

import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactDetailsFragment extends ProfileDialog {

    IconButton mContactFb;
    IconButton mContactLinkedIn;
    @Bind(R.id.contact_name) TextView mNameField;
    @Bind(R.id.contact_position) TextView mPositionField;
    @Bind(R.id.contact_number) TextView mNumberField;
    @Bind(R.id.contact_email) TextView mEmailField;

    public static ContactDetailsFragment newInstance(ParseUser user) {
        ContactDetailsFragment frag = new ContactDetailsFragment();
        Bundle args = getBundleFromUser(user);
        frag.setArguments(args);
        frag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact, null, false);
        ButterKnife.bind(this, v);

        mContactFb = (IconButton) v.findViewById(R.id.contact_fb);
        mContactLinkedIn = (IconButton) v.findViewById(R.id.contact_linkedin);

        updateUI();

        return v;
    }

    private void updateUI() {
        mNameField.setText(mProfile.getFullName());
        mPositionField.setText(mProfile.getPosition());
        mNumberField.setText(mProfile.getPhoneNumber());
        mEmailField.setText(mProfile.getEmail());
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
