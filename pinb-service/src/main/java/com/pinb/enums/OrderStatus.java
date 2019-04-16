package com.pinb.enums;

import java.util.Objects;

import com.pinb.common.ServiceException;

/**
 * 拼吧-订单状态
 * 
 * @author chenzhao @date Apr 16, 2019
 */
public enum OrderStatus {
	/**
	 * "0", "已参团"
	 */
	join(0, "已参团"),
	/**
	 * "3", "已成团"
	 */
	join_success(3, "已成团"),
	/**
	 * "5", "消费中"
	 */
	consume(6, "消费中"),
	/**
	 * "6", "已消费"
	 */
	consume_success(8, "已消费");

	private int code;
	private String msg;

	OrderStatus(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static OrderStatus getRespByCode(int code) {
		for (OrderStatus resp : values()) {
			if (resp.getCode()==code) {
				return resp;
			}
		}
		throw new ServiceException("无效的code值!code:" + code);
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public boolean isSuccess(int code) {
		return Objects.equals(code, this.code);
	}
}
