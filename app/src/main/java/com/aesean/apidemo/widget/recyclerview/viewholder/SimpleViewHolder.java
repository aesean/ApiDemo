package com.aesean.apidemo.widget.recyclerview.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aesean.apidemo.R;
import com.aesean.apidemo.widget.recyclerview.AbsViewHolder;

/**
 * SimpleViewHolder
 *
 * @author xl
 * @version 1.0
 * @since 14/08/2017
 */

public class SimpleViewHolder<T extends SimpleViewHolder.Data> extends AbsViewHolder<T> {

    private TextView mItemName;
    private FrameLayout mItemContainer;

    public SimpleViewHolder(ViewGroup parentView) {
        super(parentView, R.layout.view_holder_simple);
    }

    @Override
    protected void initView(View itemView) {
        super.initView(itemView);
        mItemName = (TextView) findViewById(R.id.item_title);
        mItemContainer = (FrameLayout) findViewById(R.id.item_container);
    }

    @Override
    protected void bindData(T data) {
        mItemName.setText(data.getTitle());
    }

    public interface Data {
        String getTitle();
    }
}
