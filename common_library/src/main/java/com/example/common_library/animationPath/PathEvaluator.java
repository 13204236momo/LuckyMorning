package com.example.common_library.animationPath;

import android.animation.TypeEvaluator;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PathEvaluator implements TypeEvaluator<CurvePath> {

    private List<Integer> a = new ArrayList<>();

    public PathEvaluator() {
        yanghui(CurvePath.points.size());
    }

    @Override
    public CurvePath evaluate(float t, CurvePath startValue, CurvePath endValue) {
        //t:动画执行的百分比 0~1
        float x=0,y=0;
        //估值计算一波骚操作
        //路径复杂：多种--直线，曲线，起始点赋值

        if(endValue.mOperation == CurvePath.CUBIC){//三阶贝塞尔曲线类型---计算
            float oneMinusT = 1 - t;
            for (int i = 0 ;i<CurvePath.points.size();i++){
                x = x + (float) (endValue.points.get(i).getPointX() * (Math.pow(oneMinusT,endValue.points.size()-1 - i)) * Math.pow(t,i)) * a.get(i);
                y = y + (float) (endValue.points.get(i).getPointY() * (Math.pow(oneMinusT,endValue.points.size()-1 - i)) * Math.pow(t,i)) * a.get(i);
            }
            endValue.mX = x;
            endValue.mY = y;
        }else if(endValue.mOperation == CurvePath.LINE){//直线类型
            //x,y= 起始点坐标 + t*(起始点和终点的距离)
            x = startValue.mX + t*(endValue.mX - startValue.mX);
            y = startValue.mY + t*(endValue.mY - startValue.mY);
        }else{
            //move类型
            x = endValue.mX;
            y = endValue.mY;
        }
        return new CurvePath(CurvePath.MOVE,x,y);
    }


    /**
     * a(系数值) 的值符合杨辉三角规律
     * @param row
     */
    private void yanghui(int row) {
        for (int i = 0; i < row; i++) {
            int number = 1;
            for (int j = 0; j <= i; j++) {
                if (i == row-1){
                    Log.e("zhou@",number+"@");
                    a.add(number);
                }
                number = number * (i - j) / (j + 1);
            }
        }

    }
}
