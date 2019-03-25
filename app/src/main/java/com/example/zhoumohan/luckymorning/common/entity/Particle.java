package com.example.zhoumohan.luckymorning.common.entity;

/**
 * 粒子实体类
 */
public class Particle {

    /**
     * 粒子圆点横坐标
     */
    private float pointX;
    /**
     * 粒子圆点纵坐标
     */
    private float pointY;
    /**
     * 粒子半径
     */
    private float radius;
    /**
     * 粒子横向速度
     */
    private float vX;
    /**
     * 粒子纵向速度
     */
    private float vY;
    /**
     * 粒子横向加速度
     */
    private float aX;
    /**
     * 粒子纵向加速度
     */
    private float aY;

    /**
     * 粒子颜色
     */
    private int color;


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

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getvX() {
        return vX;
    }

    public void setvX(float vX) {
        this.vX = vX;
    }

    public float getvY() {
        return vY;
    }

    public void setvY(float vY) {
        this.vY = vY;
    }

    public float getaX() {
        return aX;
    }

    public void setaX(float aX) {
        this.aX = aX;
    }

    public float getaY() {
        return aY;
    }

    public void setaY(float aY) {
        this.aY = aY;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
