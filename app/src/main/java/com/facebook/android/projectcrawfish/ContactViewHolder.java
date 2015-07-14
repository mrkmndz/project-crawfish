// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;// Copyright 2004-present Facebook. All Rights Reserved.

import android.view.View;
import android.widget.TextView;

import com.parse.ParseObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactViewHolder extends CustomViewPQA.CustomViewHolder{
    private final View mView;

    @Bind(R.id.list_item_contact_name) TextView mNameView;
    @Bind(R.id.list_item_contact_position)TextView mPositionView;

    public ContactViewHolder(View v, ClickEventListenerd listenerd) {
        super(v, listenerd);
        mView = v;
    }

    @Override
    void bindObject(final ParseObject obj) {
        final Profile profile = (Profile) obj;

        ButterKnife.bind(this, mView);

        mNameView.setText(profile.getFullName());
        mPositionView.setText(profile.getPosition());

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactViewHolder.this.onClick(obj);
            }
        });

    }
}
