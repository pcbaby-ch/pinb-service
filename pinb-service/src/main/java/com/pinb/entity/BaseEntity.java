package com.pinb.entity;

/**
 * 
 * @author chenzhao @date Apr 9, 2019
 */
public class BaseEntity{

	/** 时间区间 */
	private String startTime;
	private String endTime;
	
	/** 分页 */
	private Integer page;
	private Integer rows;
	
	
	/** 登陆用户唯一标识  */
	private String userWxUnionid;
	
	private String userWxOpenid;
	
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

	public String getUserWxUnionid() {
		return userWxUnionid;
	}

	public void setUserWxUnionid(String userWxUnionid) {
		this.userWxUnionid = userWxUnionid;
	}

	public String getUserWxOpenid() {
		return userWxOpenid;
	}

	public void setUserWxOpenid(String userWxOpenid) {
		this.userWxOpenid = userWxOpenid;
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
	
	
	
	
	
}
