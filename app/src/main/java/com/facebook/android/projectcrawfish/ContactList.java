// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;// Copyright 2004-present Facebook. All Rights Reserved.

import android.app.Activity;

import com.parse.ParseObject;
import com.parse.ParseQuery;


public class ContactList extends ContactListFragment {

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void openContactDetails(Contact contact);
    }

    @Override
    protected ParseQuery<ParseObject> query() {
        ParseQuery<ParseObject> query = new ParseQuery<>(Contact.CLASS_NAME);
        query.orderByDescending(Contact.LAST_NAME);
        return query;
    }

    @Override
    protected void onReceiveClickContact(Contact contact) {
        mListener.openContactDetails(contact);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
