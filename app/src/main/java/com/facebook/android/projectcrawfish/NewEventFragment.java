// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

public class NewEventFragment extends FrameFragment<EventEditorFragment> {

    public void saveToParse() {
        getFragment().saveToParse();
    }

    @Override
    protected EventEditorFragment getNewFragmentInstance() {
        return EventEditorFragment.newInstance(null);
    }
}
