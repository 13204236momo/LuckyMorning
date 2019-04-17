package com.example.zhoumohan.luckymorning;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.zhoumohan.common_library.density.Density;
import com.zhoumohan.common_library.globalNetWorkChange.NetworkManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //initActivityLifecycleCallbacks();

        NetworkManager.getDefault().init(this);
    }

    private void initActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Density.setDensity(App.this, activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
}
