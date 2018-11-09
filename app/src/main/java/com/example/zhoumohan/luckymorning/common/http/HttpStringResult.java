package com.example.zhoumohan.luckymorning.common.http;

import java.io.Serializable;

/**
 * Http返回结果
 *
 */
public class HttpStringResult implements Serializable {
	private static final long serialVersionUID = -3499016800119861019L;
	public String result;
	/**
	 * HTTP状态码
	 */
	public int resCode = -1;
	public String msg;
	public void setErrorMsg(String msg){
		this.msg = msg;
	}
	
	public void setResult(String xml) {
		//<xml> </xml>
		//this.result = Html.fromHtml(xml).toString();
		this.result = xml;
	}
	
	public String getResult(){
		return result;
	}
}
