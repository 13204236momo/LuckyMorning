package com.example.zhoumohan.luckymorning.common.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.zhoumohan.luckymorning.R;


public class CircleView extends View {

    private Paint mPaint;
    private Path mPath;
    private Bitmap bitMap;
    private Matrix matrix;
    private float mFloat;

    private float[] pos = new float[2];
    private float[] tan = new float[2];

    public CircleView(Context context) {
        super(context);
        init();
    }


    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        matrix = new Matrix();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        bitMap = BitmapFactory.decodeResource(getResources(), R.drawable.left, options);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        mPath.reset();
        mPath.addCircle(0, 0, 200, Path.Direction.CW);
        canvas.drawPath(mPath, mPaint);

        mFloat += 0.01;
        if (mFloat>=1){
            mFloat=0;
        }
//        PathMeasure pathMeasure = new PathMeasure(mPath, false);
//        pathMeasure.getPosTan(pathMeasure.getLength()*mFloat, pos, tan);
//        float d = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
//
//        matrix.reset();
//        matrix.postRotate(d, bitMap.getWidth() / 2, bitMap.getHeight() / 2);
//        //将图片的绘制点与当前点重合
//        matrix.postTranslate(pos[0] - bitMap.getWidth() / 2, pos[1] - bitMap.getHeight() / 2);
//        canvas.drawBitmap(bitMap, matrix, mPaint);

        PathMeasure pathMeasure = new PathMeasure(mPath, false);
        //将pos信息和tan信息保存在matrix中
        pathMeasure.getMatrix(pathMeasure.getLength()*mFloat,matrix,PathMeasure.POSITION_MATRIX_FLAG|PathMeasure.TANGENT_MATRIX_FLAG);
        matrix.preTranslate(-bitMap.getWidth()/2,-bitMap.getHeight()/2);
        canvas.drawBitmap(bitMap, matrix, mPaint);

        invalidate();
    }
}
