package com.aesean.apidemo.activity.recyclerview;

import android.view.ViewGroup;
import android.widget.Toast;

import com.aesean.apidemo.widget.recyclerview.viewholder.Simple2ViewHolder;

/**
 * Type0ViewHolder
 *
 * @author xl
 * @version 1.0
 * @since 16/08/2017
 */

public class Type1ViewHolder extends Simple2ViewHolder<Type1ViewHolder.Data> {
    public Type1ViewHolder(ViewGroup parentView) {
        super(parentView);
    }

    @Override
    protected void onItemClick() {
        super.onItemClick();
        Toast.makeText(getContext(), "点击了" + getData().getTitle(), Toast.LENGTH_LONG).show();
        removeMySelf();
    }

    public static class Data implements Simple2ViewHolder.Data {
        private String mTitle;
        private String mContent;

        public Data(String title, String content) {
            mTitle = title;
            mContent = content;
        }

        @Override
        public String getTitle() {
            return mTitle;
        }

        @Override
        public String getContent() {
            return mContent;
        }
    }
}
