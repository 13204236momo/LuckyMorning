package com.example.common_library.animationPath;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 模仿Path系统工具类的设计
 * 能够存储一系列的指令：moveTo/lineTo/cubicTo(x,y等参数)
 */
public class AnimatorPath {
    //如何能够存储一系列的指令？
    //如何定义指令PathPoint
    ArrayList<CurvePath> curvePathList = new ArrayList();
    private View view;

    public void moveTo(float x, float y) {
        curvePathList.add(new CurvePath(CurvePath.MOVE, x, y));
    }

    //三阶贝塞尔曲线
    public void cubicTo(float c0x, float c0y, float c1x, float c1y, float x, float y) {
        curvePathList.add(new CurvePath(CurvePath.CUBIC, c0x, c0y, c1x, c1y, x, y));
    }

    //三阶贝塞尔曲线
    public void cubicTo(List<Point> points) {
        curvePathList.add(new CurvePath(CurvePath.CUBIC, points));
    }

    //直线
    public void lineTo(float x, float y) {
        curvePathList.add(new CurvePath(CurvePath.LINE, x, y));
    }

    public void startAnimation(View v, int duration) {
        this.view = v;
        startAnimation(v, duration, null);
    }

    public void startAnimation(View v, int duration, TimeInterpolator value) {
        this.view = v;
        //属性动画：本质是控制一个对象身上的任何属性值----反射setTranslationX，setAlpha()
        ObjectAnimator animator = ObjectAnimator.ofObject(this, "path", new PathEvaluator(), curvePathList.toArray());
        if (value != null) {
            animator.setInterpolator(value);
        }
        animator.setDuration(duration);
        animator.start();
    }

    public void setPath(CurvePath p) {
        view.setTranslationX(p.mX);
        view.setTranslationY(p.mY);
    }

}


