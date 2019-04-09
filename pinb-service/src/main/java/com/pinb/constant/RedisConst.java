package com.pinb.constant;


/**
 * redis key 记录常量类（防止key冲突）
 * 
 * @author chenzhao @date Nov 30, 2018
 */
public class RedisConst {

	/**
	 * 充值中心，近期充值历史记录时间游标rechargeRecentChargeTime
	 */
	public static String rechargeRecentChargeTime = "rechargeRecentChargeTime";
	/**
	 * 充值中心，所有跑批job的实例任务lock
	 */
	public static String rechargeJobInstanceTraceLock = "rechargeJobInstanceTraceLock";
	/**
	 * 充值中心，所有跑批job的实例任务trace
	 */
	public static String rechargeJobInstanceTracePrefix = "rechargeJobInstanceTracePrefix_";
	/**
	 * 充值中心，所有充值通道的启用or禁用redis前缀GOFTBAG.STATUS.
	 */
	public static String rechargeChannelClosePrefix = "GOFTBAG.STATUS.";
	/**
	 * 供应商token billService_providerCallBackOrderIdPrefix_
	 */
	public static String providerCallBackOrderIdPrefix = "billService_providerCallBackOrderIdPrefix_";
	/**
	 * 供应商token billService_providerToken_
	 */
	public static String providerToken = "billService_providerToken_";
	/**
	 * 视频vip充值-备选供应商billService_viceProviderVideo
	 */
	public static String viceProviderVideo = "billService_viceProviderVideo";
	/**
	 * 视频vip充值-主供应商billService_masterProviderVideo
	 */
	public static String masterProviderVideo = "billService_masterProviderVideo";
	/**
	 * Q币充值-备选供应商billService_phoneViceProviderQcoin
	 */
	public static String viceProviderQcoin = "billService_viceProviderQcoin";
	/**
	 * Q币充值-主供应商billService_phoneMasterProviderQcoin
	 */
	public static String masterProviderQcoin = "billService_masterProviderQcoin";
	/**
	 * 话费流量-备选供应商billService_phoneViceProvider
	 */
	public static String phoneViceProvider = "billService_phoneViceProvider";
	/**
	 * 话费流量-主供应商billService_phoneMasterProvider
	 */
	public static String phoneMasterProvider = "billService_phoneMasterProvider";
	/**
	 * 手机号运营商billService_phoneOperatorCode_
	 */
	public static String phoneOperatorCode = "billService_phoneOperatorCode_";
	/**
	 * 充值商品类型billService_goodsTypeCode_
	 */
	public static String goodsTypeCode = "billService_goodsTypeCode_";
	/**
	 * 商城订单流水号billService_OrderFlowNum
	 */
	public static final String OrderFlowNum = "billService_OrderFlowNum";
	public static final String OrderFlowSettleNum = "billService_OrderFlowSettleNum";
	public static final String OrderFlowRefundNum = "billService_OrderFlowRefundNum";

	/**
	 * 商城订单产品流水号billService_OrderProductFlowNum
	 */
	public static final String OrderProductFlowNum = "billService_OrderProductFlowNum";
	public static final String OrderProductFlowRefundNum = "billService_OrderProductFlowRefundNum";
	public static final String OrderProductSettleFlowNum = "billService_OrderProductSettleFlowNum";

}
