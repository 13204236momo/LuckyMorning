package com.example.zhoumohan.luckymorning.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.zhoumohan.luckymorning.R;
import com.example.zhoumohan.luckymorning.common.entity.Particle;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.List;


public class ParticleBlastView extends View {
    /**
     * 粒子爆炸效果的view
     *
     * @param context
     */

    private BitmapDrawable src;
    private int width;
    private int height;

    private Bitmap mBitmap;
    private float radius = 3;
    private List<Particle> list = new ArrayList<>();
    private Paint mPaint;
    private ValueAnimator animator;

    public ParticleBlastView(Context context) {
        this(context, null);
    }

    public ParticleBlastView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParticleBlastView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ParticleBlast);
        if (array != null) {
            src = (BitmapDrawable) array.getDrawable(R.styleable.ParticleBlast_src);
            width = array.getLayoutDimension(R.styleable.ParticleBlast_android_layout_width, -1);
            height = array.getLayoutDimension(R.styleable.ParticleBlast_android_layout_height, -2);
        }
    }

    private void init() {
        mPaint = new Paint();
        mBitmap = src.getBitmap();
        for (int i = 0; i < mBitmap.getWidth(); i++) {
            for (int j = 0; j < mBitmap.getHeight(); j++) {
                Particle mParticle = new Particle();
                mParticle.setColor(mBitmap.getPixel(i, j));
                mParticle.setPointX(i * radius + radius / 2);
                mParticle.setPointY(j * radius + radius / 2);
                mParticle.setRadius(radius / 2);

                //速度(-20,20)
                mParticle.setvX((float) (Math.pow(-1, Math.ceil(Math.random() * 1000)) * 20 * Math.random()));
                mParticle.setvY(rangInt(-15, 35));
                //加速度
                mParticle.setaX(0);
                mParticle.setaY(0.98f);
                list.add(mParticle);
            }
        }

        animator = ValueAnimator.ofFloat(0, 1);
        animator.setRepeatCount(-1);
        animator.setDuration(2000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                upData();
                invalidate();
            }
        });

    }

    private void upData() {
        //更新粒子的位置
        for (Particle particle : list) {
            particle.setPointX(particle.getPointX() + particle.getvX());
            particle.setPointY(particle.getPointY() + particle.getvY());

            particle.setvX(particle.getvX() + particle.getaX());
            particle.setvY(particle.getvY() + particle.getaY());
        }
//只显示部分像素的爆炸效果
//        for (int i=0;i<list.size();i++){
//            Particle particle = list.get(i);
//
//            if (i<30){
//                particle.setRadius(30);
//            }else {
//                particle.setRadius(0);
//            }
//
//            particle.setPointX(particle.getPointX() + particle.getvX());
//            particle.setPointY(particle.getPointY() + particle.getvY());
//
//            particle.setvX(particle.getvX() + particle.getaX());
//            particle.setvY(particle.getvY() + particle.getaY());
//        }
    }

    private int rangInt(int i, int j) {
        int max = Math.max(i, j);
        int min = Math.min(i, j) - 1;
        //在0到(max - min)范围内变化，取大于x的最小整数 再随机
        return (int) (min + Math.ceil(Math.random() * (max - min)));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                animator.start();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(measureWidth(widthMeasureSpec), measureWidth(heightMeasureSpec));
//    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (measureSpec) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = Math.min(result, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }

        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(500, 500);
        for (Particle particle : list) {
            mPaint.setColor(particle.getColor());
            canvas.drawCircle(particle.getPointX(), particle.getPointY(), particle.getRadius(), mPaint);
        }
    }
}
