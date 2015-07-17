package com.facebook.android.projectcrawfish;// Copyright 2004-present Facebook. All Rights Reserved.

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class CustomFontEditText extends EditText {
    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public CustomFontEditText(Context context) {
        super(context);
        applyCustomFont(context, null);
    }

    public CustomFontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context, attrs);
    }

    public CustomFontEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context, attrs);
    }

    private void applyCustomFont(Context context, AttributeSet attributeSet) {
        int textStyle = attributeSet.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", 0);

        Typeface customFont = selectTypeface(context, textStyle);
        setTypeface(customFont);
    }

    private Typeface selectTypeface(Context context, int textStyle) {

        switch (textStyle) {
            // bold
            case 1:
                return FontCache.getTypeface("Lato-Semibold.ttf", context);
            // italic
            case 2:
                return FontCache.getTypeface("Lato-LightItalic.ttf", context);
            //regular
            case 0:
            default:
                return FontCache.getTypeface("Lato-Light.ttf", context);
        }
    }
}
