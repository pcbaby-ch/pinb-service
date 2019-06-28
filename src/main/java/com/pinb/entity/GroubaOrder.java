/**
 * 
 */
package com.pinb.entity;

/**
 * @author chenzhao @date Apr 9, 2019
 */
public class GroubaOrder extends BaseEntity {

	private String orderTrace;
	private String refGroubTrace;
	private String refGroubaTrace;
	private String orderExpiredTime;
	private String orderStatus;
	private String refUserWxUnionid;
	private String refUserWxOpenid;
	private String leader;
	private String refUserImg;
	private String orderShareCount;
	private String joinSucceedTime;
	private String consumeTime;
	private String consumeSuccessTime;
	private int groubaSize;
	private String goodsName;
	private String goodsImg;
	private String goodsPrice;
	private String groubaDiscountAmount;
	private int groubaIsnew;
	private String intime;
	private String dGoodsImgs;
	// #vo冗余字段
	private String orderRefUsers;
	private String userImgs;
	private String ordersStatus;
	private boolean isJoined;
	private String shareOrder;
	private String shareLeader;
	private String formId;

	/**
	 * @param orderTrace
	 * @param refUserWxUnionid
	 * @param orderStatus
	 */
	public GroubaOrder(String orderTrace, String refUserWxUnionid, String orderStatus) {
		super();
		this.orderTrace = orderTrace;
		this.refUserWxUnionid = refUserWxUnionid;
		this.orderStatus = orderStatus;
	}

	/**
	 * 
	 */
	public GroubaOrder() {
		super();
	}

	public String getOrderTrace() {
		return orderTrace;
	}

	public void setOrderTrace(String orderTrace) {
		this.orderTrace = orderTrace;
	}

	public String getRefGroubTrace() {
		return refGroubTrace;
	}

	public void setRefGroubTrace(String refGroubTrace) {
		this.refGroubTrace = refGroubTrace;
	}

	public String getRefGroubaTrace() {
		return refGroubaTrace;
	}

	public void setRefGroubaTrace(String refGroubaTrace) {
		this.refGroubaTrace = refGroubaTrace;
	}

	public String getOrderExpiredTime() {
		return orderExpiredTime;
	}

	public void setOrderExpiredTime(String orderExpiredTime) {
		this.orderExpiredTime = orderExpiredTime;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getRefUserWxUnionid() {
		return refUserWxUnionid;
	}

	public void setRefUserWxUnionid(String refUserWxUnionid) {
		this.refUserWxUnionid = refUserWxUnionid;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public String getRefUserImg() {
		return refUserImg;
	}

	public void setRefUserImg(String refUserImg) {
		this.refUserImg = refUserImg;
	}

	public String getOrderShareCount() {
		return orderShareCount;
	}

	public void setOrderShareCount(String orderShareCount) {
		this.orderShareCount = orderShareCount;
	}

	public String getJoinSucceedTime() {
		return joinSucceedTime;
	}

	public void setJoinSucceedTime(String joinSucceedTime) {
		this.joinSucceedTime = joinSucceedTime;
	}

	public String getConsumeTime() {
		return consumeTime;
	}

	public void setConsumeTime(String consumeTime) {
		this.consumeTime = consumeTime;
	}

	public String getConsumeSuccessTime() {
		return consumeSuccessTime;
	}

	public void setConsumeSuccessTime(String consumeSuccessTime) {
		this.consumeSuccessTime = consumeSuccessTime;
	}

	public String getRefUserWxOpenid() {
		return refUserWxOpenid;
	}

	public void setRefUserWxOpenid(String refUserWxOpenid) {
		this.refUserWxOpenid = refUserWxOpenid;
	}

	public int getGroubaSize() {
		return groubaSize;
	}

	public void setGroubaSize(int groubaSize) {
		this.groubaSize = groubaSize;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsImg() {
		return goodsImg;
	}

	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getGroubaDiscountAmount() {
		return groubaDiscountAmount;
	}

	public void setGroubaDiscountAmount(String groubaDiscountAmount) {
		this.groubaDiscountAmount = groubaDiscountAmount;
	}

	public int getGroubaIsnew() {
		return groubaIsnew;
	}

	public void setGroubaIsnew(int groubaIsnew) {
		this.groubaIsnew = groubaIsnew;
	}

	public String getIntime() {
		return intime;
	}

	public void setIntime(String intime) {
		this.intime = intime;
	}

	public String getdGoodsImgs() {
		return dGoodsImgs;
	}

	public void setdGoodsImgs(String dGoodsImgs) {
		this.dGoodsImgs = dGoodsImgs;
	}

	/**
	 * 同团所有订单的归属用户
	 * 
	 * @author chenzhao @date May 16, 2019
	 * @return
	 */
	public String getOrderRefUsers() {
		return orderRefUsers;
	}

	/**
	 * 同团所有订单的归属用户
	 * 
	 * @author chenzhao @date May 16, 2019
	 * @param orderRefUsers
	 */
	public void setOrderRefUsers(String orderRefUsers) {
		this.orderRefUsers = orderRefUsers;
	}

	public String getUserImgs() {
		return userImgs;
	}

	public void setUserImgs(String userImgs) {
		this.userImgs = userImgs;
	}

	/**
	 * 同团所有订单的状态
	 * 
	 * @return
	 */
	public String getOrdersStatus() {
		return ordersStatus;
	}

	/**
	 * 同团所有订单的状态
	 * 
	 * @param ordersStatus
	 */
	public void setOrdersStatus(String ordersStatus) {
		this.ordersStatus = ordersStatus;
	}

	public boolean getIsJoined() {
		return isJoined;
	}

	public void setIsJoined(boolean isJoined) {
		this.isJoined = isJoined;
	}

	public String getShareOrder() {
		return shareOrder;
	}

	public void setShareOrder(String shareOrder) {
		this.shareOrder = shareOrder;
	}

	public String getShareLeader() {
		return shareLeader;
	}

	public void setShareLeader(String shareLeader) {
		this.shareLeader = shareLeader;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}
	

}
