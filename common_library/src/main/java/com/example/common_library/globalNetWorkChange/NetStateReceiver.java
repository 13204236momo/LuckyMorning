package com.example.common_library.globalNetWorkChange;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import android.util.Log;

import com.example.common_library.globalNetWorkChange.utils.Constants;
import com.example.common_library.globalNetWorkChange.utils.NetworkUtils;

import java.text.SimpleDateFormat;

public class NetStateReceiver extends BroadcastReceiver {

    private NetType netType;
    private NetChangeObserver listener;
    private Long lastTime = 0l;

    public NetStateReceiver() {
        //初始化没有网络
        this.netType = NetType.NONE;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Log.e(Constants.LOG_TAG, "异常啦");
            return;
        }
        long time = getTime();

        //重复广播
        if (time == lastTime) {
            return;
        }
        //处理广播事件
        if (intent.getAction().equalsIgnoreCase(Constants.ANDROID_NET_CHANGE_ACTION)) {
            Log.e(Constants.LOG_TAG, "网络发生改变");
            netType = NetworkUtils.getNetType();
            if (NetworkUtils.isNetWoekAvailable()) {
                lastTime = time;
                Log.e(Constants.LOG_TAG, "有网络");
                listener.onConnect(netType);
            } else {
                lastTime = time;
                Log.e(Constants.LOG_TAG, "无网络");
                listener.onDisConnect();
            }
        }

    }

    public void setListener(NetChangeObserver listener) {
        this.listener = listener;
    }

    public long getTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = sDateFormat.format(new java.util.Date());
        return Long.valueOf(date);
    }
}

