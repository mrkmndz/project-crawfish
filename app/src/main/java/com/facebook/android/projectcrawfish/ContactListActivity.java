// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;// Copyright 2004-present Facebook. All Rights Reserved.

import android.support.v4.app.Fragment;

public class ContactListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ContactListFragment();
    }
}
