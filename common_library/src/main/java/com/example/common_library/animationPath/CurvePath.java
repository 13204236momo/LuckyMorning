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
    float mControl0X,mControl0Y;
    float mControl1X,mControl1Y;

    public static List<Point> points = new ArrayList<>();



    public CurvePath(int operation, float x, float y){
        mOperation = operation;
        mX=x;
        mY=y;
    }

    public CurvePath(int operation, float c0x, float c0y, float c1x, float c1y, float x, float y) {
        mOperation = operation;
        mControl0X = c0x;
        mControl0Y = c0y;
        mControl1X = c1x;
        mControl1Y = c1y;
        mX = x;
        mY = y;
    }

    public CurvePath(int operation,List<Point> points){
        mOperation = operation;
        this.points = points;
    }

    public CurvePath(){

    }
}
