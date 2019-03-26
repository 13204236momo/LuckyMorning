package com.example.zhoumohan.luckymorning.common.widget.bubble;

public class DragBubble {

    /**
     * 圆心横坐标
     */
    public float mCenterX;
    /**
     * 圆心纵坐标
     */
    public float mCenterY;
    /**
     * 圆半径
     */
    public float mRadius;

    public DragBubble(float mCenterX, float mCenterY, float radius) {
        this.mCenterX = mCenterX;
        this.mCenterY = mCenterY;
        mRadius = radius;
    }
}
