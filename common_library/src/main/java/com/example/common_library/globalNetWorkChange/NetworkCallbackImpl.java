package com.example.common_library.globalNetWorkChange;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.common_library.globalNetWorkChange.utils.Constants;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {

    private NetStateReceiver receiver;

    public NetworkCallbackImpl() {
    }

    public NetworkCallbackImpl(NetStateReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        Log.e(Constants.LOG_TAG,"网络已连接");
    }

    @Override
    public void onLost(Network network) {
        super.onLost(network);
        Log.e(Constants.LOG_TAG,"网络已中断");
        receiver.post(NetType.NONE);
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)){
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                Log.e(Constants.LOG_TAG,"网络已变更,类型为WIFI");
                receiver.post(NetType.WIFI);

            }else if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.e(Constants.LOG_TAG,"网络已变更，类型为移动数据网络");
                receiver.post(NetType.CMWAP);
            }
        }
    }
}
