// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseQuery;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactDetailsFragment extends Fragment {

    private static final String CONTACT_ID = "CONTACT_ID";
    private static final String NAME = "NAME";
    private static final String POSITION = "POSITION";
    private static final String NUMBER = "NUMBER";
    private static final String EMAIL = "EMAIL";

    @Bind(R.id.list_item_contact_name) TextView mNameField;
    @Bind(R.id.list_item_contact_position) TextView mPositionField;
    @Bind(R.id.list_item_contact_number) TextView mNumberField;
    @Bind(R.id.list_item_contact_email) TextView mEmailField;
    @Bind(R.id.contact_fb) ImageButton mFbButton;
    @Bind(R.id.contact_linkedin) ImageButton mLinkedInButton;

    private Contact mContact;
    private String mID;
    private String mName;
    private String mPosition;
    private String mNumber;
    private String mEmail;

    public Contact getContact() {
        return mContact;
    }

    public static ContactDetailsFragment newInstance(String ContactID) {
        ContactDetailsFragment fragment = new ContactDetailsFragment();
        Bundle args = new Bundle();
        args.putString(CONTACT_ID, ContactID);

        ParseQuery<Contact> query = new ParseQuery<>(Contact.CLASS_NAME);
        try {
            Contact contact = query.get(ContactID);
            args.getString(CONTACT_ID, ContactID);
            args.getString(NAME, contact.getFullName());
            args.getString(POSITION, contact.getPosition());
            args.getString(NUMBER, contact.getPhoneNumber());
            args.getString(EMAIL, contact.getEmail());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Bad Connection");
        }

        fragment.setArguments(args);
        return fragment;
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

        // mContact = Contact.getContact(getArguments().getString(CONTACT_ID));
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact, container, false);

        ButterKnife.bind(this, v);

        mFbButton.setEnabled(false);
        mLinkedInButton.setEnabled(false);

        return v;
    }

    private void updateUI() {
        mNameField.setText(mName);
        mPositionField.setText(mPosition);
        mNumberField.setText(mNumber);
        mEmailField.setText(mEmail);
    }

}
