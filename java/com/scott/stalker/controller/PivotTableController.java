package com.scott.stalker.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scott.stalker.bll.PivotTableBll;
import com.scott.stalker.bll.UserBll;
import com.scott.stalker.model.CustomerSpending;
import com.scott.stalker.model.GrossProfit;
import com.scott.stalker.model.Inventory;
import com.scott.stalker.model.ItemProfit;
import com.scott.stalker.model.User;
import com.scott.stalker.utils.CommonJSONBuilder;
import com.scott.stalker.utils.ObjectConversion;

@RestController
@RequestMapping(value = "/pivottalbe")
public class PivotTableController {
	
	@Autowired
	private PivotTableBll pivotTableBll;
	@Autowired
	private UserBll userBll;
	
	/**
	 * 127.0.0.1:8080/stalker/pivottalbe/itemProfit/ying/9e5bcbbc-405c-417a-89bd-045f25d66301?startDate=2016/01/01&endDate=2016/03/01&sortBy=sortByAverageProfit&KRW2CNY=0.005&USD2CNY=6.5&CAD2CNY=4.8
	 * @param userName
	 * @param accessToken
	 * @param startDate
	 * @param endDate
	 * @param sortBy
	 * @param KRW2CNY
	 * @param USD2CNY
	 * @param CAD2CNY
	 * @return
	 */
	@RequestMapping(value = "/itemProfit/{userName}/{accessToken}", method = RequestMethod.GET)
	public String itemProfit (@PathVariable("userName") String userName 
			, @PathVariable("accessToken") String accessToken
			, @RequestParam("startDate") String startDate
			, @RequestParam("endDate") String endDate
			, @RequestParam("sortBy") String sortBy
			, @RequestParam("KRW2CNY") String KRW2CNY
			, @RequestParam("USD2CNY") String USD2CNY
			, @RequestParam("CAD2CNY") String CAD2CNY) {
		// validate user
		User user = new User();
		user.setUserName(userName);
		user.setAccessToken(accessToken);
		if (userBll.validate(user) == null)
			return CommonJSONBuilder.getResponseJSON(3, "Invalid user or login has expired.");
		
		//package parameters
		ItemProfit itemProfitParams = new ItemProfit();
		try {
			itemProfitParams.setDeliveredStartDate(ObjectConversion.string2Date1(startDate));
			itemProfitParams.setDeliveredEndDate(ObjectConversion.addOneDay(ObjectConversion.string2Date1(endDate)));
		} catch (ParseException e) {
			return CommonJSONBuilder.getResponseJSON(3, "Date format incorrect (yyyy/mm/dd).");
		}
		
		itemProfitParams.setSortBy(sortBy);
		itemProfitParams.setKRW2CNY(new BigDecimal(KRW2CNY));
		itemProfitParams.setUSD2CNY(new BigDecimal(USD2CNY));
		itemProfitParams.setCAD2CNY(new BigDecimal(CAD2CNY));
		

		List<ItemProfit> itemProfits = pivotTableBll.itemProfit(itemProfitParams);
		if (itemProfits == null || itemProfits.size() <= 0) 
			return CommonJSONBuilder.getResponseJSON(5, "We did not find any brand/product.");
			
		GrossProfit grossProfit = pivotTableBll.grossProfit(itemProfits, itemProfitParams);
		
		return getJsonForItemProfit(grossProfit, itemProfits);
	}
	
	private String getJsonForItemProfit(GrossProfit grossProfit, List<ItemProfit> itemProfits) {
		JSONObject resultJsonObject = new JSONObject();
		
		JSONObject grossProfitObj = new JSONObject();
		// gross profit array
		grossProfitObj.put("turnOver", grossProfit.getTurnOver());
		grossProfitObj.put("turnOverCurrency", grossProfit.getTurnOverCurrency());
		grossProfitObj.put("purchasedTotal", grossProfit.getPurchasedTotal());
		grossProfitObj.put("purchasedTotalCurrency", grossProfit.getPurchasedTotalCurrency());
		grossProfitObj.put("grossProfit", grossProfit.getGrossProfit());
		resultJsonObject.put("grossProfit", grossProfitObj);

		// item profit array
		JSONArray itemProfitArray = new JSONArray();
		for (ItemProfit itemProfit : itemProfits) {
			JSONObject itemProfitObj = new JSONObject();
			itemProfitObj.put("brand", itemProfit.getBrand());
			itemProfitObj.put("product", itemProfit.getProduct());
			itemProfitObj.put("quantity", itemProfit.getQuantity());
			itemProfitObj.put("finalCurrency", itemProfit.getFinalCurrency());
			itemProfitObj.put("finalPriceAvg", itemProfit.getFinalPriceAvg());
			itemProfitObj.put("finalPriceMax", itemProfit.getFinalPriceMax());
			itemProfitObj.put("finalPriceMin", itemProfit.getFinalPriceMin());
			itemProfitObj.put("finalPriceSum", itemProfit.getFinalPriceSum());
			itemProfitObj.put("soldCurrency", itemProfit.getSoldCurrency());
			itemProfitObj.put("soldPriceAvg", itemProfit.getSoldPriceAvg());
			itemProfitObj.put("soldPriceMax", itemProfit.getSoldPriceMax());
			itemProfitObj.put("soldPriceMin", itemProfit.getSoldPriceMin());
			itemProfitObj.put("soldPriceSum", itemProfit.getSoldPriceSum());
			itemProfitObj.put("averageProfitCNY", itemProfit.getAverageProfitCNY());
			itemProfitArray.put(itemProfitObj);
		}
		resultJsonObject.put("itemProfits", itemProfitArray);
		
		return resultJsonObject.toString();
	}
	

