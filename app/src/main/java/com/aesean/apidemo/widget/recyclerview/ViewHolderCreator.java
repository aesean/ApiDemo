package com.aesean.apidemo.widget.recyclerview;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

/**
 * ViewHolderCreator
 *
 * @author danny
 * @since 03/09/2017
 */
public interface ViewHolderCreator<M, N extends AbsViewHolder<M>> {
    N create(ViewGroup parent);

    @NonNull
    Class<N> getViewHolderClass();
}
