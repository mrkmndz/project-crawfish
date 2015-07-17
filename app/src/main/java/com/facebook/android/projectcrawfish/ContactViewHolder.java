// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;// Copyright 2004-present Facebook. All Rights Reserved.

import android.view.View;
import android.widget.TextView;

import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactViewHolder extends CustomViewPQA.CustomViewHolder<ParseUser>{
    private final View mView;

    @Bind(R.id.list_item_contact_name) TextView mNameView;
    @Bind(R.id.list_item_contact_position)TextView mPositionView;

    public ContactViewHolder(View v, ClickEventListener<ParseUser> listener) {
        super(v, listener);
        mView = v;
    }

    @Override
    void bindObject(final ParseUser user) {
        Profile profile = Profile.fromUser(user);

        ButterKnife.bind(this, mView);

        mNameView.setText(profile.getFullName());
        mPositionView.setText(profile.getPosition());

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactViewHolder.this.onClick(user);
            }
        });

    }
}
