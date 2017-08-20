package com.aesean.apidemo.widget.recyclerview;

/**
 * ViewType
 *
 * @author xl
 * @since 2017-08-05
 */
public interface ViewType {
    Class<? extends AbsViewHolder<?>> getViewType();
}
