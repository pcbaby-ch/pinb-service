package com.pinb.util;
/**
 *
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.pinb.enums.RespCode;

/**
 * 响应数据
 * 
 * @author chen.zhao (chenzhao) @DATE: 2018年3月1日
 */
public class RespUtil {

	/**
	 * datagrid表格列表-web
	 * 
	 * @param page
	 */
	public static Map<String, Object> listResp(Page<?> page) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", page.getTotal());
		map.put("rows", page.getResult());
		if (page.getTotal() > 0) {
			map.put("retCode", RespCode.SUCCESS.getCode());
			map.put("retMsg", RespCode.SUCCESS.getMsg());
		} else {
			map.put("retCode", RespCode.FAILURE.getCode());
			map.put("retMsg", RespCode.FAILURE.getMsg());
		}
		return map;
	}

	/**
	 * 基础业务操作-web 适配响应：新增、修改、删除、业务操作。。。
	 * 
	 * @param result true:操作成功 false：操作失败
	 * @return
	 */
	public static Map<String, Object> baseResp(boolean result) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result) {
			map.put("retCode", RespCode.SUCCESS.getCode());
			map.put("retMsg", RespCode.SUCCESS.getMsg());
		} else {
			map.put("retCode", RespCode.FAILURE.getCode());
			map.put("retMsg", RespCode.FAILURE.getMsg());
		}
		return map;
	}

	/**
	 * 平台resultAPI接口交互响应
	 * 
	 * @param result
	 * @return
	 */
	public static Map<String, Object> apiResp(boolean result) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result) {
			map.put("retCode", RespCode.API_SUCCESS.getCode());
			map.put("retMsg", RespCode.API_SUCCESS.getMsg());
		} else {
			map.put("retCode", RespCode.FAILURE.getCode());
			map.put("retMsg", RespCode.FAILURE.getMsg());
		}
		return map;
	}

	/**
	 * 业务invoke成功带data响应业务数据 适配响应：详情查看操作。。。
	 * 
	 * @param data 不限定格式规约
	 * @return
	 */
	public static Map<String, Object> dataResp(Object data) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("retCode", RespCode.SUCCESS.getCode());
		map.put("retMsg", RespCode.SUCCESS.getMsg());
		map.put("data", data);
		return map;
	}

	/**
	 * 
	 * 适配响应：。。。
	 * 
	 * @param data 不限定格式规约
	 * @return
	 */
	public static Map<String, Object> codeDateResp(RespCode respEnum, Object data) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("retCode", respEnum.getCode());
		map.put("retMsg", respEnum.getMsg());
		map.put("data", data);
		return map;
	}

	/**
	 *
	 * 适配响应：。。。
	 * 
	 * @param respEnum 不限定格式规约
	 * @return
	 */
	public static Map<String, Object> codeResp(RespCode respEnum) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("retCode", respEnum.getCode());
		map.put("retMsg", respEnum.getMsg());
		return map;
	}

	public static Map<String, Object> listResp(List<?> list) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", list.size());
		map.put("rows", list);
		return map;
	}

}
