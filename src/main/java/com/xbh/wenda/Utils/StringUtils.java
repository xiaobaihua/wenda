package com.xbh.wenda.Utils;

/**
 * @author xbh
 * @date 2019年7月28日09:19:55
 * @Description
 */
public class StringUtils {

	public static Boolean isEmpty(Object o) {
		if (o.equals("") || o == null) {
			return true;
		} else {
			return false;
		}
	}
}
