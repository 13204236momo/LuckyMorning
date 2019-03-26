package com.example.zhoumohan.luckymorning.common.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

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
    private float mRotateRadius = 70;
    /**
     * 画小球的画笔
     */
    private Paint mPaint;

    private float mCircleRadius = 15;
    /**
     * 小球颜色
     */
    private int[] colors = {Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE, Color.GREEN, Color.GRAY};

    /**
     * 水波纹画笔
     */
    private Paint mHolePaint;

    /**
     * 画布背景色
     */
    private int backGround = Color.WHITE;

    private float mDistance;

    /**
     * 动画
     */
    private ValueAnimator animator;

    /**
     * 当前旋转角度
     */
    private float mCurrentRotateAngle = 0f;
    /**
     * 当前水波纹半径
     */
    private float mCurrentHoleRadius = 0f;


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
        mDistance = (float) (Math.hypot(w, h) / 2);
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

    /**
     * 要进行第二步动画时调用（比如网络请求成功后）
     */
    public void setLoadingEnd() {
        animator.end();
    }

    private abstract class SplashState {
        abstract void drawState(Canvas canvas);
    }

    /**
     * 旋转
     */
    private class RotateState extends SplashState {

        public RotateState() {
            //旋转动画
            animator = ValueAnimator.ofFloat(0, (float) (Math.PI * 2));
            animator.setRepeatCount(-1);
            animator.setDuration(1200);
            animator.setInterpolator(new LinearInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotateAngle = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mState = new MergingState();
                }
            });
            animator.start();
        }

        @Override
        void drawState(Canvas canvas) {
            //绘制背景
            drawBackGround(canvas);
            //绘制6个小球
            drawCircles(canvas);

        }
    }

    /**
     * 缩放
     */
    private class MergingState extends SplashState {
        public MergingState() {
            animator = ValueAnimator.ofFloat(mCircleRadius, mRotateRadius);
            animator.setDuration(1000);
            animator.setInterpolator(new OvershootInterpolator(10f));
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mRotateRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mState = new ExpandState();
                }
            });
            animator.reverse();
        }

        @Override
        void drawState(Canvas canvas) {
            drawBackGround(canvas);
            drawCircles(canvas);
        }
    }


    private class ExpandState extends SplashState {
        public ExpandState() {
            animator = ValueAnimator.ofFloat(mCircleRadius, mDistance);
            animator.setDuration(900);
            animator.setInterpolator(new LinearInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentHoleRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            animator.start();
        }

        @Override
        void drawState(Canvas canvas) {
            drawBackGround(canvas);
        }
    }


    private void drawBackGround(Canvas canvas) {
        if (mCurrentHoleRadius >0){  //第三种动画状态
            //绘制空心圆
            float strokeWidth = mDistance - mCurrentHoleRadius;
            float radius = strokeWidth/2 + mCurrentHoleRadius;   //setStrokeWidth()这个方法设置圆环宽度，是往园内设置一半，往圆外设置一半
            mHolePaint.setStrokeWidth(strokeWidth);
            canvas.drawCircle(mCenterX,mCenterY,radius,mHolePaint);
        }else {
            canvas.drawColor(backGround);
        }

    }

    /**
     * 画出6个小球
     *
     * @param canvas
     */
    private void drawCircles(Canvas canvas) {
        float rotateAngle = (float) (Math.PI * 2 / colors.length);
        for (int i = 0; i < colors.length; i++) {
            float angle = i * rotateAngle + mCurrentRotateAngle;
            mPaint.setColor(colors[i]);
            float pointX = (float) (Math.cos(angle) * mRotateRadius + mCenterX);
            float pointY = (float) (Math.sin(angle) * mRotateRadius + mCenterY);
            canvas.drawCircle(pointX, pointY, mCircleRadius, mPaint);
        }

    }


}
