package com.example.zhoumohan.luckymorning.demo;

import android.os.Bundle;

import com.example.zhoumohan.luckymorning.R;
import com.example.zhoumohan.luckymorning.base.BaseActivity;
import com.example.zhoumohan.luckymorning.common.widget.CircleView;

public class CircleViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new CircleView(this));
    }

}
