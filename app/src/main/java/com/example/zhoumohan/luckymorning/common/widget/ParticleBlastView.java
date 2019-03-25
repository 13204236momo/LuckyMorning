package com.example.zhoumohan.luckymorning.common.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
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
        init();
    }

    private void init() {
        mPaint = new Paint();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.pic);
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
            particle.setPointX(particle.getvX() + particle.getaX());
            particle.setPointY(particle.getvX() + particle.getaX());

            particle.setvX(particle.getvX() + particle.getaX());
            particle.setvX(particle.getvX() + particle.getaX());
        }
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
