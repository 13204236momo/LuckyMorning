package com.example.zhoumohan.luckymorning.community;

import com.example.zhoumohan.luckymorning.base.mvp.BaseModel;
import com.example.zhoumohan.luckymorning.base.mvp.BasePresenter;
import com.example.zhoumohan.luckymorning.base.mvp.BaseView;

public interface CommunityContract {
    interface View extends BaseView {
        void upDate();

    }

    interface Model extends BaseModel {

        void upDate();

    }

    abstract class Presenter extends BasePresenter<Model,View> {

    }
}
