// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import com.parse.ui.ParseLoginDispatchActivity;

/**
 * Created by markamendoza on 7/15/15.
 */
public class MainDispatchActivity extends ParseLoginDispatchActivity {
    @Override
    protected Class<?> getTargetClass() {
        return MainActivity.class;
    }
}
