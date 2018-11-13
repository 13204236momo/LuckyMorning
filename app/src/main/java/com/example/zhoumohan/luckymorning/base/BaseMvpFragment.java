package com.example.zhoumohan.luckymorning.base;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhoumohan.luckymorning.base.mvp.BaseModel;
import com.example.zhoumohan.luckymorning.base.mvp.BasePresenter;
import com.example.zhoumohan.luckymorning.base.mvp.BaseView;
import com.example.zhoumohan.luckymorning.base.mvp.TUtil;


/**
 * Created by DN on 2018/03/12.
 */

public abstract class BaseMvpFragment<T extends BasePresenter, E extends BaseModel> extends BaseFragment {
    public T mPresenter;
    public E mModel;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (this instanceof BaseView) mPresenter.setVM(this, mModel);
    }

}