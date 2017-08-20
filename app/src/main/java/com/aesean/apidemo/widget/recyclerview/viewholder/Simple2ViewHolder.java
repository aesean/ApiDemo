package com.aesean.apidemo.widget.recyclerview.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aesean.apidemo.R;
import com.aesean.apidemo.widget.recyclerview.AbsViewHolder;

/**
 * Simple2ViewHolder
 *
 * @author xl
 * @version 1.0
 * @since 14/08/2017
 */

public class Simple2ViewHolder<T extends Simple2ViewHolder.Data> extends AbsViewHolder<T> {

    private TextView mItemTitle;
    private TextView mItemContent;

    public Simple2ViewHolder(ViewGroup parentView) {
        super(parentView, R.layout.view_holder_simple2);
    }

    @Override
    protected void initView(View itemView) {
        super.initView(itemView);
        mItemTitle = (TextView) findViewById(R.id.item_title);
        mItemContent = (TextView) findViewById(R.id.item_content);
    }

    @Override
    protected void bindData(T data) {
        mItemTitle.setText(data.getTitle());
        mItemContent.setText(data.getContent());
    }

    public interface Data {
        String getTitle();

        String getContent();
    }
}
