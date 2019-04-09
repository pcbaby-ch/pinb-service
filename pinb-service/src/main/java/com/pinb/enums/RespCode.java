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
	 * 操作成功
	 */
	SUCCESS("0000", "操作成功"),
	/**
	 * resultfulAPI操作成功
	 */
	API_SUCCESS("0000", "操作成功"),
	/**
	 * 操作失败
	 */
	FAILURE("0", "操作失败,%s"),
	/**
	 * 参数非法
	 */
	PARAM_ILLEGAL("00001", "参数[%s]非法,%s"),

	PARAM_INCOMPLETE("00002", "必要参数[%s]残缺"),
	/**
	 * 外部服务请求超时
	 */
	RESTFUL_REQ_TIMEOUT("00005", "外部服务请求超时"),
	/**
	 * 外部服务请求异常,%s
	 */
	RESTFUL_REQ_SERVICEERROR("00006", "外部服务请求异常,%s"),
	/**
	 * 外部服务响应报文为空
	 */
	RESTFUL_REQ_RESPERROR("00007", "外部服务响应报文为空"),
	/**
	 * 主键服务异常
	 */
	SEQUENCE_SERVICE_EXCEPTION("00018", "主键服务异常"),
	// ### 充值中心 03################################################
	/**
	 * 充值中
	 */
	CHARGE_ING("0000", "充值中"),
	/**
	 * 充值失败，供应商服务异常
	 */
	CHARGE_OUTORDER_ERROR("0000", "充值失败，供应商服务异常"),
	/**
	 * 充值，供应商重复回调，跳过处理04001
	 */
	CHARGE_OUTORDER_callBack_ERROR("04001", "充值，供应商重复回调，跳过处理"),
	/**
	 * 分类充值，路由充值异常04021
	 */
	CHARGE_OUTORDER_router_ERROR("04021", "分类充值，路由充值异常"),
	/**
	 * 外部充值订单-内部订单状态变更-失败04022
	 */
	CHARGE_OUTORDER_update_ERROR("04022", "外部充值订单-内部订单状态变更-失败"),
	/**
	 * 供应商重复回调04025
	 */
	CHARGE_OUTORDER_callBackError("04025", "供应商重复回调"),
	/**
	 * 您选择的商品暂不能充值M102
	 */
	CHARGE_check_goodsError("M102", "您选择的商品暂不能充值"),
	/**
	 * 充值通道已关闭 M109
	 */
	RECHARGE_DISABLE("M109", "充值通道已关闭"),
	/**
	 * 您当日充值次数已上限 00020
	 */
	RECHARGE_COUNTMAX("00020", "您当日充值次数已上限"),
	/**
	 * 您选择的商品不存在 M101
	 */
	RECHARGE_GOODS_UNEXIST("M101", "您选择的商品不存在"),
	/**
	 * 账户异常 M112
	 */
	RECHARGE_accountError("M112", "账户异常"),
	/**
	 * 24小时内下单超限 M105
	 */
	RECHARGE_dayMax("M105", "24小时内下单超限"),
	/**
	 * 24小时内被充值超限 M320
	 */
	RECHARGE_dayPayeeMax("M320", "24小时内被充值超限"),
	/**
	 * 30天内下单超限 M106
	 */
	RECHARGE_monthMax("M106", "30天内下单超限"),
	/**
	 * 30天内被充值超限 M220
	 */
	RECHARGE_monthPayeeMax("M220", "30天内被充值超限"),
	/**
	 * 未记录最终使用的供应商 01020
	 */
	RECHARGE_PROVIDER_UNRECORD("01020", "未记录最终使用的供应商"),
	/**
	 * 供应商回调验签失败%s 01025
	 */
	RECHARGE_PROVIDER_signError("01025", "供应商回调验签失败%s"),

	// ### 充值中心跑批***************************************************/
	/**
	 * 任务计划重复配置
	 */
	RECHARGE_batch_scheduleRepeat("03001", "任务计划重复配置"),

	END("0", "未知错误");

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
