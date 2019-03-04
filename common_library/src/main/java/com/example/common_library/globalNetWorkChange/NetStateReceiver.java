package com.example.common_library.globalNetWorkChange;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import android.util.Log;

import com.example.common_library.globalNetWorkChange.utils.Constants;
import com.example.common_library.globalNetWorkChange.utils.NetworkUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetStateReceiver extends BroadcastReceiver {

    private NetType netType;
    private Map<Object, List<MethodManager>> networkList;

    public NetStateReceiver() {
        //初始化没有网络
        this.netType = NetType.NONE;
        networkList = new HashMap<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Log.e(Constants.LOG_TAG, "异常啦");
            return;
        }

        //处理广播事件
        if (intent.getAction().equalsIgnoreCase(Constants.ANDROID_NET_CHANGE_ACTION)) {
            Log.e(Constants.LOG_TAG, "网络发生改变");
            netType = NetworkUtils.getNetType();
            if (NetworkUtils.isNetWoekAvailable()) {
                Log.e(Constants.LOG_TAG, "有网络");
            } else {
                Log.e(Constants.LOG_TAG, "无网络");
            }
        }

        //通知所有注册的方法网络发生了变化
        post(netType);
    }

    /**
     * 同时分发出去
     *
     * @param netType
     */
    private void post(NetType netType) {
        Set<Object> set = networkList.keySet();
        for (final Object getter : set) {
            //所有注解方法
            List<MethodManager> methodList = networkList.get(getter);
            if (methodList != null) {
                //循环每个方法
                for (MethodManager methodManager : methodList) {
                    if (methodManager.getType().isAssignableFrom(netType.getClass())) {
                        switch (methodManager.getNetType()) {
                            case AUTO:
                                invoke(methodManager,getter,netType);
                                break;
                            case WIFI:
                                if (netType == NetType.WIFI || netType == NetType.NONE){
                                    invoke(methodManager,getter,netType);
                                }
                                break;
                            case CMWAP:
                                if (netType == NetType.CMWAP || netType == NetType.NONE){
                                    invoke(methodManager,getter,netType);
                                }
                                break;
                            case CMNET:
                                if (netType == NetType.CMNET || netType == NetType.NONE){
                                    invoke(methodManager,getter,netType);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    private void invoke(MethodManager methodManager, Object getter, NetType netType) {
        Method execute = methodManager.getMethod();
        try {
            execute.invoke(getter,netType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void register(Object object) {
        //获取注册者的所有的网络监听注解方法
        List<MethodManager> managerList = networkList.get(object);
        if (managerList == null) {
            //开始添加方法
            managerList = findAnnotationMethod(object);
            networkList.put(object, managerList);
        }
    }

    private List<MethodManager> findAnnotationMethod(Object object) {
        List<MethodManager> managerList = new ArrayList<>();
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            Network network = method.getAnnotation(Network.class);
            if (network == null) {
                continue;
            }

            //方法返回校验
            Type returnType = method.getGenericReturnType();
            if (!"void".equals(returnType.toString())) {
                throw new RuntimeException(method.getName() + "方法返回必须是void类型");
            }

            //参数校验
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new RuntimeException(method.getName() + "方法只能有一个参数");
            }

            //过滤上面，得到符合要求的方法，才开始添加到集合
            MethodManager manager = new MethodManager(parameterTypes[0], network.netType(), method);
            managerList.add(manager);

        }
        return managerList;
    }

    public void unRegister(Object object) {
        if (!networkList.isEmpty()) {
            networkList.remove(object);
        }
        Log.e(Constants.LOG_TAG, object.getClass().getName() + "解注册成功");
    }

    public void unRegisterAll() {
        if (!networkList.isEmpty()) {
            networkList.clear();
        }
        NetworkManager.getDefault().getApplication().unregisterReceiver(this);
        networkList = null;
        Log.e(Constants.LOG_TAG, "全部解注册成功");
    }
}

