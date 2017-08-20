package com.aesean.apidemo.widget.recyclerview;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aesean.apidemo.BuildConfig;

/**
 * AbsViewHolder
 *
 * @author xl
 * @since 2017-08-04
 */
public abstract class AbsViewHolder<T> extends RecyclerView.ViewHolder {
    protected static final boolean DEBUG_MODE = BuildConfig.DEBUG;
    protected final String TAG = this.getClass().getName();

    private AbsMultiTypeAdapter mAdapter;
    private T mData;
    private boolean mRemoved = false;

    protected AbsViewHolder(View itemView) {
        super(itemView);
    }

    protected AbsViewHolder(ViewGroup parentView, @LayoutRes int layoutRes) {
        this(LayoutInflater.from(parentView.getContext()).inflate(layoutRes, parentView, false));
    }

    protected View getItemView() {
        return itemView;
    }

    @CallSuper
    protected void attach(AbsMultiTypeAdapter adapter) {
        mAdapter = adapter;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick();
            }
        });
        initView(itemView);
    }

    protected void onItemClick() {

    }

    protected void removeMySelf() {
        if (!mRemoved) {
            mAdapter.removeItem(getAdapterPosition());
            mData = null;
        }
        mRemoved = true;
    }

    protected boolean isRemoved() {
        return mRemoved;
    }

    protected Context getContext() {
        return itemView.getContext();
    }

    protected void update() {
        mAdapter.updateItem(mData);
    }

    protected void update(Object object) {
        mAdapter.updateItem(getAdapterPosition(), object);
    }

    protected View findViewById(@IdRes int id) {
        return itemView.findViewById(id);
    }

    protected void initView(View itemView) {

    }

    void bindDataSafe(@NonNull Object data) {
        T t;
        try {
            //noinspection unchecked
            t = (T) data;
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("无法将" + data.getClass().toString() + "绑定到" + this.getClass().toString());
        }
        mData = t;
        mRemoved = false;
        bindData(t);
    }

    protected abstract void bindData(T data);

    public T getData() {
        return mData;
    }

    protected final AbsMultiTypeAdapter getAdapter() {
        return mAdapter;
    }
}
