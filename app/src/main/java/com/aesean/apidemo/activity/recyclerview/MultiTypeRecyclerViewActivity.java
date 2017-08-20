package com.aesean.apidemo.activity.recyclerview;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aesean.apidemo.R;
import com.aesean.apidemo.base.BaseActivity;
import com.aesean.apidemo.widget.recyclerview.MultiTypeAdapterImpl;

import java.util.ArrayList;
import java.util.List;

import static com.aesean.apidemo.application.App.showLongMessage;

public class MultiTypeRecyclerViewActivity extends BaseActivity {
    private MultiTypeAdapterImpl mAdapter;
    private TextView mResultView;

    private static final String[] VIEW_TYPE = new String[]{"VIEW0", "VIEW1", "VIEW2"};
    private static final String[] TYPE = new String[]{"HEAD", "CONTENT", "FOOT"};

    private int mViewType = 0;
    private int mType = 0;
    private int mDataCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_type_recycler_view);

        mResultView = (TextView) findViewById(R.id.result);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mAdapter = new MultiTypeAdapterImpl();
        mAdapter.register(Type0ViewHolder.Data.class, Type0ViewHolder.class);
        mAdapter.register(Type1ViewHolder.Data.class, Type1ViewHolder.class);
        mAdapter.register(Type2ViewHolder.Data.class, Type2ViewHolder.class);
        recyclerView.setAdapter(mAdapter);
        update();
    }

    private static final int CONTENT = 1;

    private void addContnetData(Object data) {
        mAdapter.addItemByType(CONTENT, "String");
    }

    public void setView0(View view) {
        mViewType = 0;
        update();
    }

    public void setView1(View view) {
        mViewType = 1;
        update();
    }

    public void setView2(View view) {
        mViewType = 2;
        update();
    }

    public void setHead(View view) {
        mType = 0;
        update();
    }

    public void setContent(View view) {
        mType = 1;
        update();
    }

    public void setFoot(View view) {
        mType = 2;
        update();
    }

    public void addOne(View view) {
        mDataCount = 1;
        update();
    }

    public void addTwo(View view) {
        mDataCount = 2;
        update();
    }


    public void addThree(View view) {
        mDataCount = 3;
        update();
    }

    public void addFour(View view) {
        mDataCount = 4;
        update();
    }

    public void addFive(View view) {
        mDataCount = 5;
        update();
    }


    private void update() {
        mResultView.setText("添加" + mDataCount + "个-->>" + VIEW_TYPE[mViewType] + "-->>到" + TYPE[mType]);
    }

    private static int sIndex = 0;

    public void run(View view) {
        if (mType < 0 || mType > 3) {
            showLongMessage("unknown type, Type = " + mType);
            return;
        }
        if (mViewType == 0) {
            if (mDataCount == 1) {
                sIndex++;
                final Object object = new Type0ViewHolder.Data(TYPE[mType] + ">>>Title--" + sIndex);
                mAdapter.addItemByType(mType, object);
            } else {
                final List<Object> list = new ArrayList<>();
                for (int i = 0; i < mDataCount; i++) {
                    sIndex++;
                    list.add(new Type0ViewHolder.Data(TYPE[mType] + ">>>Title--" + sIndex));
                }
                mAdapter.addAllItemByType(mType, list);
            }
        } else if (mViewType == 1) {
            sIndex++;
            final String content = "Content Content Content Content Content Content Content Content Content Content Content Content Content Content Content Content Content Content ";
            if (mDataCount == 1) {
                sIndex++;
                final Object object = new Type1ViewHolder.Data(TYPE[mType] + ">>>Title--" + sIndex, "Content");
                mAdapter.addItemByType(mType, object);
            } else {
                final List<Object> list = new ArrayList<>();
                for (int i = 0; i < mDataCount; i++) {
                    sIndex++;
                    list.add(new Type1ViewHolder.Data(TYPE[mType] + ">>>Title--" + sIndex, content));
                }
                mAdapter.addAllItemByType(mType, list);
            }
        } else if (mViewType == 2) {
            sIndex++;
            final String content = "Content Content Content Content Content Content Content Content Content Content Content Content Content Content Content Content Content Content ";
            if (mDataCount == 1) {
                sIndex++;
                final Object object = new Type2ViewHolder.DataImpl(TYPE[mType] + ">>>Title--" + sIndex, "Content");
                mAdapter.addItemByType(mType, object);
            } else {
                final List<Object> list = new ArrayList<>();
                for (int i = 0; i < mDataCount; i++) {
                    sIndex++;
                    list.add(new Type2ViewHolder.DataImpl(TYPE[mType] + ">>>Title--" + sIndex, content));
                }
                mAdapter.addAllItemByType(mType, list);
            }
        } else {
            showLongMessage("unknown view type, ViewType = " + mViewType);
        }
    }

    public void removeByType(View view) {
        mAdapter.removeAllItemByType(mType);
    }

    public void checkDataType(View view) {
        int count = 0;
        try {
            mAdapter.addItem(0, "Exception");
        } catch (Exception e) {
            e.printStackTrace();
            count++;
        }
        try {
            mAdapter.addItem(0, 0, "Exception");
        } catch (Exception e) {
            e.printStackTrace();
            count++;
        }
        try {
            mAdapter.addItem("Exception");
        } catch (Exception e) {
            e.printStackTrace();
            count++;
        }
        try {
            mAdapter.addItemByType(0, "Exception");
        } catch (Exception e) {
            e.printStackTrace();
            count++;
        }
        try {
            List<String> list = new ArrayList<>();
            list.add("Exception");
            mAdapter.addAllItem(0, list);
        } catch (Exception e) {
            e.printStackTrace();
            count++;
        }
        try {
            List<String> list = new ArrayList<>();
            list.add("Exception");
            mAdapter.addAllItem(0, list);
        } catch (Exception e) {
            e.printStackTrace();
            count++;
        }
        try {
            List<String> list = new ArrayList<>();
            list.add("Exception");
            mAdapter.addAllItem(0, 0, list);
        } catch (Exception e) {
            e.printStackTrace();
            count++;
        }
        try {
            List<String> list = new ArrayList<>();
            list.add("Exception");
            mAdapter.addAllItem(list);
        } catch (Exception e) {
            e.printStackTrace();
            count++;
        }
        try {
            List<String> list = new ArrayList<>();
            list.add("Exception");
            mAdapter.addAllItemByType(0, list);
        } catch (Exception e) {
            e.printStackTrace();
            count++;
        }
        try {
            List<String> list = new ArrayList<>();
            list.add("Exception");
            mAdapter.addAllItem(list.toArray());
        } catch (Exception e) {
            e.printStackTrace();
            count++;
        }
        showLongMessage("检查" + (count == 10 ? "Success" : "Fail") + "，测试10个，捕获到类型异常" + count + "个。");
    }
}
