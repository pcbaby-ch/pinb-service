/**
 * 
 */
package com.pinb.entity;

/**
 * @author chenzhao @date Apr 15, 2019
 */
public class User extends BaseEntity {

	private String phone;
	private String headImg;
	private String creditScoreUser;
	private String isOpenGroub;
	private String creditScoreGroub;

	private String brand;
	private String model;
	private String system;
	private String platform;
	private String benchmark;
	private String nickname;
	private int gender;
	private String city;
	private String province;
	private String latitude;
	private String longitude;


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getCreditScoreUser() {
		return creditScoreUser;
	}

	public void setCreditScoreUser(String creditScoreUser) {
		this.creditScoreUser = creditScoreUser;
	}

	public String getIsOpenGroub() {
		return isOpenGroub;
	}

	public void setIsOpenGroub(String isOpenGroub) {
		this.isOpenGroub = isOpenGroub;
	}

	public String getCreditScoreGroub() {
		return creditScoreGroub;
	}

	public void setCreditScoreGroub(String creditScoreGroub) {
		this.creditScoreGroub = creditScoreGroub;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getBenchmark() {
		return benchmark;
	}

	public void setBenchmark(String benchmark) {
		this.benchmark = benchmark;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
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

	

}
