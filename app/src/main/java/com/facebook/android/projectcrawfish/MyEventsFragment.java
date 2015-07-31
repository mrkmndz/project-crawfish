// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.view.View;

import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by markamendoza on 7/31/15.
 */
public class MyEventsFragment extends  FrameFragment<MyEventsFragment.MyEventsList> {
    @Override
    protected MyEventsList getNewFragmentInstance() {
        return new MyEventsList();
    }

    public void refreshList(){
        getFragment().refreshList();
    }

    public static class MyEventsList extends ListFragment<Event> {
        @Override
        protected ParseQuery<Event> getQuery() {
            ParseQuery<Event> query = new ParseQuery<>(Event.CLASS_NAME);
            query.orderByAscending(Event.START_DATE);
            Date now = new Date();
            query.whereLessThan(Event.START_DATE, now);
            query.whereGreaterThan(Event.END_DATE, now);//Not over
            query.whereEqualTo(Event.CREATOR, ParseUser.getCurrentUser());
            return query;
        }

        @Override
        protected CustomViewPQA.CustomViewHolderFactory<Event> getHolderFactory() {
            return new CustomViewPQA.CustomViewHolderFactory<Event>() {
                @Override
                public CustomViewPQA.CustomViewHolder<Event> create(View v, CustomViewPQA.CustomViewHolder.ClickEventListener<Event> listener) {
                    return new EventViewHolder(v, listener);
                }
            };
        }

        @Override
        protected int getListItemResID() {
            return R.layout.event_list_item;
        }

        @Override
        public void OnClick(Event obj) {
            EventEditorFragment editor = EventEditorFragment.newInstance(obj);
            editor.show(getChildFragmentManager(), "EDITOR");
        }
    }
}
