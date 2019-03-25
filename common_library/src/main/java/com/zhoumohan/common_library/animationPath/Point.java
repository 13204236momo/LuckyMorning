package com.zhoumohan.common_library.animationPath;

public class Point {
    /**
     * 点的横坐标
     */
    private float pointX;
    /**
     * 点的纵坐标
     */
    private float pointY;

    public Point(float pointX, float pointY) {
        this.pointX = pointX;
        this.pointY = pointY;
    }

    public float getPointX() {
        return pointX;
    }

    public void setPointX(float pointX) {
        this.pointX = pointX;
    }

    public float getPointY() {
        return pointY;
    }

    public void setPointY(float pointY) {
        this.pointY = pointY;
    }
}
