// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;


public class CustomViewPQA<T extends ParseObject> extends ParseQueryAdapter<T>  {


    public static abstract class CustomViewHolder<Q> {
        interface ClickEventListener<X> {
            void onClick(X obj);
        }

        abstract void bindObject(Q obj);

        private final ClickEventListener<Q> mListener;

        public CustomViewHolder(ClickEventListener<Q> listener) {
            mListener = listener;
        }

        protected void onClick(Q object) {
            mListener.onClick(object);
        }
    }

    abstract public static class CustomViewHolderFactory<Y> {
        abstract public CustomViewHolder<Y> create(View v, CustomViewHolder.ClickEventListener<Y> listener);
    }

    interface ClickEventListener<A> {
        void OnClick(A obj);
    }

    private final int mViewResource;
    private final ClickEventListener<T> mListener;
    private final CustomViewHolderFactory<T> mHolderFactory;

    public CustomViewPQA(Context context, QueryFactory<T> queryFactory, ClickEventListener<T> listener, CustomViewHolderFactory<T> holderFactory, int viewResource
    ) {
        super(context, queryFactory);
        mHolderFactory = holderFactory;
        mListener = listener;
        mViewResource = viewResource;
        this.setAutoload(false);

    }


    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(T object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), mViewResource, null);
        }

        super.getItemView(object, v, parent);

        CustomViewHolder<T> holder = mHolderFactory.create(v, new CustomViewHolder.ClickEventListener<T>() {
            @Override
            public void onClick(T obj) {
                mListener.OnClick(obj);
            }
        });
        holder.bindObject(object);

        return v;
    }

}
