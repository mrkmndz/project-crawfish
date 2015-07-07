// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

public class CustomViewPQA extends ParseQueryAdapter<ParseObject> {

    private final int mViewResource;

    public static abstract class CustomViewHolder{

        private final ClickEventListenerd mListenerd;

        interface ClickEventListenerd{
            void onClick(ParseObject obj);
        }
        public CustomViewHolder(View v, ClickEventListenerd listenerd){
            mListenerd = listenerd;
        }
        abstract void bindObject(ParseObject obj);
        protected void onClick(ParseObject object){
            mListenerd.onClick(object);
        }
    }

    abstract public static class CustomViewHolderFactory{
        abstract public CustomViewHolder create(View v, CustomViewHolder.ClickEventListenerd listener);
    }

    private final ClickEventListener mListener;
    private CustomViewHolderFactory mHolderFactory;

    public CustomViewPQA(Context context, QueryFactory<ParseObject> queryFactory, ClickEventListener listener, CustomViewHolderFactory holderFactory, int viewResource
    ) {
        super(context, queryFactory);
        mHolderFactory = holderFactory;
        mListener = listener;
        mViewResource = viewResource;
    }

    interface ClickEventListener{
        void OnClick(ParseObject obj);
    }

    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), mViewResource, null);
        }

        super.getItemView(object, v, parent);

        CustomViewHolder holder = mHolderFactory.create(v, new CustomViewHolder.ClickEventListenerd() {
            @Override
            public void onClick(ParseObject obj) {
                mListener.OnClick(obj);
            }
        });
        holder.bindObject(object);

        return v;
    }

}