	@RequestMapping(value = "/inventory/{userName}/{accessToken}", method = RequestMethod.GET)
	public String inventory (@PathVariable("userName") String userName, @PathVariable("accessToken") String accessToken) {
		//validate user
		User user = new User();
		user.setUserName(userName);
		user.setAccessToken(accessToken);
		if (userBll.validate(user) == null)
			return CommonJSONBuilder.getResponseJSON(3, "Invalid user or login has expired.");
		
		List<Inventory> inventories = pivotTableBll.inventory ();
		
		if (inventories == null || inventories.size() <= 0)
			return CommonJSONBuilder.getResponseJSON(5, "We did not find any brand/product.");
		
		return getJSONForInventory(inventories);
	}
	
	private String getJSONForInventory (List<Inventory> inventories) {
		JSONObject resultJsonObject = new JSONObject();
		JSONArray inventoryArray = new JSONArray();
		
		for (Inventory inventory : inventories) {
			JSONObject inventoryObj = new JSONObject();
			inventoryObj.put("brand", inventory.getBrand());
			inventoryObj.put("product", inventory.getProduct());
			inventoryObj.put("quantity", inventory.getQuantity());
			inventoryArray.put(inventoryObj);
		}
		
		resultJsonObject.put("inventories", inventoryArray);
		return resultJsonObject.toString();
	}
	
	/**
	 * 127.0.0.1:8080/stalker/pivottalbe/customerSpending/ying/7df5da7c-306f-45d9-924c-00f248e7c25e?startDate=2016/01/01&endDate=2016/06/06
	 * @param userName
	 * @param accessToken
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@RequestMapping(value = "/customerSpending/{userName}/{accessToken}", produces="text/html;charset=UTF-8", method = RequestMethod.GET)
	public String customerSpending (@PathVariable("userName") String userName 
			, @PathVariable("accessToken") String accessToken
			, @RequestParam("startDate") String startDate
			, @RequestParam("endDate") String endDate) {
		//validate user
		User user = new User();
		user.setUserName(userName);
		user.setAccessToken(accessToken);
		if (userBll.validate(user) == null)
			return CommonJSONBuilder.getResponseJSON(3, "Invalid user or login has expired.");
		
		CustomerSpending params = new CustomerSpending();
		
		try {
			params.setStartDate(ObjectConversion.string2Date1(startDate));
			params.setEndDate(ObjectConversion.addOneDay(ObjectConversion.string2Date1(endDate)));
		} catch (ParseException e) {
			return CommonJSONBuilder.getResponseJSON(3, "Date format incorrect (yyyy/mm/dd).");
		}
		
		List<CustomerSpending> customerSpendings = pivotTableBll.customerSpending(params);
		
		if (customerSpendings == null || customerSpendings.size() <= 0)
			return CommonJSONBuilder.getResponseJSON(5, "We did not find any customers.");
		
		return getCustomerSpendingJSON (customerSpendings);
	}
	
	private String getCustomerSpendingJSON (List<CustomerSpending> customerSpendings) {
		JSONObject resultJsonObject = new JSONObject();
		JSONArray customerArray = new JSONArray();
		
		for (CustomerSpending customerSpending : customerSpendings) {
			JSONObject customerObj = new JSONObject();
			customerObj.put("customerName", customerSpending.getCustomerName());
			customerObj.put("spending", customerSpending.getSpending());
			customerObj.put("spendingCurrency", customerSpending.getSpendingCurrency());
			customerArray.put(customerObj);
		}
		resultJsonObject.put("customerSpendings", customerArray);
		return resultJsonObject.toString();
	}
}
