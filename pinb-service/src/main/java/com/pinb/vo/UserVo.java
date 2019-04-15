/**
 * 
 */
package com.pinb.vo;

import com.pinb.entity.User;

/**
 * @author chenzhao @date Apr 15, 2019
 */
public class UserVo extends User {

	private String appid;
	private String secret;
	private String jsCode;
	private String grantType;
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getJsCode() {
		return jsCode;
	}
	public void setJsCode(String jsCode) {
		this.jsCode = jsCode;
	}
	public String getGrantType() {
		return grantType;
	}
	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	
}
