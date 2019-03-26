package com.example.zhoumohan.luckymorning.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.zhoumohan.luckymorning.R;
import com.example.zhoumohan.luckymorning.base.BaseFragment;
import com.example.zhoumohan.luckymorning.common.widget.SplashView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFragment1 extends BaseFragment {

    @BindView(R.id.btn_success)
    Button btnSuccess;
    @BindView(R.id.splash)
    SplashView splashView;
    @Override
    protected int getLayoutResID() {
        return R.layout.item_vp1;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this,view);

        btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                splashView.setLoadingEnd();
            }
        });
    }

}
