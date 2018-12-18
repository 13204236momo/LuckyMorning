package com.example.zhoumohan.luckymorning.common.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


public class FontIconView extends AppCompatTextView {
    /*
     * 控件在xml加载的时候是调用两个参数的构造函数 ，为了自定义的控件的完整性我们可以
     * 都把构造函数写出来
     */
    public FontIconView(Context context) {
        super(context);
        init(context);
    }

    public FontIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FontIconView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * 初始化
     * @param context
     */
    private void init(Context context) {
        //设置字体图标
        Typeface font = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
        this.setTypeface(font);
    }
}
