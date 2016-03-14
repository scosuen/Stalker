package com.scott.stalker.bll;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.scott.stalker.dao.PivotTableDao;
import com.scott.stalker.model.CustomerSpending;
import com.scott.stalker.model.GrossProfit;
import com.scott.stalker.model.Inventory;
import com.scott.stalker.model.ItemProfit;
import com.scott.stalker.utils.CurrencyUtils;

@Component
public class PivotTableBll {
	@Autowired
	private PivotTableDao pivotTableDao;

	public List<ItemProfit> itemProfit(ItemProfit itemProfitParams) {
		
		List<ItemProfit> itemProfits = pivotTableDao.itemProfit(itemProfitParams);
		if (itemProfits == null || itemProfits.size() <= 0)
			return null;
		
		CurrencyUtils currencyUtils = new CurrencyUtils(itemProfitParams.getKRW2CNY(), itemProfitParams.getUSD2CNY(), itemProfitParams.getCAD2CNY());
		
		//calculate average profit
		for (ItemProfit itemProfit : itemProfits) {
			BigDecimal soldPriceSettCur = currencyUtils.calcPriceByRate(itemProfit.getSoldPriceAvg(), 
					itemProfit.getSoldCurrency(), CurrencyUtils.CURRENCY_CNY);
			BigDecimal finalPriceSettCur = currencyUtils.calcPriceByRate(itemProfit.getFinalPriceAvg(), 
					itemProfit.getFinalCurrency(), CurrencyUtils.CURRENCY_CNY);
			
			itemProfit.setAverageProfitCNY(soldPriceSettCur.subtract(finalPriceSettCur).setScale(2, BigDecimal.ROUND_HALF_UP));
		}
		
		//order
		if (ItemProfit.SORT_TYPE_AVERAGE_PROFIT.equals(itemProfitParams.getSortBy())) {
			Collections.sort(itemProfits, new ItemProfitSortByAvgProfitDesc());
		}
		
		return itemProfits;
	}
	
	private class ItemProfitSortByAvgProfitDesc implements Comparator<ItemProfit> {
		@Override
		public int compare(ItemProfit o1, ItemProfit o2) {
			return o2.getAverageProfitCNY().compareTo(o1.getAverageProfitCNY());
		}
		
	}

	public GrossProfit grossProfit(List<ItemProfit> result, ItemProfit itemProfitParams) {
		GrossProfit grossProfit = new GrossProfit();
		
		BigDecimal turnOver = new BigDecimal("0");
		String turnOverCurrency = null;
		BigDecimal purchasedTotal = new BigDecimal("0");
		String purchasedTotalCurrency = null;
		CurrencyUtils currencyUtils = new CurrencyUtils(itemProfitParams.getKRW2CNY(), itemProfitParams.getUSD2CNY(), itemProfitParams.getCAD2CNY());

		for (ItemProfit itemProfit : result) {
			turnOver = turnOver.add(itemProfit.getSoldPriceSum());
			purchasedTotal = purchasedTotal.add(itemProfit.getFinalPriceSum());
			turnOverCurrency = itemProfit.getSoldCurrency();
			purchasedTotalCurrency = itemProfit.getFinalCurrency();
		}
		grossProfit.setTurnOver(turnOver);
		grossProfit.setTurnOverCurrency(turnOverCurrency);
		grossProfit.setPurchasedTotal(purchasedTotal);
		grossProfit.setPurchasedTotalCurrency(purchasedTotalCurrency);
		
		BigDecimal returnOverCNY = currencyUtils.calcPriceByRate(turnOver, turnOverCurrency, CurrencyUtils.CURRENCY_CNY);
		BigDecimal purchasedTotalCNY = currencyUtils.calcPriceByRate(purchasedTotal, purchasedTotalCurrency, CurrencyUtils.CURRENCY_CNY);
		
		grossProfit.setGrossProfit(returnOverCNY.subtract(purchasedTotalCNY));
		
		return grossProfit;
	}

	public List<Inventory> inventory() {
		return pivotTableDao.inventory();
	}

	public List<CustomerSpending> customerSpending(CustomerSpending params) {
		
		
		return pivotTableDao.customerSpending(params);
	}

}
