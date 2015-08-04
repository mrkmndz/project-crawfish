// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by markamendoza on 7/31/15.
 */
public abstract class FrameFragment<T extends Fragment> extends Fragment {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment, container, false);

        ButterKnife.bind(this, v);

        if (getFragment() == null) {
            getChildFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, getNewFragmentInstance())
                    .commit();
        }

        mToolbar.setTitle(getTitle());
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);

        ActionBar ab = activity.getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        return v;
    }

    abstract protected T getNewFragmentInstance();

    protected String getTitle(){
        return "bïnder";
    }

    protected T getFragment(){
        return (T) getChildFragmentManager().findFragmentById(R.id.fragment_container);
    }
}
