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

public class ContactDetailsFragment extends DialogFragment {

    private static final String CONTACT_ID = "CONTACT_ID";
    private static final String NAME = "NAME";
    private static final String POSITION = "POSITION";
    private static final String NUMBER = "NUMBER";
    private static final String EMAIL = "EMAIL";

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

    private String mID;
    private String mName;
    private String mPosition;
    private String mNumber;
    private String mEmail;

    public static ContactDetailsFragment newInstance(String ContactID) {

        try {
            ContactDetailsFragment fragment = new ContactDetailsFragment();
            Bundle args = new Bundle();

            ParseQuery<Contact> query = new ParseQuery<>(Contact.CLASS_NAME);
            Contact contact = query.get(ContactID);
            args.putString(CONTACT_ID, ContactID);
            args.putString(NAME, contact.getFullName());
            args.putString(POSITION, contact.getPosition());
            args.putString(NUMBER, contact.getPhoneNumber());
            args.putString(EMAIL, contact.getEmail());

            fragment.setArguments(args);
            return fragment;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Bad Connection");
        }
    }

    public ContactDetailsFragment() {
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

        mID = bundle.getString(CONTACT_ID);
        mName = bundle.getString(NAME);
        mPosition = bundle.getString(POSITION);
        mNumber = bundle.getString(NUMBER);
        mEmail = bundle.getString(EMAIL);
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
        mNameField.setText(mName);
        mPositionField.setText(mPosition);
        mNumberField.setText(mNumber);
        mEmailField.setText(mEmail);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CONTACT_ID, mID);
        outState.putString(NAME, mName);
        outState.putString(POSITION, mPosition);
        outState.putString(NUMBER, mNumber);
        outState.putString(EMAIL, mEmail);
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
