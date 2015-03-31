package com.pxs.util;

import org.json.JSONObject;

/** 时间等弹出控件接�?
 * @author peng_xuesong
 *
 */
public interface PluginInterface {
	/** 显示控件
	 * @param data
	 */
	void show(JSONObject data);
	/** 处理结果
	 * @param key  html元素id
	 * @param domEntity
	 * @throws Exception
	 */
	void sendResult(String key,DomEntity domEntity) throws Exception;
}
