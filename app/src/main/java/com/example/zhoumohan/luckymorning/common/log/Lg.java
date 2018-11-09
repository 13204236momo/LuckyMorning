package com.example.zhoumohan.luckymorning.common.log;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Lg {
	
	private  static  boolean DEBUG = true;
	
//	public static void setLog(){
//		if("175".equals(BaseService.API_HOST)){
//			DEBUG = false;
//		}else{
//			DEBUG = true;
//		}
//		
//	}
	
	public static void d(String TAG, String method, String msg) {
		Log.d(TAG, "[" + method + "]" + msg);
	}
	
	public static void d(String TAG, String msg){
		if (DEBUG) {
			Log.d(TAG, "[" + getFileLineMethod() + "]" + msg);
		}
	}
	
	public static void d(String msg){
		if (DEBUG) {
			Log.d(_FILE_(), "[" + getLineMethod() + "]" + msg);
		}
	}
	
	public static void e(String msg){
		if (DEBUG) {
			Log.e(_FILE_(), getLineMethod() + msg);
		}
	}
	
	public static void e(String TAG, String msg){
		if (DEBUG) {
			Log.e(TAG, getLineMethod() + msg);
		}
	}

	
	public static void i(String TAG, String msg){
		if (DEBUG) {
			Log.i(TAG, msg);
		}
	}
	public static String getFileLineMethod() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
		StringBuffer toStringBuffer = new StringBuffer("[")
				.append(traceElement.getFileName()).append(" | ")
				.append(traceElement.getLineNumber()).append(" | ")
				.append(traceElement.getMethodName()).append("]");
		return toStringBuffer.toString();
	}
	
	public static String getLineMethod() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
		StringBuffer toStringBuffer = new StringBuffer("[")
				.append(traceElement.getLineNumber()).append(" | ")
				.append(traceElement.getMethodName()).append("]");
		return toStringBuffer.toString();
	}

	public static String _FILE_() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
		return traceElement.getFileName();
	}

	public static String _FUNC_() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
		return traceElement.getMethodName();
	}

	public static int _LINE_() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
		return traceElement.getLineNumber();
	}

	public static String _TIME_() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return sdf.format(now);
	}
}