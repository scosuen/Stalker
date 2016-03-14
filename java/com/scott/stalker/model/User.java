package com.scott.stalker.model;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import com.scott.stalker.utils.MD5Utils;

public class User {

	private String userName;
	private String pwd;
	private String accessToken;
	private Date lastActivityTime;
	private String companyName;

	public String getUserName() {
		return userName;
	}

	public String getLowercaseUserName() {
		if (this.getUserName() == null)
			return null;
		return this.getUserName().toLowerCase();
	}

	public void setUserName(String userName) {
		if (userName != null) 
			this.userName = userName.trim();
	}

	public String getPwd() {
		return pwd;
	}

	public String getEncrypedPwd() throws NoSuchAlgorithmException {
		if (getPwd() == null)
			return null;
		return MD5Utils.encryption(getPwd());
	}

	public void setPwd(String pwd) {
		if (pwd != null)
			this.pwd = pwd.trim();
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		if (accessToken != null)
			this.accessToken = accessToken.trim();
	}

	public Date getLastActivityTime() {
		return lastActivityTime;
	}

	public void setLastActivityTime(Date lastActivityTime) {
		this.lastActivityTime = lastActivityTime;
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


	public boolean validateAccessToken() {
		if (getLowercaseUserName() == null || getLowercaseUserName().length() <= 0 || getAccessToken() == null
				|| getAccessToken().length() <= 0)
			return false;

		return true;
	}
	
	public boolean validatePwd() {
		if (getLowercaseUserName() == null || getLowercaseUserName().length() <= 0 || getPwd() == null
				|| getPwd().length() <= 0)
			return false;

		return true;
	}

}
