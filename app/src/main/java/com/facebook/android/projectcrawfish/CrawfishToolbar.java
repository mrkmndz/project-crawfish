// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

/**
 * Created by markamendoza on 7/15/15.
 */
public class CrawfishToolbar extends Toolbar {
    public CrawfishToolbar(Context context) {
        this(context, null);
    }

    public CrawfishToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CrawfishToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setTitle("");
        this.setBackgroundColor(getResources().getColor(R.color.toolbar_2));
        LayoutInflater  mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView textView = (TextView) mInflater.inflate(R.layout.standard_toolbar, this, false);
        this.addView(textView);
        try{
            AppCompatActivity activity = (AppCompatActivity) context;
            activity.setSupportActionBar(this);
        }catch (ClassCastException e){
            if (!isInEditMode()) Log.e("Toolbar","Toolbar not in activity");
        }
    }
}
