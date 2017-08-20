package com.aesean.apidemo.activity.constraint;

import android.os.Bundle;
import android.widget.Toast;

import com.aesean.apidemo.R;
import com.aesean.apidemo.base.BaseActivity;


public class ConstraintLayoutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraint_layout);
        Toast.makeText(this, "这里展示了如何用ConstraintLayout来处理复杂布局。详细的请参考具体layout xml代码。", Toast.LENGTH_LONG).show();
    }
}
