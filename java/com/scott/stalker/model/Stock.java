package com.scott.stalker.model;

import java.math.BigDecimal;
import java.util.Date;

import com.scott.stalker.utils.ObjectConversion;

public class Stock {

	public static final String STATUS_STOCK_IN = "stockIn";
	public static final String STATUS_DELIVERY = "delivery";
	
	public static final String CURRENCY_CNY = "CNY";
	public static final String CURRENCY_USD = "USD";
	public static final String CURRENCY_KRW = "KRW";
	
	private Long stockId;
	
	private String brand;
	private String product;

	private BigDecimal originalPrice;
	private String originalCurrency;
	private BigDecimal finalPrice;
	private String finalCurrency;
	private Date stockInDate;
	private String purchasedMemo;

	private BigDecimal expectWholesalePrice;
	private String expectWholesaleCurrency;
	private BigDecimal expectRetailPrice;
	private String expectRetailCurrency;

	private Date soldDate;
	private BigDecimal soldPrice;
	private String soldCurrency;
	private String trackingInfo;
	private String deliveredMemo;
	private String soldBy;
	private String customerName;
	private Date deliveredUpdateTime;
	
	private String status; // stockIn, delivery
	
	private Date createOn;
	private String createBy;

	public String getBrand() {
		return brand;
	}
	public String getLowerCaseBrand () {
		if (getBrand() == null)
			return null;
		return  getBrand().toLowerCase();
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getProduct() {
		return product;
	}
	public String getLowerCaseProduct () {
		if (getProduct() == null)
			return null;
		return  getProduct().toLowerCase();
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getOriginalCurrency() {
		if (originalCurrency == null)
			return null;
		return originalCurrency.toUpperCase();
	}

	public void setOriginalCurrency(String originalCurrency) {
		this.originalCurrency = originalCurrency;
	}

	public BigDecimal getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(BigDecimal finalPrice) {
		this.finalPrice = finalPrice;
	}

	public String getFinalCurrency() {
		if (finalCurrency == null)
			return null;
		return finalCurrency.toUpperCase();
	}

	public void setFinalCurrency(String finalCurrency) {
		this.finalCurrency = finalCurrency;
	}

	public Date getStockInDate() {
		return stockInDate;
	}
	
	public String getStockInDateString () {
		return ObjectConversion.date2String(getStockInDate());
	}
	
	public long getStockInDateLong () {
		return ObjectConversion.date2Long(getStockInDate());
	}

	public void setStockInDate(Date stockInDate) {
		this.stockInDate = stockInDate;
	}

	public String getPurchasedMemo() {
		return purchasedMemo;
	}
	public String getPurchasedMemoEmp () {
		if (getPurchasedMemo() == null)
			return "";
		return getPurchasedMemo();
	}

	public void setPurchasedMemo(String purchasedMemo) {
		this.purchasedMemo = purchasedMemo;
	}

	public Date getSoldDate() {
		return soldDate;
	}
	
	public long getSoldDateLong () {
		if (getSoldDate() == null)
			return 0;
		return getSoldDate().getTime();
	}
	
	public String getSoldDateString () {
		return ObjectConversion.date2String(getSoldDate());
	}

	public void setSoldDate(Date soldDate) {
		this.soldDate = soldDate;
	}

	public BigDecimal getSoldPrice() {
		return soldPrice;
	}

	public void setSoldPrice(BigDecimal soldPrice) {
		this.soldPrice = soldPrice;
	}

	public String getSoldCurrency() {
		if (soldCurrency == null)
			return null;
		return soldCurrency.toUpperCase();
	}

	public void setSoldCurrency(String soldCurrency) {
		this.soldCurrency = soldCurrency;
	}

	public String getTrackingInfo() {
		return trackingInfo;
	}

	public void setTrackingInfo(String trackingInfo) {
		this.trackingInfo = trackingInfo;
	}

	public String getDeliveredMemo() {
		return deliveredMemo;
	}

	public void setDeliveredMemo(String deliveredMemo) {
		this.deliveredMemo = deliveredMemo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public Date getCreateOn() {
		return createOn;
	}

	public void setCreateOn(Date createOn) {
		this.createOn = createOn;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public BigDecimal getExpectWholesalePrice() {
		return expectWholesalePrice;
	}

	public void setExpectWholesalePrice(BigDecimal expectWholesalePrice) {
		this.expectWholesalePrice = expectWholesalePrice;
	}

	public String getExpectWholesaleCurrency() {
		if (expectWholesaleCurrency == null)
			return null;
		return expectWholesaleCurrency.toUpperCase();
	}

	public void setExpectWholesaleCurrency(String expectWholesaleCurrency) {
		this.expectWholesaleCurrency = expectWholesaleCurrency;
	}

	public BigDecimal getExpectRetailPrice() {
		return expectRetailPrice;
	}

	public void setExpectRetailPrice(BigDecimal expectRetailPrice) {
		this.expectRetailPrice = expectRetailPrice;
	}

	public String getExpectRetailCurrency() {
		if (expectRetailCurrency == null)
			return null;
		return expectRetailCurrency.toUpperCase();
	}

	public void setExpectRetailCurrency(String expectRetailCurrency) {
		this.expectRetailCurrency = expectRetailCurrency;
	}

	public String getSoldBy() {
		return soldBy;
	}

	public void setSoldBy(String soldBy) {
		this.soldBy = soldBy;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Date getDeliveredUpdateTime() {
		return deliveredUpdateTime;
	}
	public void setDeliveredUpdateTime(Date deliveredUpdateTime) {
		this.deliveredUpdateTime = deliveredUpdateTime;
	}
	
	public boolean validateBrandNProduct () {
		if (getBrand() == null || getProduct() == null || getBrand().length() <= 0 || getProduct().length() <= 0)
			return false;
		return true;
	}
}
