package com.example.zhoumohan.luckymorning.common.http;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 
 * 负责处理得到泛型类中的方法和属性
 * @author liguotao
 *
 * @param <T>
 */
public abstract class ServiceCallback<T>{
    public Type type;
    public ServiceCallback(){  
        Type mySuperClass = getClass().getGenericSuperclass();
        Type type = ((ParameterizedType)mySuperClass).getActualTypeArguments()[0];
        this.type = type;
    }
    public abstract void done(int what, T obj);
    public abstract void error(String msg);
}