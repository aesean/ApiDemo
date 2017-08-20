package com.aesean.apidemo.activity.recyclerview;

import android.support.annotation.DrawableRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aesean.apidemo.R;
import com.aesean.apidemo.widget.recyclerview.AbsViewHolder;

/**
 * Type2ViewHolder
 *
 * @author xl
 * @version 1.0
 * @since 18/08/2017
 */

public class Type2ViewHolder extends AbsViewHolder<Type2ViewHolder.Data> {
    private ImageView mIconView;
    private TextView mTitleView;
    private TextView mContentView;

    public Type2ViewHolder(ViewGroup parentView) {
        super(parentView, R.layout.view_holder_type2);
    }

    @Override
    protected void initView(View itemView) {
        super.initView(itemView);
        mIconView = (ImageView) findViewById(R.id.item_icon);
        mTitleView = (TextView) findViewById(R.id.item_title);
        mContentView = (TextView) findViewById(R.id.item_content);
    }

    @Override
    protected void onItemClick() {
        super.onItemClick();
        removeMySelf();
    }

    @Override
    protected void bindData(Data data) {
        mIconView.setImageResource(data.getDrawableRes());
        mTitleView.setText(data.getTitle());
        mContentView.setText(data.getContent());
    }

    public interface Data {
        @DrawableRes
        int getDrawableRes();

        String getTitle();

        String getContent();
    }

    public static class DataImpl implements Data {
        @DrawableRes
        private int mIconRes;
        private String mTitle, mContent;

        public DataImpl(String title, String content) {
            this(R.mipmap.ic_launcher, title, content);
        }

        public DataImpl(int iconRes, String title, String content) {
            mIconRes = iconRes;
            mTitle = title;
            mContent = content;
        }

        @Override
        public int getDrawableRes() {
            return mIconRes;
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
