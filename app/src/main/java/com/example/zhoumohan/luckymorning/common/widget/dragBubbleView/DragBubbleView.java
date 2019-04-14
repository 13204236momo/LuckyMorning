
package com.example.zhoumohan.luckymorning.common.widget.dragBubbleView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PointFEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.example.zhoumohan.luckymorning.R;

/**
 * QQ气泡效果
 */
public class DragBubbleView extends View {

    /**
     * 气泡默认状态--静止
     */
    private final int BUBBLE_STATE_DEFAULT = 0;
    /**
     * 气泡相连
     */
    private final int BUBBLE_STATE_CONNECT = 1;
    /**
     * 气泡分离
     */
    private final int BUBBLE_STATE_APART = 2;
    /**
     * 气泡消失
     */
    private final int BUBBLE_STATE_DISMISS = 3;

    /**
     * 气泡半径
     */
    private float mBubbleRadius;
    /**
     * 气泡颜色
     */
    private int mBubbleColor;
    /**
     * 气泡消息文字
     */
    private String mTextStr;
    /**
     * 气泡消息文字颜色
     */
    private int mTextColor;
    /**
     * 气泡消息文字大小
     */
    private float mTextSize;
    /**
     * 不动气泡的半径
     */
    private float mBubFixedRadius;
    /**
     * 可动气泡的半径
     */
    private float mBubMovableRadius;
    /**
     * 不动气泡的圆心
     */
    private PointF mBubFixedCenter;
    /**
     * 可动气泡的圆心
     */
    private PointF mBubMovableCenter;
    /**
     * 气泡的画笔
     */
    private Paint mBubblePaint;
    /**
     * 贝塞尔曲线path
     */
    private Path mBezierPath;

    private Paint mTextPaint;

    //文本绘制区域
    private Rect mTextRect;

    private Paint mBurstPaint;

    //爆炸绘制区域
    private Rect mBurstRect;

    /**
     * 气泡状态标志
     */
    private int mBubbleState = BUBBLE_STATE_DEFAULT;
    /**
     * 两气泡圆心距离
     */
    private float mDist;
    /**
     * 气泡相连状态最大圆心距离
     */
    private float mMaxDist;
    /**
     * 手指触摸偏移量
     */
    private float MOVE_OFFSET;

    /**
     * 气泡爆炸的bitmap数组
     */
    private Bitmap[] mBurstBitmapsArray;
    /**
     * 是否在执行气泡爆炸动画
     */
    private boolean mIsBurstAnimStart = false;

    /**
     * 当前气泡爆炸图片index
     */
    private int mCurDrawableIndex;

