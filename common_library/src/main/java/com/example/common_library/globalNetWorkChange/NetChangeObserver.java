package com.example.common_library.globalNetWorkChange;

public interface NetChangeObserver {
    /**
     * 网络连接时调用
     * @param netType
     */
    void onConnect(NetType netType);

    /**
     * 当前没有网络连接
     */
    void onDisConnect();
}
