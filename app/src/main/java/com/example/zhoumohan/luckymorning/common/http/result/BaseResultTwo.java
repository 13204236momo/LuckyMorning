package com.example.zhoumohan.luckymorning.common.http.result;

import java.io.Serializable;

/**
 * 基类 http请求返回的数据结构类
 */
public class BaseResultTwo implements Serializable {
	private static final long serialVersionUID = -2592849950094769081L;
	public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_FAILE = 0;
	/**
	 * 服务器返回的状态码
	 */
	public int status;
	/**
	 * 服务器返回的消息
	 */
	public String msg;
	
	
}
