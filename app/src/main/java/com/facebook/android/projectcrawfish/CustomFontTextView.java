// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;// Copyright 2004-present Facebook. All Rights Reserved.

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomFontTextView extends TextView {

    private static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public CustomFontTextView(Context context) {
        super(context);
        applyCustomFont(context, null);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context, attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context, attrs);
    }

    //TODO get attribute style to texteditswitcher
    private void applyCustomFont(Context context, AttributeSet attributeSet) {
        TypedArray attributeArray = context.obtainStyledAttributes(
                attributeSet,
                R.styleable.CustomFontTextView);

        try {
            int textStyle = attributeArray
                    .getInt(R.styleable.CustomFontTextView_textStyle, 0);

            Typeface customFont = selectTypeface(context, textStyle);

            setTypeface(customFont);
        } finally {
            attributeArray.recycle();
        }
    }

    private Typeface selectTypeface(Context context, int textStyle) {

        if (textStyle == 1) {
            return FontCache.getTypeface("Lato-Semibold.ttf", context);
        } else if (textStyle == 2) {
            return FontCache.getTypeface("Lato-LightItalic.ttf", context);
        } else {
            return FontCache.getTypeface("Lato-Light.ttf", context);
        }
    }
}
