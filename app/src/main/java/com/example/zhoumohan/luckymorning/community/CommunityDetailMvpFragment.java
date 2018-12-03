package com.example.zhoumohan.luckymorning.community;

import android.os.Bundle;
import android.view.View;

import com.example.zhoumohan.luckymorning.R;
import com.example.zhoumohan.luckymorning.base.BaseMvpFragment;


import butterknife.ButterKnife;

public class CommunityDetailMvpFragment extends BaseMvpFragment<CommunityPresenter,CommunityContract.Model>implements CommunityContract.View {



    @Override
    protected int getLayoutResID() {
        return R.layout.community_detail_fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this,view);


    }

    @Override
    public void upDate() {

    }
}
