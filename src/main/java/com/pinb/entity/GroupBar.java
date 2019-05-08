/**
 * 
 */
package com.pinb.entity;

/**
 * @author chenzhao @date Apr 15, 2019
 */
public class GroupBar extends BaseEntity {

	private String groubTrace;
	private String refUserWxUnionid;
	private String groubName;
	private String groubImg;
	private String groubPhone;
	private String groubAddress;
	private String isOpen;
	// vo冗余添加部分属性
	private String orderTrace;
	private String orderRelationUser;

	public String getGroubTrace() {
		return groubTrace;
	}

	public void setGroubTrace(String groubTrace) {
		this.groubTrace = groubTrace;
	}

	public String getRefUserWxUnionid() {
		return refUserWxUnionid;
	}

	public void setRefUserWxUnionid(String refUserWxUnionid) {
		this.refUserWxUnionid = refUserWxUnionid;
	}

	public String getGroubName() {
		return groubName;
	}

	public void setGroubName(String groubName) {
		this.groubName = groubName;
	}

	public String getGroubImg() {
		return groubImg;
	}

	public void setGroubImg(String groubImg) {
		this.groubImg = groubImg;
	}

	public String getGroubPhone() {
		return groubPhone;
	}

	public void setGroubPhone(String groubPhone) {
		this.groubPhone = groubPhone;
	}

	public String getGroubAddress() {
		return groubAddress;
	}

	public void setGroubAddress(String groubAddress) {
		this.groubAddress = groubAddress;
	}

	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}

	public String getOrderTrace() {
		return orderTrace;
	}

	public void setOrderTrace(String orderTrace) {
		this.orderTrace = orderTrace;
	}
	/**
	 * 分享订单所属用户
	 * @author chenzhao @date May 8, 2019
	 * @return
	 */
	public String getOrderRelationUser() {
		return orderRelationUser;
	}
	/**
	 * 分享订单所属用户
	 * @author chenzhao @date May 8, 2019
	 * @param orderRelationUser
	 */
	public void setOrderRelationUser(String orderRelationUser) {
		this.orderRelationUser = orderRelationUser;
	}
	
	

}
