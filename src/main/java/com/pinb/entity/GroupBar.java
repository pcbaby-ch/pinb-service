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
	private String province;
	private String city;
	private String latitude;
	private String longitude;
	// vo冗余添加部分属性
	private String orderTrace;
	private String orderLeader;

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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * 被分享的订单
	 * 
	 * @author chenzhao @date May 16, 2019
	 * @return
	 */
	public String getOrderTrace() {
		return orderTrace;
	}

	public void setOrderTrace(String orderTrace) {
		this.orderTrace = orderTrace;
	}

	/**
	 * 被分享订单的团长
	 * 
	 * @author chenzhao @date May 16, 2019
	 * @return
	 */
	public String getOrderLeader() {
		return orderLeader;
	}

	/**
	 * 被分享订单的团长
	 * 
	 * @author chenzhao @date May 16, 2019
	 * @param orderLeader
	 */
	public void setOrderLeader(String orderLeader) {
		this.orderLeader = orderLeader;
	}

}
