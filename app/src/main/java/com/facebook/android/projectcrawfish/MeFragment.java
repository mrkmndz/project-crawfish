// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.joanzapata.android.iconify.Iconify;
import com.joanzapata.android.iconify.IconDrawable;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {



    public MeFragment() {
        // Required empty public constructor
    }

    @Bind(R.id.contact_fb)
    ImageButton mFacebook;

    @Bind((R.id.contact_linkedin))
    ImageButton mLinkedIn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, v);
        mFacebook.setImageDrawable(new IconDrawable(getActivity(), Iconify.IconValue.fa_facebook_square).sizeDp(48).colorRes(R.color.fb));
        mLinkedIn.setImageDrawable(new IconDrawable(getActivity(), Iconify.IconValue.fa_linkedin_square).sizeDp(48).colorRes(R.color.linkedIn));




        return v;
    }


}
