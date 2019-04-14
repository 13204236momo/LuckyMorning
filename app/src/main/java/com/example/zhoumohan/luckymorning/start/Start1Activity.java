package com.example.zhoumohan.luckymorning.start;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.zhoumohan.luckymorning.R;
import com.example.zhoumohan.luckymorning.base.BaseActivity;
import com.example.zhoumohan.luckymorning.common.widget.parallaxView.ParallaxContainer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Start1Activity extends BaseActivity {
    @BindView(R.id.ParallaxContainer)
    ParallaxContainer parallaxContainer;
    @BindView(R.id.iv_person)
    ImageView ivPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start1);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        parallaxContainer.setUp(
                R.layout.view_intro_1,
                R.layout.view_intro_2,
                R.layout.view_intro_3,
                R.layout.view_intro_4,
                R.layout.view_intro_5,
                R.layout.view_login
                );

        ivPerson.setBackgroundResource(R.drawable.man_run);
        parallaxContainer.setIvPerson(ivPerson);
    }



}
