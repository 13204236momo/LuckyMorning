package com.example.zhoumohan.luckymorning.common.widget.bubble;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    private float fixBubbleR = 15f;

    /**
     * 移动气泡半径
     */
    private float moveBubbleR = 15f;

    /**
     * 气泡文字
     */
    private String text = "19";

    /**
     * 文字绘制区域
     */
    private Rect textRect;



    public DragBubbleView(Context context) {
        this(context,null);
    }

    public DragBubbleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
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

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        fixDragBubble = new DragBubble(w/2,h/2,fixBubbleR);
        moveDragBubble = new DragBubble(w/2,h/2,moveBubbleR);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (currentBubbleState == BUBBLE_STATE_DEFAULT){
            //1.静止状态 气泡和文字
            canvas.drawCircle(fixDragBubble.mCenterX, fixDragBubble.mCenterY, fixDragBubble.mRadius,bubblePaint);
            textPaint.getTextBounds(text,0,text.length(),textRect);
            canvas.drawText(text,fixDragBubble.mCenterX - textRect.width()/2,fixDragBubble.mCenterY+textRect.height()/2,textPaint);
        }

        //2.连接状态 静止状态气泡、移动气泡、文字、两气泡和贝塞尔曲线围成的区域

        //3.分离状态  气泡和文字

        //4.消失状态 爆炸效果
    }
}
