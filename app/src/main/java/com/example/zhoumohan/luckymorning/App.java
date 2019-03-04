package com.example.zhoumohan.luckymorning;

import android.app.Application;

import com.example.common_library.globalNetWorkChange.NetworkManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        NetworkManager.getDefault().init(this);
    }
}
