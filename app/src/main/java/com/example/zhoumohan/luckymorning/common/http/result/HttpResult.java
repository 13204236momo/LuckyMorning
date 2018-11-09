package com.example.zhoumohan.luckymorning.common.http.result;

import java.io.Serializable;

/**
 * Http返回
 *
 */
public class HttpResult implements Serializable {
	private static final long serialVersionUID = 3289545910542470664L;
	public String result;
	/**
	 * HTTP状态码
	 */
	public int resCode = -1;
	public String url;
	public String msg;
	
	public HttpResult(int resCode) {
		this.resCode = resCode;
	}
}
