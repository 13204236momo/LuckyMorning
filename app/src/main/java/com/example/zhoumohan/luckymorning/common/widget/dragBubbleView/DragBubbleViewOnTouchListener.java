package com.example.zhoumohan.luckymorning.common.widget.dragBubbleView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.TextView;

public class DragBubbleViewOnTouchListener implements View.OnTouchListener, DragBubbleView.StateChangeListener {
    private DragBubbleView dragBubbleView1;
    private WindowManager windowManager;
    private WindowManager.LayoutParams wmParams;
    private int radius = 15;
    private TextView textView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DragBubbleViewOnTouchListener(Activity context) {
        dragBubbleView1 = new DragBubbleView(context);
        dragBubbleView1.setTextStr("19");
        dragBubbleView1.setTextSize(20);
        dragBubbleView1.setTextColor(Color.WHITE);
        dragBubbleView1.setBubbleRadius(radius);
        dragBubbleView1.setSateChangeListener(this);

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        wmParams.format = PixelFormat.TRANSLUCENT;//使窗口支持透明度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            wmParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        textView = (TextView) v;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ViewParent parent = v.getParent();
            // 请求其父级View不拦截Touch事件
            parent.requestDisallowInterceptTouchEvent(true);
            int[] location = new int[2];
            v.getLocationOnScreen(location);
            dragBubbleView1.init(location[0] + v.getWidth() / 2, location[1] + v.getHeight() / 2);
            dragBubbleView1.setTextStr(textView.getText().toString());
            windowManager.addView(dragBubbleView1, wmParams);
            textView.setVisibility(View.GONE);
        }
        dragBubbleView1.onTouchEvent(event);
        return true;
    }

    @Override
    public void onDefault() {
        windowManager.removeView(dragBubbleView1);
        textView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConnect() {

    }

    @Override
    public void onApart() {

    }

    @Override
    public void onDismiss() {
        windowManager.removeView(dragBubbleView1);
    }
}