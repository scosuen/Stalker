package com.scott.stalker.model;

import java.math.BigDecimal;

public class GrossProfit {

	private BigDecimal turnOver;
	private String turnOverCurrency;
	private BigDecimal purchasedTotal;
	private String purchasedTotalCurrency;
	private BigDecimal grossProfit;

	public BigDecimal getTurnOver() {
		return turnOver;
	}

	public void setTurnOver(BigDecimal turnOver) {
		this.turnOver = turnOver;
	}

	public String getTurnOverCurrency() {
		return turnOverCurrency;
	}

	public void setTurnOverCurrency(String turnOverCurrency) {
		this.turnOverCurrency = turnOverCurrency;
	}

	public BigDecimal getPurchasedTotal() {
		return purchasedTotal;
	}

	public void setPurchasedTotal(BigDecimal purchasedTotal) {
		this.purchasedTotal = purchasedTotal;
	}

	public String getPurchasedTotalCurrency() {
		return purchasedTotalCurrency;
	}

	public void setPurchasedTotalCurrency(String purchasedTotalCurrency) {
		this.purchasedTotalCurrency = purchasedTotalCurrency;
	}

	public BigDecimal getGrossProfit() {
		return grossProfit;
	}

	public void setGrossProfit(BigDecimal grossProfit) {
		this.grossProfit = grossProfit;
	}
	
	

}
