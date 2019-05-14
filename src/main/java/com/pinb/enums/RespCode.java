package com.pinb.enums;

import java.util.Objects;

/**
 * 响应异常码统一管理表(业务异常码规约，前2位：模块编号，后3位：异常编号
 * 
 * @author chen.zhao (chenzhao) @DATE: 2018年3月5日
 */
public enum RespCode {
	// #公共模块 编码前缀：00
	/**
	 * "10000", "操作成功"
	 */
	SUCCESS("10000", "操作成功"),
	/**
	 * resultfulAPI "10000", "操作成功"
	 */
	API_SUCCESS("10000", "操作成功"),
	/**
	 * "11111", "操作失败,%s"
	 */
	FAILURE("11111", "操作失败,%s"),
	/**
	 * "10001", "参数[%s]非法,%s"
	 */
	PARAM_ILLEGAL("10001", "参数[%s]非法"),
	/**
	 * "10002", "必要参数[%s]残缺"
	 */
	PARAM_INCOMPLETE("10002", "必要参数[%s]残缺"),
	/**
	 * "10005", "外部服务请求超时"
	 */
	RESTFUL_REQ_TIMEOUT("10005", "外部服务请求超时"),
	/**
	 * "10006", "外部服务请求异常,%s"
	 */
	RESTFUL_REQ_SERVICEERROR("10006", "外部服务请求异常,%s"),
	/**
	 * "10007", "外部服务响应报文为空"
	 */
	RESTFUL_REQ_RESPERROR("10007", "外部服务响应报文为空"),
	// #拼吧-用户模块#############################################
	/**
	 * "20001","您未入驻，请完善店铺、商品信息"
	 */
	user_unExist("20001", "请完善店铺、商品信息，闪电入驻"),
	
	
	// #拼吧-订单模块#############################################
	/**
	 * "30001","未成团，不能消费"
	 */
	order_unJoinSuccess("30001", "未成团，不能消费"),
	/**
	 * "30005", "已成团，不能重复消费"
	 */
	order_unRepeatConsume("30005", "已成团，不能重复消费"),
	/**
	 * "30007", "参团失败，还未开团
	 */
	order_unOpenOrder("30007", "参团失败，还未开团"),
	/**
	 * "30009", "参团失败，你已参团"
	 */
	order_joinedGrouba("30009", "参团失败，你已参团"),
	/**
	 * "30009", "未找到团订单"
	 */
	order_unExistOrderTrace("30009", "未找到团订单"),
	/**
	 * "30011", "消费失败，消费订单不属于该拼吧"
	 */
	order_groubError("30011", "消费失败，消费订单不属于该拼吧"),

	
	// #拼吧-公共#############################################
	/**
	 * "80011", "文件不存在，未读取到文件流"
	 */
	file_unExist("80011", "文件不存在，请重试"),
	/**
	 * "80013", "文件md5值错误"
	 */
	file_fileMd5Error("80013", "%s文件md5值错误,正确md5:%s"),

	
	
	
	
	
	/**
	 * "66666", "服务繁忙"
	 */
	END("66666", "服务繁忙");

	private String code;
	private String msg;

	RespCode(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static RespCode getRespByCode(String code) {
		if (code == null) {
			return null;
		}
		for (RespCode resp : values()) {
			if (resp.getCode().equals(code)) {
				return resp;
			}
		}
		throw new IllegalArgumentException("无效的code值!code:" + code);
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public boolean isSuccess(String code) {
		return Objects.equals(code, this.code);
	}
}
