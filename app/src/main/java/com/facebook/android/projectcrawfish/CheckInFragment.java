// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by markamendoza on 7/9/15.
 */
public class CheckInFragment extends DialogFragment {
    private Event mEvent;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_event_creator,null);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Event Title")
                .setPositiveButton("Check In", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
    }

    public static CheckInFragment newInstance(Event event){
        CheckInFragment frag = new CheckInFragment();
        frag.mEvent=event;
        return frag;
    }
}
