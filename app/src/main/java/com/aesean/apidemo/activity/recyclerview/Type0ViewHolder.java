package com.aesean.apidemo.activity.recyclerview;

import android.view.ViewGroup;
import android.widget.Toast;

import com.aesean.apidemo.widget.recyclerview.viewholder.SimpleViewHolder;

/**
 * Type0ViewHolder
 *
 * @author xl
 * @version 1.0
 * @since 16/08/2017
 */

public class Type0ViewHolder extends SimpleViewHolder<Type0ViewHolder.Data> {

    public Type0ViewHolder(ViewGroup parentView) {
        super(parentView);
    }

    @Override
    protected void onItemClick() {
        super.onItemClick();
        if (!isRemoved()) {
            Toast.makeText(getContext(), "点击了" + getData().getTitle(), Toast.LENGTH_LONG).show();
            removeMySelf();
        }
    }

    public static class Data implements SimpleViewHolder.Data {
        private String mTitle;

        public Data(String title) {
            mTitle = title;
        }

        @Override
        public String getTitle() {
            return mTitle;
        }
    }
}
