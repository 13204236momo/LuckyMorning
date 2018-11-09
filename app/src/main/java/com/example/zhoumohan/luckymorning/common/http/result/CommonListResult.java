package com.example.zhoumohan.luckymorning.common.http.result;

import java.util.ArrayList;

/**
 * 公共列表返回数据
 *
 * @param <T>
 */
public class CommonListResult<T> extends BaseResult{
	private static final long serialVersionUID = -283042288791896637L;
	public ArrayList<T> data;
}
