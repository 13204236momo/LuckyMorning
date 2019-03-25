package com.example.zhoumohan.luckymorning.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.zhoumohan.luckymorning.R;

public class SplashView extends View {

    /**
     * 旋转圆的圆心
     */
    private float mCenterX;
    private float mCenterY;
    /**
     * 旋转半径
     */
    private float mRotateRadius = 90;
    /**
     * 画小球的画笔
     */
    private Paint mPaint;

    private float mCircleRadius = 19;
    /**
     * 小球颜色
     */
    private int[] colors = {Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE, Color.GREEN, Color.GRAY};

    /**
     * 水波纹半径
     */
    private Paint mHolePaint;

    /**
     * 画布背景色
     */
    private int backGround = Color.WHITE;

    private float mDistance;


    public SplashView(Context context) {
        this(context, null);
    }

    public SplashView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SplashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHolePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHolePaint.setStyle(Paint.Style.STROKE);
        mHolePaint.setColor(backGround);
    }


    /**
     * 控件大小改变时会被调用，所以初始化时会被调用一次，在此获取控件大小
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = w * (1f / 2);
        mCenterY = h * (1f / 2);
        mDistance = (float) Math.hypot(mCenterX, mCenterY) / 2;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mState == null) {
            mState = new RotateState();
        }
        mState.drawState(canvas);
    }


    private SplashState mState;

    private abstract class SplashState {
        abstract void drawState(Canvas canvas);
    }

    /**
     * 旋转
     */
    private class RotateState extends SplashState {

        @Override
        void drawState(Canvas canvas) {
            //绘制背景
            drawBackGround(canvas);
            //绘制6个小球
            drawCircles(canvas);
        }
    }


    private void drawBackGround(Canvas canvas) {
        canvas.drawColor(backGround);
    }

    /**
     * 画出6个小球
     *
     * @param canvas
     */
    private void drawCircles(Canvas canvas) {
        float rotateAngle = (float) (Math.PI * 2 / colors.length);
        for (int i = 0; i < colors.length; i++) {
            float angle = i * rotateAngle;
            mPaint.setColor(colors[i]);
            float pointX = (float) (Math.cos(angle) * mRotateRadius + mCenterX);
            float pointY = (float) (Math.sin(angle) * mRotateRadius + mCenterY);
            canvas.drawCircle(pointX, pointY, mCircleRadius, mPaint);
        }

    }
}
