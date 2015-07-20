// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.parse.ParseUser;

/**
 * Created by markamendoza on 7/14/15.
 */
abstract class ProfileDialog extends DialogFragment{
    public static final String PROFILE = "PROFILE";
    protected Profile mProfile;

    protected static Bundle getBundleFromUser(ParseUser user){
        Bundle bundle = new Bundle();
            Profile profile = Profile.fromUser(user);
            bundle.putSerializable(PROFILE,profile);
            return bundle;
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

        mProfile = (Profile) bundle.getSerializable(PROFILE);
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
        outState.putSerializable(PROFILE,mProfile);
    }
}
