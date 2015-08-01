// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;// Copyright 2004-present Facebook. All Rights Reserved.

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactListFragment extends ListFragment<ParseUser> {


    @Override
    protected ParseQuery<ParseUser> getQuery() {
        ParseQuery<Swipe> queryB = ParseQuery.getQuery(Swipe.class);
        queryB.whereEqualTo(Swipe.SWIPEE, ParseUser.getCurrentUser());
        queryB.whereEqualTo(Swipe.IS_LEFT_SWIPE, false);

        ParseQuery<Swipe> queryA = ParseQuery.getQuery(Swipe.class);
        queryA.whereEqualTo(Swipe.SWIPER, ParseUser.getCurrentUser());
        queryA.whereEqualTo(Swipe.IS_LEFT_SWIPE, false);//People you swiped right
        queryA.whereMatchesKeyInQuery(Swipe.SWIPEE, Swipe.SWIPER, queryB);//People who swiped right on you

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereMatchesKeyInQuery("objectId","swipeeID",queryA);
        query.addAscendingOrder(Profile.LAST_NAME);

        return query;
    }

    @Override
    protected CustomViewPQA.CustomViewHolderFactory<ParseUser> getHolderFactory() {
        return new CustomViewPQA.CustomViewHolderFactory<ParseUser>() {
            @Override
            public CustomViewPQA.CustomViewHolder<ParseUser> create(View v, CustomViewPQA.CustomViewHolder.ClickEventListener<ParseUser> listener) {
                return new ContactViewHolder(v, listener);
            }
        };
    }

    @Override
    protected int getListItemResID() {
        return R.layout.list_item_contact;
    }


    public interface OnFragmentInteractionListener {
        void openContactDetails(ParseUser user);
    }

    private OnFragmentInteractionListener mListener;

    @Override
    public void OnClick(ParseUser user) {
        mListener.openContactDetails(user);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
