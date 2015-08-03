// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;// Copyright 2004-present Facebook. All Rights Reserved.

import android.app.Activity;
import android.view.View;

import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ContactListFragment extends ListFragment<ParseUser> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseQueryAdapter.QueryFactory<ParseUser> queryFactory =
                new ParseQueryAdapter.QueryFactory<ParseUser>() {
                    public ParseQuery<ParseUser> create() {
                        return getQuery();
                    }
                };

        mAdapter = new ContactPQA(
                getActivity(),
                queryFactory,
                this,
                getHolderFactory(),
                getListItemResID()
        );
        mAdapter.loadObjects();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        mListView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        mListView.setScrollbarFadingEnabled(false);
        mListView.setFastScrollAlwaysVisible(true);
        return v;
    }

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
