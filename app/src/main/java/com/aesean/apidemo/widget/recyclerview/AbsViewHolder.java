package com.aesean.apidemo.widget.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    private OnItemClickListener<T> mOnItemClickListener;

    public interface OnItemClickListener<T> {
        boolean onClick(T data);
    }

    protected AbsViewHolder(View itemView) {
        super(itemView);
    }

    public AbsViewHolder(ViewGroup parentView, @LayoutRes int layoutRes) {
        this(LayoutInflater.from(parentView.getContext()).inflate(layoutRes, parentView, false));
        initView(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null && mOnItemClickListener.onClick(getData())) {
                    return;
                }
                onItemClick();
            }
        });
    }

    protected View getItemView() {
        return itemView;
    }

    @CallSuper
    protected void attach(AbsMultiTypeAdapter adapter) {
        mAdapter = adapter;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    protected void onItemClick() {

    }

    protected static void updateTextView(TextView textView, String text) {
        // 相同不再更新，避免闪烁
        if (!textView.getText().toString().equals(text)) {
            textView.setText(text);
        }
    }

    protected static void updateViewVisible(View view, int visible) {
        // 相同不再更新，避免闪烁
        if (view.getVisibility() != visible) {
            view.setVisibility(visible);
        }
    }

    protected String getString(@StringRes int resId) {
        return getContext().getString(resId);
    }

    protected String getString(@StringRes int resId, Object... formatArgs) {
        return getContext().getString(resId, formatArgs);
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

    protected Activity getActivity() {
        return (Activity) itemView.getContext();
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
