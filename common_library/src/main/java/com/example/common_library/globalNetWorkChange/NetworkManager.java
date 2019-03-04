package com.example.common_library.globalNetWorkChange;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

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

    @SuppressLint("MissingPermission")
    public void init(Application application) {
        this.application = application;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager.NetworkCallback networkCallback = new NetworkCallbackImpl();
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            ConnectivityManager connectivityManager = (ConnectivityManager) NetworkManager.getDefault().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                connectivityManager.registerNetworkCallback(request, networkCallback);
            }
        } else {
            //做广播注册
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constants.ANDROID_NET_CHANGE_ACTION);
            application.registerReceiver(receiver, filter);
        }
    }


    public void register(Object object) {
        receiver.register(object);
    }

    public void unRegister(Object object) {
        receiver.unRegister(object);

    }

    public void unRegisterAll() {
        receiver.unRegisterAll();

    }
}
