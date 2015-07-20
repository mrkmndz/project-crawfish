// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ViewSwitcher;

/**
 * Created by markamendoza on 7/20/15.
 */
public class ProgressSwitcher extends ViewSwitcher {

    public static final int BAR = 0;

    ProgressBar mBar;

    public ProgressSwitcher(Context context) {
        super(context);
    }

    public ProgressSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBar = new ProgressBar(context);
        mBar.setId(BAR);
        ViewSwitcher.LayoutParams params = new ViewSwitcher.LayoutParams(ViewSwitcher.LayoutParams.WRAP_CONTENT,ViewSwitcher.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        addView(mBar,params);
    }

    public void showContent(){
        if (getChildCount() < 2) {
            throw new IllegalStateException("Needs a second view");
        }
        if (getNextView().getId()!=BAR){
            showNext();
        }
    }

    public void showBar(){
        if (getNextView().getId()==BAR){
            showNext();
        }
    }
}
