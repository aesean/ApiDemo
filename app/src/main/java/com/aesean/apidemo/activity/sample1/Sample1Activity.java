package com.aesean.apidemo.activity.sample1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.aesean.apidemo.base.BaseActivity;

/**
 * Sample1Activity
 *
 * @author xl
 * @version 1.0
 * @since 20/08/2017
 */

public class Sample1Activity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText(this.getClass().getSimpleName());
        textView.setTextSize(48);
        setContentView(textView);
    }
}
