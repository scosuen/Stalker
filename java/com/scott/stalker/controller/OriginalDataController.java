package com.scott.stalker.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.scott.stalker.bll.OriginalDataBll;
import com.scott.stalker.model.User;
import com.scott.stalker.utils.CommonJSONBuilder;

@RestController
@RequestMapping(value = "/originalData")
public class OriginalDataController {

	@Autowired
	private OriginalDataBll originalDataBll;

	@RequestMapping(value = "/importPurcharedOriginalData", method = RequestMethod.POST)
	public String importPurcharedOriginalData(@RequestParam("file") MultipartFile uploadFile,
			@RequestParam(value = "userName") String userName, 
			@RequestParam(value = "accessToken") String accessToken) {
		User user = new User();
		user.setUserName(userName);
		user.setAccessToken(accessToken);
		
		String errorMsg = null;
		try {
			File excelFile = new File( uploadFile.getOriginalFilename());
			uploadFile.transferTo(excelFile);
			errorMsg = originalDataBll.importPurchasedOriginalData(excelFile, user);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (errorMsg == null || errorMsg.length() <= 0)
			return CommonJSONBuilder.getSuccResponseJSON();
		return CommonJSONBuilder.getResponseJSON(9, errorMsg);
	}
	
	@RequestMapping(value = "/importDeliveredOriginalData", method = RequestMethod.POST)
	public String importDeliveredOriginalData (@RequestParam("file") MultipartFile uploadFile,
			@RequestParam(value = "userName") String userName, 
			@RequestParam(value = "accessToken") String accessToken) {
		User user = new User();
		user.setUserName(userName);
		user.setAccessToken(accessToken);
		
		String errorMsg = null;
		try {
			File excelFile = new File( uploadFile.getOriginalFilename());
			uploadFile.transferTo(excelFile);
			errorMsg = originalDataBll.importDeliveredOriginalData(excelFile, user);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (errorMsg == null || errorMsg.length() <= 0)
			return CommonJSONBuilder.getSuccResponseJSON();
		
		return CommonJSONBuilder.getResponseJSON(9, errorMsg);
	}
}
