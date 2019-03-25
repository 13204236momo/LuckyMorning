package com.example.zhoumohan.luckymorning.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.zhoumohan.luckymorning.R;
import com.example.zhoumohan.luckymorning.base.BaseActivity;
import com.example.zhoumohan.luckymorning.common.widget.ParticleBlastView;
import com.example.zhoumohan.luckymorning.common.widget.SplashView;
import com.example.zhoumohan.luckymorning.common.widget.titanic.Titanic;
import com.example.zhoumohan.luckymorning.common.widget.titanic.TitanicTextView;
import com.example.zhoumohan.luckymorning.main.MainActivity;
import com.example.zhoumohan.luckymorning.util.StatusBarUtils;
import com.example.zhoumohan.luckymorning.util.Typefaces;

public class StartActivity extends BaseActivity implements View.OnClickListener{

    private TitanicTextView tvStart;
    private TextView tvJump;

    private Titanic titanic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new SplashView(this));
        //contentView(R.layout.activity_start);
        //initUI();
    }
    private void initUI(){
        StatusBarUtils.setTranslucentForImageViewInFragment(this, 0);

        tvStart = findBy(R.id.tv_start);
        tvJump = findBy(R.id.tv_jump);
        tvStart.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));
        titanic = new Titanic();
        titanic.start(tvStart);

        tvJump.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_jump:
                startActivity(new Intent(this, MainActivity.class));
                titanic.cancel();
                finish();
                break;
        }
    }
}