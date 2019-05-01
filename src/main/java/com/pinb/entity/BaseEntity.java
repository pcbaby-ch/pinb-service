package com.pinb.entity;

/**
 * 
 * @author chenzhao @date Apr 9, 2019
 */
public class BaseEntity {

	/** 时间区间 */
	private String startTime;
	private String endTime;

	/** 分页 */
	private Integer page;
	private Integer rows;

	// # 公参<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	private String wxUnionid;
	private String wxOpenid;
	private String sign;
	private String clientIp;
	// 客户端信息
	
	// # 公参>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	private String intime;
	private String uptime;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public String getWxUnionid() {
		return wxUnionid;
	}

	public void setWxUnionid(String wxUnionid) {
		this.wxUnionid = wxUnionid;
	}

	public String getWxOpenid() {
		return wxOpenid;
	}

	public void setWxOpenid(String wxOpenid) {
		this.wxOpenid = wxOpenid;
	}

	public String getIntime() {
		return intime;
	}

	public void setIntime(String intime) {
		this.intime = intime;
	}

	public String getUptime() {
		return uptime;
	}

	public void setUptime(String uptime) {
		this.uptime = uptime;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}


}
