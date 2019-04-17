package com.zhoumohan.common_library.density;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

public class Density {
    /**
     * 参考像素密度，即UI设计图尺寸
     * 由此，使用后 1dp = 1px
     */
    private static final float WIDTH = 720;
    private static float appDensity;
    private static float appScaleDensity;

    public static void setDensity(final Application application, Activity activity){
        //获取当前app屏幕的显示信息
        DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
        //获取目标density值
        if (appDensity == 0){
            appDensity = appDisplayMetrics.density;
            appScaleDensity = appDisplayMetrics.scaledDensity;

            //添加字体变化监听回调
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0){
                        appScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;

                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
        float targetDensity = appDisplayMetrics.widthPixels / WIDTH;
        float targetScaleDensity = targetDensity * (appScaleDensity/ appDensity);
        int targetDpi = (int) (targetDensity *160);

        DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaleDensity;
        activityDisplayMetrics.densityDpi = targetDpi;
    }
}
