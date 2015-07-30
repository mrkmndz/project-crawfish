// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by markamendoza on 7/22/15.
 */
abstract public class ListFragment<T extends ParseObject> extends Fragment
        implements CustomViewPQA.ClickEventListener<T>, ParseQueryAdapter.OnQueryLoadListener<T> {

    private boolean mHasLoaded;

    private CustomViewPQA<T> mAdapter;

    @Bind(R.id.list_view)
    ListView mListView;
    @Bind(R.id.progress_switcher)
    ProgressSwitcher mSwitcher;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    abstract protected ParseQuery<T> getQuery();
    abstract protected CustomViewPQA.CustomViewHolderFactory<T> getHolderFactory();
    abstract protected int getListItemResID();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseQueryAdapter.QueryFactory<T> queryFactory =
                new ParseQueryAdapter.QueryFactory<T>() {
                    public ParseQuery<T> create() {
                        return getQuery();
                    }
                };

        mAdapter = new CustomViewPQA<>(
                getActivity(),
                queryFactory,
                this,
                getHolderFactory(),
                getListItemResID()
        );
        mAdapter.loadObjects();

    }

    public void refreshList() {
        mAdapter.loadObjects();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, v);

        if (mHasLoaded) {
            mSwitcher.showContent();
        } else {
            mSwitcher.showBar();
        }
        mAdapter.addOnQueryLoadListener(this);

        mListView.setAdapter(mAdapter);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });

        return v;
    }

    @Override
    public void onLoading() {
        if (!mHasLoaded && mSwitcher!=null){
            mSwitcher.showBar();
        }
    }

    @Override
    public void onLoaded(List<T> list, Exception e) {
        mHasLoaded = true;
        if (mSwitcher != null) mSwitcher.showContent();
        if (mSwipeRefreshLayout != null) mSwipeRefreshLayout.setRefreshing(false);
    }
}
