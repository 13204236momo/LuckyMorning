package com.example.zhoumohan.luckymorning.community;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.example.zhoumohan.luckymorning.R;
import com.example.zhoumohan.luckymorning.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddRecordActivity extends BaseActivity {

    @BindView(R.id.gv_item)
    GridView gridView;

    private AddRecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_add_recor);
        ButterKnife.bind(this);

        init();
    }

    private void init(){

    }


    public class AddRecordAdapter extends BaseAdapter{
        private String[] titles = {"动态","日记","计划","任务"};
        private String[] fontIcons = {""};

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Object getItem(int position) {
            return titles[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }

}
