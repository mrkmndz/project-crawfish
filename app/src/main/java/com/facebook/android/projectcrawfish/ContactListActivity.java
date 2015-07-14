// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;// Copyright 2004-present Facebook. All Rights Reserved.

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class ContactListActivity extends AppCompatActivity
        implements ContactListFragment.OnFragmentInteractionListener {

    public static final String DIALOG_CONTACT_DETAILS = "DialogContactDetails";

    protected Fragment createFragment() {
        return new ContactListFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    public void openContactDetails(Profile profile) {
        FragmentManager manager = getSupportFragmentManager();
        ContactDetailsFragment contactDetailsFragment = ContactDetailsFragment.newInstance(profile.getObjectId());
        contactDetailsFragment.show(manager, DIALOG_CONTACT_DETAILS);
    }
}
