// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Date;

/**
 * Created by markamendoza on 7/13/15.
 */
public abstract class EventDialog extends DialogFragment {

    public static final String PROXY = "PROXY";
    protected Event mEvent;

    protected static Bundle getBundleFromEvent(Event event) {
        Bundle bundle = new Bundle();
        Event.Proxy proxy = event.toProxy();
        bundle.putSerializable(PROXY,proxy);
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
        Event.Proxy proxy = (Event.Proxy) bundle.getSerializable(PROXY);
        mEvent = proxy.toPO();
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
        outState.putSerializable(PROXY, mEvent.toProxy());
    }
}
