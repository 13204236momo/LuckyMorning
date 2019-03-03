package com.example.common_library.globalNetWorkChange;

import android.app.Application;
import android.content.IntentFilter;

import com.example.common_library.globalNetWorkChange.utils.Constants;

public class NetworkManager {
    private static volatile NetworkManager instance;
    private Application application;
    private NetStateReceiver receiver;

    private NetworkManager() {
        receiver = new NetStateReceiver();
    }

    public static NetworkManager getDefault() {
        if (instance == null) {
            synchronized (NetworkManager.class) {
                if (instance == null) {
                    instance = new NetworkManager();
                }
            }
        }
        return instance;
    }

    public Application getApplication() {
        if (application == null) {
            throw new RuntimeException("NetworkManager.getDefault().init()未初始化");
        }
        return application;
    }

    public void init(Application application) {
        this.application = application;
        //做广播注册
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ANDROID_NET_CHANGE_ACTION);
        application.registerReceiver(receiver, filter);
    }

    public void setListener(NetChangeObserver listener) {
        receiver.setListener(listener);
    }
}
