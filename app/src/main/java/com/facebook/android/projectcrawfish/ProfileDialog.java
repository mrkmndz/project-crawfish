// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.parse.ParseException;
import com.parse.ParseQuery;

/**
 * Created by markamendoza on 7/14/15.
 */
abstract class ProfileDialog extends DialogFragment{
    private static final String CONTACT_ID = "CONTACT_ID";
    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String LAST_NAME = "LAST_NAME";
    private static final String POSITION = "POSITION";
    private static final String NUMBER = "NUMBER";
    private static final String EMAIL = "EMAIL";

    protected String mID;
    protected String mFirstName;
    protected String mLastName;
    protected String mPosition;
    protected String mNumber;
    protected String mEmail;

    protected static Bundle getBundleFromID(String profileID){
        if (profileID==null) throw  new RuntimeException("Needs an Profile ID");
        Bundle bundle = new Bundle();
        ParseQuery<Profile> query = new ParseQuery<>(Profile.CLASS_NAME);
        try {
            Profile profile = query.get(profileID);
            bundle.putString(CONTACT_ID,profileID);
            bundle.putString(FIRST_NAME,profile.getFirstName());
            bundle.putString(LAST_NAME, profile.getLastName());
            bundle.putString(POSITION,profile.getPosition());
            bundle.putString(NUMBER,profile.getPhoneNumber());
            bundle.putString(EMAIL,profile.getEmail());
            return bundle;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Bad Connection");
        }
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
        mFirstName = bundle.getString(FIRST_NAME);
        mLastName = bundle.getString(LAST_NAME);
        mPosition = bundle.getString(POSITION);
        mNumber = bundle.getString(NUMBER);
        mEmail = bundle.getString(EMAIL);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CONTACT_ID, mID);
        outState.putString(FIRST_NAME, mFirstName);
        outState.putString(LAST_NAME, mLastName);
        outState.putString(POSITION, mPosition);
        outState.putString(NUMBER, mNumber);
        outState.putString(EMAIL, mEmail);
    }
}
