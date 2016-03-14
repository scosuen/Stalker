package com.scott.stalker.model;

import java.math.BigDecimal;
import java.util.Date;

import com.scott.stalker.utils.CurrencyUtils;

public class ItemProfit {
	
	public static final String SORT_TYPE_QUANTITY = "sortByQuantity";
	public static final String SORT_TYPE_FINAL_PRICE = "sortByFinalPrice";
	public static final String SORT_TYPE_SOLD_PRICE = "sortBySoldPrice";
	public static final String SORT_TYPE_AVERAGE_PROFIT = "sortByAverageProfit";
	
	private Date deliveredStartDate;
	private Date deliveredEndDate;
	private String sortBy;
	
	private BigDecimal KRW2CNY;
	private BigDecimal USD2CNY;
	private BigDecimal CAD2CNY;
	
	private String brand;
	private String product;
	private Long quantity;
	private BigDecimal finalPriceMin;
	private BigDecimal finalPriceAvg;
	private BigDecimal finalPriceMax;
	private BigDecimal finalPriceSum;
	private String finalCurrency;
	private BigDecimal soldPriceMin;
	private BigDecimal soldPriceAvg;
	private BigDecimal soldPriceMax;
	private BigDecimal soldPriceSum;
	private String soldCurrency;
	
	private BigDecimal averageProfitCNY;
	
	public BigDecimal getCNYRateByCurrency (String currency) {
		if (currency == null)
			return null;
		
		if (CurrencyUtils.CURRENCY_CAD.equals(currency.toUpperCase())) {
			return getCAD2CNY();
		} else if (CurrencyUtils.CURRENCY_USD.equals(currency.toUpperCase())) {
			return getUSD2CNY();
		} else if (CurrencyUtils.CURRENCY_KRW.equals(currency.toUpperCase())) {
			return getKRW2CNY();
		} else if (CurrencyUtils.CURRENCY_CNY.equals(currency.toUpperCase())) {
			return new BigDecimal(1);
		}
		
		return null;
	}
	
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public BigDecimal getFinalPriceMin() {
		return finalPriceMin;
	}

	public void setFinalPriceMin(BigDecimal finalPriceMin) {
		this.finalPriceMin = finalPriceMin;
	}

	public BigDecimal getFinalPriceAvg() {
		return finalPriceAvg;
	}

	public void setFinalPriceAvg(BigDecimal finalPriceAvg) {
		this.finalPriceAvg = finalPriceAvg;
	}

	public BigDecimal getFinalPriceMax() {
		return finalPriceMax;
	}

	public void setFinalPriceMax(BigDecimal finalPriceMax) {
		this.finalPriceMax = finalPriceMax;
	}

	public BigDecimal getSoldPriceMin() {
		return soldPriceMin;
	}

	public void setSoldPriceMin(BigDecimal soldPriceMin) {
		this.soldPriceMin = soldPriceMin;
	}

	public BigDecimal getSoldPriceAvg() {
		return soldPriceAvg;
	}

	public void setSoldPriceAvg(BigDecimal soldPriceAvg) {
		this.soldPriceAvg = soldPriceAvg;
	}

	public BigDecimal getSoldPriceMax() {
		return soldPriceMax;
	}

	public void setSoldPriceMax(BigDecimal soldPriceMax) {
		this.soldPriceMax = soldPriceMax;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Date getDeliveredStartDate() {
		return deliveredStartDate;
	}
	public Long getDeliveredStartDateLong() {
		if (getDeliveredStartDate() == null)
			return null;
		return getDeliveredStartDate().getTime();
	}

	public void setDeliveredStartDate(Date deliveredStartDate) {
		this.deliveredStartDate = deliveredStartDate;
	}

	public Date getDeliveredEndDate() {
		return deliveredEndDate;
	}
	public Long getDeliveredEndDateLong() {
		if (getDeliveredEndDate() == null)
			return null;
		return getDeliveredEndDate().getTime();
	}

	public void setDeliveredEndDate(Date deliveredEndDate) {
		this.deliveredEndDate = deliveredEndDate;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public BigDecimal getKRW2CNY() {
		return KRW2CNY;
	}

	public void setKRW2CNY(BigDecimal kRW2CNY) {
		KRW2CNY = kRW2CNY;
	}

	public BigDecimal getUSD2CNY() {
		return USD2CNY;
	}

	public String getFinalCurrency() {
		if (finalCurrency == null)
			return null;
		return finalCurrency.toUpperCase();
	}

	public void setFinalCurrency(String finalCurrency) {
		this.finalCurrency = finalCurrency;
	}

	public String getSoldCurrency() {
		if (soldCurrency == null)
			return null;
		return soldCurrency.toUpperCase();
	}

	public void setSoldCurrency(String soldCurrency) {
		this.soldCurrency = soldCurrency;
	}

	public void setUSD2CNY(BigDecimal uSD2CNY) {
		USD2CNY = uSD2CNY;
	}

	public BigDecimal getCAD2CNY() {
		return CAD2CNY;
	}

	public void setCAD2CNY(BigDecimal cAD2CNY) {
		CAD2CNY = cAD2CNY;
	}

	public BigDecimal getFinalPriceSum() {
		return finalPriceSum;
	}

	public void setFinalPriceSum(BigDecimal finalPriceSum) {
		this.finalPriceSum = finalPriceSum;
	}

	public BigDecimal getSoldPriceSum() {
		return soldPriceSum;
	}

	public void setSoldPriceSum(BigDecimal soldPriceSum) {
		this.soldPriceSum = soldPriceSum;
	}

	public BigDecimal getAverageProfitCNY() {
		return averageProfitCNY;
	}

	public void setAverageProfitCNY(BigDecimal averageProfitCNY) {
		this.averageProfitCNY = averageProfitCNY;
	}

	

	public static void main(String[] args) {
		BigDecimal KRW2CNY = new BigDecimal(0.005);
		
		ItemProfit i = new ItemProfit();
	}
}
