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
	private String refUserImg;
	private String orderShareCount;
	private String joinSucceedTime;
	private String consumeTime;
	private String consumeSuccessTime;
	// #vo冗余字段
	private String userImgs;
	private String shareUser;

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

	public String getUserImgs() {
		return userImgs;
	}

	public void setUserImgs(String userImgs) {
		this.userImgs = userImgs;
	}
	/**
	 * 团订单分享发起者
	 * @return
	 */
	public String getShareUser() {
		return shareUser;
	}
	/**
	 * 团订单分享发起者
	 * @param shareUser
	 */
	public void setShareUser(String shareUser) {
		this.shareUser = shareUser;
	}
	

}
