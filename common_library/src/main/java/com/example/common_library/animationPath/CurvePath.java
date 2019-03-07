package com.example.common_library.animationPath;

import java.util.ArrayList;
import java.util.List;

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

    public static List<Point> points = new ArrayList<>();



    public CurvePath(int operation, float x, float y){
        mOperation = operation;
        mX=x;
        mY=y;
    }

    /**
     * 多拐点贝塞尔曲线 （3个拐点及以上，即points传入四个或以上）
     * @param operation
     * @param points
     */
    public CurvePath(int operation,List<Point> points){
        mOperation = operation;
        this.points = points;
    }

    public CurvePath(){
    }
}
