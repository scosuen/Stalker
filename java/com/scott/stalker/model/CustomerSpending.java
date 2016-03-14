package com.scott.stalker.model;

import java.math.BigDecimal;
import java.util.Date;

public class CustomerSpending {
	
	private Date startDate;
	private Date endDate;
	
	private String customerName;
	private BigDecimal spending;
	private String spendingCurrency;
	
	public String getSpendingCurrency() {
		return spendingCurrency;
	}
	public void setSpendingCurrency(String spendingCurrency) {
		this.spendingCurrency = spendingCurrency;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public BigDecimal getSpending() {
		return spending;
	}
	public void setSpending(BigDecimal spending) {
		this.spending = spending;
	}

}
