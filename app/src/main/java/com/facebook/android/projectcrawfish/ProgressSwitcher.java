// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ProgressBar;
import android.widget.ViewSwitcher;

/**
 * Created by markamendoza on 7/20/15.
 */
public class ProgressSwitcher extends ViewSwitcher {


    ProgressBar mBar;

    public ProgressSwitcher(Context context) {
        super(context);
    }

    public ProgressSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBar = new ProgressBar(context);
        ViewSwitcher.LayoutParams params = new ViewSwitcher.LayoutParams(ViewSwitcher.LayoutParams.WRAP_CONTENT,ViewSwitcher.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        addView(mBar,params);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    public void showContent(){
        if (getChildCount() < 2) {
            throw new IllegalStateException("Needs a second view");
        }
        if (getNextView()!= mBar){
            showNext();
        }
    }

    public void showBar(){
        if (getNextView()==mBar){
            showNext();
        }
    }
}
