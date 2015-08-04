// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;// Copyright 2004-present Facebook. All Rights Reserved.

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactViewHolder extends CustomViewPQA.CustomViewHolder<ParseUser> {
    private final View mView;

    @Bind(R.id.list_item_contact_name)
    TextView mNameView;
    @Bind(R.id.list_item_contact_position)
    TextView mPositionView;
    @Bind(R.id.list_item_contact_photo)
    ImageView mPhoto;
    @Bind(R.id.switcher)
    ProgressSwitcher mSwitcher;

    public ContactViewHolder(View v, ClickEventListener<ParseUser> listener) {
        super(listener);
        mView = v;
    }

    @Override
    void bindObject(final ParseUser user) {
        Profile profile = Profile.fromUser(user);

        ButterKnife.bind(this, mView);

        mNameView.setText(profile.getFullName());
        mPositionView.setText(profile.getPosition());

        profile.loadProfilePictureIntoImageView(mPhoto,mSwitcher);

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactViewHolder.this.onClick(user);
            }
        });
    }
}
