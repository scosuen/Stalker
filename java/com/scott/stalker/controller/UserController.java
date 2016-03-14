package com.scott.stalker.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.scott.stalker.bll.UserBll;
import com.scott.stalker.model.User;
import com.scott.stalker.utils.CommonJSONBuilder;

@RestController
@RequestMapping(value = "/user")
public class UserController {
	
	@Autowired
	private UserBll userBll;
	
	/**
	 * request :  {"userName":"xxx", "pwd":"xxx"}
	 * response : {"success": "true", "user": {"userName":"xxx", "accessToken":"xxx", "companyName":"xxx"}}
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login (@RequestBody String json) {
		
		JSONObject messageJSON = new JSONObject(json);
		User user = new User();
		user.setUserName(messageJSON.getString("userName"));
		user.setPwd(messageJSON.getString("pwd"));
		
		if (user.validatePwd() == false)
			return CommonJSONBuilder.getResponseJSON(1, "Please enter your username and password."); //failed
		
		
		User result = userBll.login(user);
		if (result == null) 
			return CommonJSONBuilder.getResponseJSON(2, "Wrong username or password."); //failed
		return getResponseJSON(result);
	}
	
	/**
	 * {"success": "true", "user": {"userName":"xxx", "accessToken":"xxx", "companyName":"xxx"}}
	 * @param user
	 * @return
	 */
	private String getResponseJSON (User user) {
		StringBuffer json = new StringBuffer();
		json.append(" {\"success\": \"true\", \"user\": {");
		json.append(" \"userName\":\"" + user.getUserName() + "\",");
		json.append(" \"accessToken\":\"" + user.getAccessToken() + "\",");
		json.append(" \"companyName\":\"" + user.getCompanyName() + "\"}}");
		
		return json.toString();
	}
	
	@RequestMapping(value = "/validate", method = RequestMethod.POST)
	public String validateUser (@RequestBody String json) {
		JSONObject messageJSON = new JSONObject(json);
		User user = new User();
		user.setUserName(messageJSON.getString("userName"));
		user.setAccessToken(messageJSON.getString("accessToken"));
		
		if (user.validateAccessToken() == false)
			return CommonJSONBuilder.getResponseJSON(3, "Invalid user or login has expired.");
		
		User result = userBll.validate(user);
		if (result == null) 
			return CommonJSONBuilder.getResponseJSON(3, "Invalid user or login has expired.");
		
		return CommonJSONBuilder.getSuccResponseJSON();
	}
	
	@RequestMapping(value = "/updatePwd/{userName}/{pwd}", method = RequestMethod.GET)
	public String updatePwd (@PathVariable("userName") String userName, @PathVariable("pwd") String pwd) {
		User user = new User();
		user.setUserName(userName);
		user.setPwd(pwd);
		
		if (user.validatePwd() == false)
			return CommonJSONBuilder.getResponseJSON(1, "Please enter your username and password."); //failed
		
		User result = userBll.updatePwd(user);
		if (result == null) 
			return CommonJSONBuilder.getResponseJSON(4, "User dosen't exist.");
		
		return CommonJSONBuilder.getSuccResponseJSON();
	}
	
	@RequestMapping(value = "/logout/{userName}/{accessToken}", method = RequestMethod.GET)
	public String logout (@PathVariable("userName") String userName, @PathVariable("accessToken") String accessToken) {
		User user = new User();
		user.setUserName(userName);
		user.setAccessToken(accessToken);
		
		if (user.validateAccessToken() == false)
			return CommonJSONBuilder.getResponseJSON(3, "Invalid user or login has expired.");
		User validateUser = userBll.validate(user);
		if (validateUser == null) 
			return CommonJSONBuilder.getResponseJSON(3, "Invalid user or login has expired.");
		
		User result = userBll.logout(user);
		
		if (result != null)
			return CommonJSONBuilder.getSuccResponseJSON();
		
		return CommonJSONBuilder.getResponseJSON(3, "Logout failed.");
	}
	
	
}
