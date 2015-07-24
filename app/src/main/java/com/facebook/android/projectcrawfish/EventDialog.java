// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.util.zip.Inflater;

public abstract class EventDialog extends CustomViewDialog {

    public static final String PROXY = "PROXY";
    protected Event mEvent;

    protected static Bundle getBundleFromEvent(Event event) {
        Bundle bundle = new Bundle();
        Event.Proxy proxy = event.toProxy();
        bundle.putSerializable(PROXY, proxy);
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
        assert proxy != null;
        mEvent = proxy.toPO();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PROXY, mEvent.toProxy());
    }

}
