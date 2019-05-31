/**
 * 
 */
package com.pinb.vo;


/**
 * @author chenzhao @date Apr 15, 2019
 */
public class TemplateMsgVo {

	private String appid;
	private String secret;
	private String touser;
	private String templateId;
	

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

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

}
