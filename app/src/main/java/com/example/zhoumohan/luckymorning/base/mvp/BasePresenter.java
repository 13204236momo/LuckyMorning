package com.example.zhoumohan.luckymorning.base.mvp;

import android.content.Context;

/**
 * Created by Administrator on 2018/4/3 0003.
 */
public abstract class BasePresenter<M, T> {
    public Context context;
    public M mModel;
    public T mView;
   // public RxManager mRxManager = new RxManager();

    public void setVM(T v, M m) {
        this.mView = v;
        this.mModel = m;
        this.onStart();

    }

    public abstract void onStart();

    public void onDestroy() {
       // mRxManager.clear();
    }
}