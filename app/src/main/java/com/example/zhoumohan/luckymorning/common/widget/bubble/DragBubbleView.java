package com.example.zhoumohan.luckymorning.common.widget.bubble;

import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

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
    private float MAX_CONNECT_DISTANCE = 60;

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
     * 静止气泡
     */
    private DragBubble fixDragBubble;
    /**
     * 移动气泡
     */
    private DragBubble moveDragBubble;

    /**
     * 静止气泡半径
     */
    private float fixBubbleR = 12f;

    /**
     * 移动气泡半径
     */
    private float moveBubbleR = 12f;

    /**
     * 气泡文字
     */
    private String text = "19";

    /**
     * 文字绘制区域
     */
    private Rect textRect;

    /**
     * A、B、C、D 四点闭合路径
     */
    private Path path;


    public DragBubbleView(Context context) {
        this(context, null);
    }

    public DragBubbleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragBubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(AttributeSet attrs) {
        bubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG); //抗锯齿
        bubblePaint.setColor(Color.RED);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textRect = new Rect();

        pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pathPaint.setColor(Color.RED);
        pathPaint.setStyle(Paint.Style.FILL);

        path = new Path();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        fixDragBubble = new DragBubble(w / 2, h / 2, fixBubbleR);
        moveDragBubble = new DragBubble(w / 2, h / 2, moveBubbleR);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                moveDragBubble.mCenterX = event.getX();
                moveDragBubble.mCenterY = event.getY();
                if (Math.hypot(event.getX() - fixDragBubble.mCenterX, event.getY() - fixDragBubble.mCenterY) > MAX_CONNECT_DISTANCE) {
                    currentBubbleState = BUBBLE_STATE_INSULATE;
                } else {
                    currentBubbleState = BUBBLE_STATE_CONNECT;
                    fixDragBubble.mRadius = moveDragBubble.mRadius - MAX_CONNECT_DISTANCE/8;
                }

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (currentBubbleState == BUBBLE_STATE_DEFAULT) {
            //1.静止状态 气泡和文字
            canvas.drawCircle(fixDragBubble.mCenterX, fixDragBubble.mCenterY, fixDragBubble.mRadius, bubblePaint);
            textPaint.getTextBounds(text, 0, text.length(), textRect);
            canvas.drawText(text, fixDragBubble.mCenterX - ((float)textRect.width()) / 2, fixDragBubble.mCenterY + textRect.height() / 2, textPaint);
        }

        //2.连接状态 静止状态气泡、移动气泡、文字、两气泡和贝塞尔曲线围成的区域

        if (currentBubbleState == BUBBLE_STATE_CONNECT) {
            //静止气泡
            canvas.drawCircle(fixDragBubble.mCenterX, fixDragBubble.mCenterY, fixDragBubble.mRadius, bubblePaint);
            //移动气泡
            canvas.drawCircle(moveDragBubble.mCenterX, moveDragBubble.mCenterY, moveDragBubble.mRadius, bubblePaint);

            //两圆心之间的距离
            float mDistance = (float) Math.hypot((moveDragBubble.mCenterX - fixDragBubble.mCenterX),(moveDragBubble.mCenterY - fixDragBubble.mCenterY));
            //控制点坐标
            float iAnchorX = (moveDragBubble.mCenterX + fixDragBubble.mCenterX) / 2;
            float iAnchorY = (moveDragBubble.mCenterY + fixDragBubble.mCenterY) / 2;

            float sinTheta = (moveDragBubble.mCenterY - fixDragBubble.mCenterY) / mDistance;
            float cosTheta = (moveDragBubble.mCenterX - fixDragBubble.mCenterX) / mDistance;

            //A点
            float aX = fixDragBubble.mCenterX + cosTheta * fixDragBubble.mRadius;
            float aY = fixDragBubble.mCenterY - sinTheta * fixDragBubble.mRadius;

            //B点
            float bX = fixDragBubble.mCenterX - cosTheta * fixDragBubble.mRadius;
            float bY = fixDragBubble.mCenterY + sinTheta * fixDragBubble.mRadius;

            //C点
            float cX = moveDragBubble.mCenterX + cosTheta * moveDragBubble.mRadius;
            float cY = moveDragBubble.mCenterY - sinTheta * moveDragBubble.mRadius;

            //D点
            float dX = moveDragBubble.mCenterX - cosTheta * moveDragBubble.mRadius;
            float dY = moveDragBubble.mCenterY + sinTheta * moveDragBubble.mRadius;

            path.reset();
            path.moveTo(cX,cY);
            path.quadTo(iAnchorX,iAnchorY,aX,aY);
            path.lineTo(bX,bY);
            path.quadTo(iAnchorX,iAnchorY,dX,dY);
            path.close();

            canvas.drawPath(path,pathPaint);

            textPaint.getTextBounds(text, 0, text.length(), textRect);
            canvas.drawText(text, moveDragBubble.mCenterX - ((float)textRect.width()) / 2, moveDragBubble.mCenterY + textRect.height() / 2, textPaint);


        }
        //3.分离状态  气泡和文字

        if (currentBubbleState == BUBBLE_STATE_INSULATE) {
            canvas.drawCircle(moveDragBubble.mCenterX, moveDragBubble.mCenterY, moveDragBubble.mRadius, bubblePaint);
            textPaint.getTextBounds(text, 0, text.length(), textRect);
            canvas.drawText(text, moveDragBubble.mCenterX - textRect.width() / 2, moveDragBubble.mCenterY + textRect.height() / 2, textPaint);
        }

        //4.消失状态 爆炸效果
    }


    private class RadiuTypeEvaluator implements TypeEvaluator<Integer> {

        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            return null;
        }
    }
}
