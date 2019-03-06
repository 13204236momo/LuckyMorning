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
    public static final int CUBIC=2;

    int mOperation;
    float mX,mY;

    public static List<Point> points = new ArrayList<>();



    public CurvePath(int operation, float x, float y){
        mOperation = operation;
        mX=x;
        mY=y;
    }


    public CurvePath(int operation,List<Point> points){
        mOperation = operation;
        this.points = points;
    }

    public CurvePath(){

    }
}
