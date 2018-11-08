package com.example.zhoumohan.luckymorning.start;

import android.os.Bundle;

import com.example.zhoumohan.luckymorning.R;
import com.example.zhoumohan.luckymorning.base.BaseActivity;
import com.example.zhoumohan.luckymorning.common.widget.titanic.Titanic;
import com.example.zhoumohan.luckymorning.common.widget.titanic.TitanicTextView;
import com.example.zhoumohan.luckymorning.util.StatusBarUtils;
import com.example.zhoumohan.luckymorning.util.Typefaces;

public class StartActivity extends BaseActivity {

    private TitanicTextView tvStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_start);


        initUI();
    }
    private void initUI(){
        StatusBarUtils.setTranslucentForImageViewInFragment(this, 0);

        tvStart = findBy(R.id.tv_start);
        tvStart.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));
        new Titanic().start(tvStart);

    }
}
