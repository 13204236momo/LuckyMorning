package com.example.zhoumohan.luckymorning.common.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PointFEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.example.zhoumohan.luckymorning.R;

/**
 * qq消息气泡
 */
public class DragBubbleView extends View {

    private int BUBBLE_STATE_DEFAULT = 1;
    private int BUBBLE_STATE_CONNECT = 2;
    private int BUBBLE_STATE_INSULATE = 3;
    private int BUBBLE_STATE_DISSMIS = 4;

    /**
     * 两小球最大连接距离
     */
    private float mMaxDist;

    /**
     * 当前状态
     */
    private int currentBubbleState = 1;
    /**
     * 气泡画笔
     */
    private Paint bubblePaint;
    /**
     * 文字画笔
     */
    private Paint textPaint;

    /**
     * 路径画笔
     */
    private Paint pathPaint;

    /**
     * 气泡半径
     */
    private float mBubbleRadius;

    /**
     * 静止气泡半径
     */
    private float fixBubbleR;

    /**
     * 移动气泡半径
     */
    private float moveBubbleR;

    /**
     * 不动气泡的圆心
     */
    private PointF mBubFixedCenter;
    /**
     * 可动气泡的圆心
     */
    private PointF mBubMovableCenter;

    /**
     * 两气泡圆心距离
     */
    private float mDist;

    /**
     * 气泡颜色
     */
    private int mBubbleColor;

    /**
     * 气泡文字
     */
    private String text;

    /**
     * 文字大小
     */
    private float mTextSize;

    /**
     * 气泡消息文字颜色
     */
    private int mTextColor;

    /**
     * 文字绘制区域
     */
    private Rect textRect;

    /**
     * A、B、C、D 四点闭合路径
     */
    private Path path;

    /**
     * 手指触摸偏移量
     */
    private float MOVE_OFFSET = 0;


    public DragBubbleView(Context context) {
        this(context, null);
    }

    public DragBubbleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragBubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context,attrs);
    }

    private void init(Context context,@Nullable AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DragBubbleView);
        mBubbleRadius = array.getDimension(R.styleable.DragBubbleView_bubble_radius, moveBubbleR);
        mBubbleColor = array.getColor(R.styleable.DragBubbleView_bubble_color, Color.RED);
        text = array.getString(R.styleable.DragBubbleView_bubble_text);
        mTextSize = array.getDimension(R.styleable.DragBubbleView_bubble_textSize, mTextSize);
        mTextColor = array.getColor(R.styleable.DragBubbleView_bubble_textColor, Color.WHITE);
        array.recycle();

        fixBubbleR = mBubbleRadius;
        moveBubbleR = mBubbleRadius;
        mMaxDist = 8 * mBubbleRadius;
        MOVE_OFFSET = mMaxDist / 4;


        bubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG); //抗锯齿
        bubblePaint.setColor(mBubbleColor);
        bubblePaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(mTextColor);
        textPaint.setTextSize(mTextSize);
        textRect = new Rect();

        pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pathPaint.setColor(mBubbleColor);
        pathPaint.setStyle(Paint.Style.FILL);

        path = new Path();



    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        initValue(w,h);
    }

    private void initValue(int w, int h) {
        currentBubbleState = BUBBLE_STATE_DEFAULT;
        //设置固定气泡圆心初始坐标
        if (mBubFixedCenter == null) {
            mBubFixedCenter = new PointF(w / 2, h / 2);
        } else {
            mBubFixedCenter.set(w / 2, h / 2);
        }
        //设置可动气泡圆心初始坐标
        if (mBubMovableCenter == null) {
            mBubMovableCenter = new PointF(w / 2, h / 2);
        } else {
            mBubMovableCenter.set(w / 2, h / 2);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDist = (float) Math.hypot(event.getX() - mBubFixedCenter.x, event.getY() - mBubFixedCenter.y);
                break;
            case MotionEvent.ACTION_MOVE:
                mBubMovableCenter.x = event.getX();
                mBubMovableCenter.y = event.getY();
                if (Math.hypot(event.getX() - mBubFixedCenter.x, event.getY() - mBubFixedCenter.y) > mMaxDist) {
                    currentBubbleState = BUBBLE_STATE_INSULATE;
                } else {
                    currentBubbleState = BUBBLE_STATE_CONNECT;
                    fixBubbleR = moveBubbleR - mMaxDist/8;
                }

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (currentBubbleState == BUBBLE_STATE_CONNECT){
                //橡皮筋动画
                    startBubbleRestAnim();
                }else if (currentBubbleState == BUBBLE_STATE_INSULATE){

                }

                break;
        }
        return true;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //2.连接状态 静止状态气泡、移动气泡、文字、两气泡和贝塞尔曲线围成的区域
        if (currentBubbleState == BUBBLE_STATE_CONNECT) {
            //静止气泡
            canvas.drawCircle(mBubFixedCenter.x, mBubFixedCenter.y, fixBubbleR, bubblePaint);
            //移动气泡
            canvas.drawCircle(mBubMovableCenter.x, mBubMovableCenter.y, moveBubbleR, bubblePaint);

            //控制点坐标
            int iAnchorX = (int) ((mBubMovableCenter.x + mBubFixedCenter.x) / 2);
            int iAnchorY = (int) ((mBubMovableCenter.y + mBubFixedCenter.y) / 2);

            float sinTheta = (mBubMovableCenter.y - mBubFixedCenter.y) / mDist;
            float cosTheta = (mBubMovableCenter.x - mBubFixedCenter.x) / mDist;

            //A点
            float aX = mBubFixedCenter.x + cosTheta * fixBubbleR;
            float aY = mBubFixedCenter.y - sinTheta * fixBubbleR;

            //B点
            float bX = mBubFixedCenter.x - cosTheta * fixBubbleR;
            float bY = mBubFixedCenter.y + sinTheta * fixBubbleR;

            //C点
            float cX = mBubMovableCenter.x + cosTheta * moveBubbleR;
            float cY = mBubMovableCenter.y - sinTheta * moveBubbleR;

            //D点
            float dX = mBubMovableCenter.x - cosTheta * moveBubbleR;
            float dY = mBubMovableCenter.y + sinTheta * moveBubbleR;

            path.reset();
            path.moveTo(cX,cY);
            path.quadTo(iAnchorX,iAnchorY,aX,aY);
            path.lineTo(bX,bY);
            path.quadTo(iAnchorX,iAnchorY,dX,dY);
            path.close();
            canvas.drawPath(path,pathPaint);

        }
        //3.分离状态  气泡和文字

        if (currentBubbleState != BUBBLE_STATE_INSULATE) {
            canvas.drawCircle(mBubMovableCenter.x, mBubMovableCenter.y, moveBubbleR, bubblePaint);
            textPaint.getTextBounds(text, 0, text.length(), textRect);
            canvas.drawText(text, mBubMovableCenter.x - textRect.width() / 2, mBubMovableCenter.y + textRect.height() / 2, textPaint);
        }

        //4.消失状态 爆炸效果
    }


    /**
     * 橡皮筋动画
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startBubbleRestAnim() {
        ValueAnimator animator = ValueAnimator.ofObject(new PointFEvaluator(),new PointF(mBubMovableCenter.x,mBubMovableCenter.y),
                new PointF(mBubFixedCenter.x,mBubFixedCenter.y));
        animator.setDuration(200);
        animator.setInterpolator(new OvershootInterpolator(5f));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mBubMovableCenter = (PointF) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                currentBubbleState = BUBBLE_STATE_DEFAULT;
                fixBubbleR = moveBubbleR;
            }
        });
        animator.start();
    }

}
