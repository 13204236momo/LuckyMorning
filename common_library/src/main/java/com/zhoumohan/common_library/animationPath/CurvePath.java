package com.zhoumohan.common_library.animationPath;

/**
 * 动画移动的曲线路线
 */
public class CurvePath {
    /**
     * 起点
     */
    public static final int MOVE=0;
    /**
     * 直线
     */
    public static final int LINE=1;
    /**
     * 贝塞尔曲线
     */
    public static final int CURVE=2;

    int mOperation;
    float mX,mY;

    public static Point[] points;


    public CurvePath(int operation, float x, float y){
        mOperation = operation;
        mX=x;
        mY=y;
    }

    /**
     * 多拐点贝塞尔曲线 （3个拐点及以上，即points传入四个或以上）
     * 传入的第一个点为起点
     * @param operation
     * @param points
     */

    public CurvePath(int operation, Point[] points){
        mOperation = operation;
        this.points = points;
    }
}
