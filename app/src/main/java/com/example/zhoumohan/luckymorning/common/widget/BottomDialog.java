package com.example.zhoumohan.luckymorning.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhoumohan.luckymorning.R;
import com.example.zhoumohan.luckymorning.demo.CouponAdapter;
import com.example.zhoumohan.luckymorning.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

public class BottomDialog extends Dialog {
    private LinearLayout llContent;
    private TextView tvClose;
    private ListView lvCoupon;
    private CouponAdapter adapter;

    public BottomDialog(@NonNull Context context) {
        super(context);
    }

    public BottomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_dialog);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.BottomDialog_Animation);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        initView();
    }

    private void initView() {
        llContent = findViewById(R.id.ll_content);
        tvClose = findViewById(R.id.tv_close);
        lvCoupon = findViewById(R.id.lv_coupon);


        List<String> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add("标题" + i);
        }

        adapter = new CouponAdapter(getContext(),list);
        lvCoupon.setAdapter(adapter);

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });

        llContent.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int light, int bottom, int oldLeft, int oldTop, int oldLight, int oldBottom) {
                int height = view.getHeight();
                int needHeight = DisplayUtil.getRealScreenRelatedInformation(getContext())[1]/2;
                if (height > needHeight) {
                    //注意：这里的 LayoutParams 必须是 FrameLayout的！！
                    view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, needHeight));
                }
            }
        });
    }


}