    /**
     * 气泡爆炸的图片id数组
     */
    private int[] mBurstDrawablesArray = {R.mipmap.burst_1, R.mipmap.burst_2, R.mipmap.burst_3, R.mipmap.burst_4, R.mipmap.burst_5};

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DragBubbleView(Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DragBubbleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DragBubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DragBubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        //默认值
        mTextSize = 15;
        mTextStr = "";
        mBubbleColor = Color.RED;
        mTextColor = Color.WHITE;


        //两个圆半径大小一致
        mBubFixedRadius = mBubbleRadius;
        mBubMovableRadius = mBubFixedRadius;
        mMaxDist = 8 * mBubbleRadius;
        MOVE_OFFSET = mMaxDist / 4;

        //抗锯齿
        mBubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBubblePaint.setColor(mBubbleColor);
        mBubblePaint.setStyle(Paint.Style.FILL);
        mBezierPath = new Path();

        //文本画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextRect = new Rect();

        //爆炸画笔
        mBurstPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBurstPaint.setFilterBitmap(true);
        mBurstRect = new Rect();
        mBurstBitmapsArray = new Bitmap[mBurstDrawablesArray.length];
        for (int i = 0; i < mBurstDrawablesArray.length; i++) {
            //将气泡爆炸的drawable转为bitmap
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mBurstDrawablesArray[i]);
            mBurstBitmapsArray[i] = bitmap;
        }
    }


    public void init(int w, int h) {
        mBubbleState = BUBBLE_STATE_DEFAULT;
        //设置固定气泡圆心初始坐标
        if (mBubFixedCenter == null) {
            mBubFixedCenter = new PointF(w, h);
        } else {
            mBubFixedCenter.set(w, h);
        }
        //设置可动气泡圆心初始坐标
        if (mBubMovableCenter == null) {
            mBubMovableCenter = new PointF(w, h);
        } else {
            mBubMovableCenter.set(w, h);
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //1. 连接情况绘制贝塞尔曲线  2.绘制圆背景以及文本 3.另外端点绘制一个圆
        //1. 静止状态  2，连接状态 3，分离状态  4，消失

        if (mBubbleState == BUBBLE_STATE_CONNECT) {
            //绘制静止的气泡
            canvas.drawCircle(mBubFixedCenter.x, mBubFixedCenter.y, mBubFixedRadius, mBubblePaint);
            //计算控制点的坐标
            int iAnchorX = (int) ((mBubMovableCenter.x + mBubFixedCenter.x) / 2);
            int iAnchorY = (int) ((mBubMovableCenter.y + mBubFixedCenter.y) / 2);

            float sinTheta = (mBubMovableCenter.y - mBubFixedCenter.y) / mDist;
            float cosTheta = (mBubMovableCenter.x - mBubFixedCenter.x) / mDist;

            //D
            float iBubFixedStartX = mBubFixedCenter.x - mBubFixedRadius * sinTheta;
            float iBubFixedStartY = mBubFixedCenter.y + mBubFixedRadius * cosTheta;
            //C
            float iBubMovableEndX = mBubMovableCenter.x - mBubMovableRadius * sinTheta;
            float iBubMovableEndY = mBubMovableCenter.y + mBubMovableRadius * cosTheta;

            //A
            float iBubFixedEndX = mBubFixedCenter.x + mBubFixedRadius * sinTheta;
            float iBubFixedEndY = mBubFixedCenter.y - mBubFixedRadius * cosTheta;
            //B
            float iBubMovableStartX = mBubMovableCenter.x + mBubMovableRadius * sinTheta;
            float iBubMovableStartY = mBubMovableCenter.y - mBubMovableRadius * cosTheta;

            mBezierPath.reset();
            mBezierPath.moveTo(iBubFixedStartX, iBubFixedStartY);
            mBezierPath.quadTo(iAnchorX, iAnchorY, iBubMovableEndX, iBubMovableEndY);

            mBezierPath.lineTo(iBubMovableStartX, iBubMovableStartY);
            mBezierPath.quadTo(iAnchorX, iAnchorY, iBubFixedEndX, iBubFixedEndY);
            mBezierPath.close();
            canvas.drawPath(mBezierPath, mBubblePaint);
        }

        //静止，连接，分离状态都需要绘制圆背景以及文本
        if (mBubbleState != BUBBLE_STATE_DISMISS) {
            canvas.drawCircle(mBubMovableCenter.x, mBubMovableCenter.y, mBubMovableRadius, mBubblePaint);
            mTextPaint.getTextBounds(mTextStr, 0, mTextStr.length(), mTextRect);
            canvas.drawText(mTextStr, mBubMovableCenter.x - ((float) mTextRect.width()) / 2, mBubMovableCenter.y + ((float) mTextRect.height()) / 2, mTextPaint);
        }

        // 认为是消失状态，执行爆炸动画
        if (mBubbleState == BUBBLE_STATE_DISMISS && mCurDrawableIndex < mBurstBitmapsArray.length) {
            mBurstRect.set(
                    (int) (mBubMovableCenter.x - mBubMovableRadius),
                    (int) (mBubMovableCenter.y - mBubMovableRadius),
                    (int) (mBubMovableCenter.x + mBubMovableRadius),
                    (int) (mBubMovableCenter.y + mBubMovableRadius));
            canvas.drawBitmap(mBurstBitmapsArray[mCurDrawableIndex], null, mBurstRect, mBubblePaint);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mBubbleState != BUBBLE_STATE_DISMISS) {
                    mDist = (float) Math.hypot(Math.abs(event.getX()), Math.abs(event.getY()));
                    if (mDist < mBubbleRadius + MOVE_OFFSET) {
                        //加上MOVE_OFFSET是为了方便拖拽
                        mBubbleState = BUBBLE_STATE_CONNECT;
                    } else {
                        mBubbleState = BUBBLE_STATE_DEFAULT;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mBubbleState != BUBBLE_STATE_DEFAULT) {
                    mDist = (float) Math.hypot(event.getX(), event.getY());
                    mBubMovableCenter.x = event.getX() + mBubFixedCenter.x;
                    mBubMovableCenter.y = event.getY() + mBubFixedCenter.y;
                    if (mBubbleState == BUBBLE_STATE_CONNECT) {
                        if (mDist < mMaxDist - MOVE_OFFSET) {//当拖拽的距离在指定范围内，那么调整不动气泡的半径
                            mBubFixedRadius = mBubbleRadius - mDist / 8;
                        } else {
                            mBubbleState = BUBBLE_STATE_APART; //当拖拽距离超过指定范围，那么改成脱离状态
                        }
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mBubbleState == BUBBLE_STATE_CONNECT) {
                    // 橡皮筋动画
                    startBubbleRestAnim();
                } else if (mBubbleState == BUBBLE_STATE_APART) {
                    if (mDist < 2 * mBubbleRadius) {
                        //反弹动画
                        startBubbleRestAnim();
                    } else {
                        // 爆炸动画
                        startBubbleBurstAnim();
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 连接状态下松开手指，执行类似橡皮筋动画
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startBubbleRestAnim() {
        ValueAnimator anim = ValueAnimator.ofObject(new PointFEvaluator(),
                new PointF(mBubMovableCenter.x, mBubMovableCenter.y),
                new PointF(mBubFixedCenter.x, mBubFixedCenter.y));
        anim.setDuration(200);
        anim.setInterpolator(new OvershootInterpolator(5f));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mBubMovableCenter = (PointF) animation.getAnimatedValue();
                invalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBubbleState = BUBBLE_STATE_DEFAULT;
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) {
                    listener.onDefault();
                }
            }
        });
        anim.start();
    }

    /**
     * 爆炸动画
     */
    private void startBubbleBurstAnim() {
        //将气泡改成消失状态
        mBubbleState = BUBBLE_STATE_DISMISS;
        ValueAnimator animator = ValueAnimator.ofInt(0, mBurstBitmapsArray.length);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurDrawableIndex = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) {
                    listener.onDismiss();
                }
            }
        });
        animator.start();
    }


    /**
     * 设置消息数
     *
     * @param str
     */
    public void setTextStr(String str) {
        mTextStr = str;
    }

    /**
     * 设置文字大小
     *
     * @param textSize
     */
    public void setTextSize(float textSize) {
        mTextSize = textSize;
        mTextPaint.setTextSize(textSize);
    }

    /**
     * 设置文字颜色
     *
     * @param color
     */
    public void setTextColor(int color) {
        mTextColor = color;
        mTextPaint.setColor(color);
    }

    /**
     * 设置气泡半径
     *
     * @param radius
     */
    public void setBubbleRadius(float radius) {
        this.mBubbleRadius = radius;
        mBubFixedRadius = mBubbleRadius;
        mBubMovableRadius = mBubFixedRadius;
        mMaxDist = 8 * mBubbleRadius;
        MOVE_OFFSET = mMaxDist / 4;
    }


    public interface StateChangeListener {
        void onDefault();

        void onConnect();

        void onApart();

        void onDismiss();
    }

    private StateChangeListener listener;

    public void setSateChangeListener(StateChangeListener listener) {
        this.listener = listener;
    }


}


