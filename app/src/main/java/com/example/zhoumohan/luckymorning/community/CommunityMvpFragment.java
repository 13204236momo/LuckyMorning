package com.example.zhoumohan.luckymorning.community;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.zhoumohan.luckymorning.R;
import com.example.zhoumohan.luckymorning.base.BaseMvpFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommunityMvpFragment extends BaseMvpFragment<CommunityPresenter,CommunityContract.Model>implements CommunityContract.View {

    @BindView(R.id.txt_vp_item_page)
    TextView textView;

    @Override
    protected int getLayoutResID() {
        return R.layout.item_vp;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this,view);
        textView.setText("666");

    }

    @Override
    public void upDate() {

    }
}
