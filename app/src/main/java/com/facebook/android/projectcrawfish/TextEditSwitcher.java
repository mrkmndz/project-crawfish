package com.facebook.android.projectcrawfish;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ViewSwitcher;

public class TextEditSwitcher extends ViewSwitcher {

    public static final int EDIT = 0;

    private CustomFontTextView mText;
    private CustomFontEditText mEdit;

    public TextEditSwitcher(Context context) {
        super(context);
        init(context);
    }

    public TextEditSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {

        mText = new CustomFontTextView(context);
        mEdit = new CustomFontEditText(context);
        mEdit.setId(EDIT);

        addView(mText);
        addView(mEdit);
    }

    public void showText() {
        if (getNextView().getId() != EDIT) {
            showNext();
        }
    }

    public void showEdit() {
        if (getNextView().getId() == EDIT) {
            showNext();
        }
    }

    public void setDisplayedText(String string) {
        mText.setText(string);
        mEdit.setText(string);
    }

    public CustomFontTextView getTextView() {
        return mText;
    }

    public CustomFontEditText getEditText() {
        return mEdit;
    }

    public void setNameDisplay() {
        mText.setTextSize(32);
        mEdit.setTextSize(32);
        mText.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
        mEdit.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
        mText.setSingleLine();
        mEdit.setSingleLine();
    }

    public void setDisplay() {
        mText.setTextSize(18);
        mEdit.setTextSize(18);
        mText.setGravity(Gravity.CENTER_VERTICAL);
        mEdit.setGravity(Gravity.CENTER_VERTICAL);
        mText.setSingleLine();
        mEdit.setSingleLine();
    }
}