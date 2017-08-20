package com.aesean.apidemo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aesean.apidemo.application.App;
import com.aesean.apidemo.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new Adapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });
    }

    private void init() {
        PackageInfo packageInfo;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.get_package_info_error, Toast.LENGTH_LONG).show();
            return;
        }
        List<Data> list = new ArrayList<>();
        for (ActivityInfo activity : packageInfo.activities) {
            // 不显示自身Activity
            if (MainActivity.this.getClass().getName().equals(activity.name)) {
                continue;
            }
            list.add(new DataImpl(activity));
        }
        mAdapter.add(list);
    }


    private class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private ArrayList<Data> mList;

        Adapter() {
            mList = new ArrayList<>();
        }

        void add(List<Data> list) {
            if (list == null || list.size() < 1) {
                return;
            }
            int size = mList.size();
            mList.addAll(list);
            notifyItemRangeInserted(size, list.size());
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Data data = mList.get(position);
            holder.setData(data);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            Data mData;
            TextView mTitleView;
            TextView mContentView;

            ViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_activity_list, parent, false));
                mTitleView = (TextView) itemView.findViewById(R.id.item_title);
                mContentView = (TextView) itemView.findViewById(R.id.item_content);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setPackage(App.getPackageName());
                        intent.setClassName(App.getPackageName(), mData.getTargetName());
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            Log.e(TAG, "start activity error. name = " + mData.getTargetName());
                            e.printStackTrace();
                            Toast.makeText(view.getContext(), getString(R.string.open_activity_error) + "name = " + mData.getTargetName(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            void setData(@NonNull Data data) {
                mData = data;
                mTitleView.setText(data.getTitle());
                mContentView.setText(data.getContent());
            }

        }
    }

    interface Data {
        String getTitle();

        String getContent();

        String getTargetName();
    }

    private static class DataImpl implements Data {

        ActivityInfo mActivityInfo;

        DataImpl(ActivityInfo activity) {
            mActivityInfo = activity;
        }

        @Override
        public String getTitle() {
            return mActivityInfo.name.substring(mActivityInfo.name.lastIndexOf(".") + 1);
        }

        @Override
        public String getContent() {
            return mActivityInfo.name;
        }

        @Override
        public String getTargetName() {
            return mActivityInfo.name;
        }
    }
}
