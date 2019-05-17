/**
 * 
 */
package com.pinb.entity;

/**
 * @author chenzhao @date Apr 10, 2019
 */
public class GroubActivity extends BaseEntity {

	private String groubaTrace;
	private String refGroubTrace;
	private String refUserWxUnionid;
	private int groubaSize;
	private int groubaMaxCount;
	private String goodsName;
	private String goodsImg;
	private String goodsPrice;
	private String groubaDiscountAmount;
	private int groubaIsnew;
	private String groubaExpiredTime;
	private String groubaActiveMinute;
	private String province;
	private String city;
	private String latitude;
	private String longitude;
	private String address;
	// #vo冗余字段
	private String orderRefUsers;
	private String userImgs;
	private String ordersStatus;
	private String distance;
	private boolean isJoined;
	private String shareOrder;
	private String shareLeader;

	public String getGroubaTrace() {
		return groubaTrace;
	}

	public void setGroubaTrace(String groubaTrace) {
		this.groubaTrace = groubaTrace;
	}

	public String getRefGroubTrace() {
		return refGroubTrace;
	}

	public void setRefGroubTrace(String refGroubTrace) {
		this.refGroubTrace = refGroubTrace;
	}

	public String getRefUserWxUnionid() {
		return refUserWxUnionid;
	}

	public void setRefUserWxUnionid(String refUserWxUnionid) {
		this.refUserWxUnionid = refUserWxUnionid;
	}

	public int getGroubaSize() {
		return groubaSize;
	}

	public void setGroubaSize(int groubaSize) {
		this.groubaSize = groubaSize;
	}

	public int getGroubaMaxCount() {
		return groubaMaxCount;
	}

	public void setGroubaMaxCount(int groubaMaxCount) {
		this.groubaMaxCount = groubaMaxCount;
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

	public String getGroubaExpiredTime() {
		return groubaExpiredTime;
	}

	public void setGroubaExpiredTime(String groubaExpiredTime) {
		this.groubaExpiredTime = groubaExpiredTime;
	}

	public String getGroubaActiveMinute() {
		return groubaActiveMinute;
	}

	public void setGroubaActiveMinute(String groubaActiveMinute) {
		this.groubaActiveMinute = groubaActiveMinute;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public void setJoined(boolean isJoined) {
		this.isJoined = isJoined;
	}

	/**
	 * 活动商品分享订单下的同团用户
	 * 
	 * @author chenzhao @date May 8, 2019
	 * @return
	 */
	public String getUserImgs() {
		return userImgs;
	}

	/**
	 * 活动商品分享订单下的同团用户
	 * 
	 * @author chenzhao @date May 8, 2019
	 * @param userImgs
	 */
	public void setUserImgs(String userImgs) {
		this.userImgs = userImgs;
	}

	/**
	 * 活动商品分享订单下的同团订单状态
	 * 
	 * @author chenzhao @date May 14, 2019
	 * @return
	 */
	public String getOrdersStatus() {
		return ordersStatus;
	}

	/**
	 * 活动商品分享订单下的同团订单状态
	 * 
	 * @author chenzhao @date May 14, 2019
	 * @param ordersStatus
	 */
	public void setOrdersStatus(String ordersStatus) {
		this.ordersStatus = ordersStatus;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public boolean getIsJoined() {
		return isJoined;
	}

	public void setIsJoined(boolean isJoined) {
		this.isJoined = isJoined;
	}

	/**
	 * 分享订单
	 * 
	 * @author chenzhao @date May 17, 2019
	 * @return
	 */
	public String getShareOrder() {
		return shareOrder;
	}

	/**
	 * 分享订单
	 * 
	 * @author chenzhao @date May 17, 2019
	 * @param shareOrder
	 */
	public void setShareOrder(String shareOrder) {
		this.shareOrder = shareOrder;
	}

	/**
	 * 分享订单-所属团团长
	 * 
	 * @author chenzhao @date May 17, 2019
	 * @return
	 */
	public String getShareLeader() {
		return shareLeader;
	}

	/**
	 * 分享订单-所属团团长
	 * 
	 * @author chenzhao @date May 17, 2019
	 * @param shareLeader
	 */
	public void setShareLeader(String shareLeader) {
		this.shareLeader = shareLeader;
	}

}
