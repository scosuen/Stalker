package com.scott.stalker.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CurrencyUtils {
	
	public static final String CURRENCY_KRW = "KRW";
	public static final String CURRENCY_USD = "USD";
	public static final String CURRENCY_CAD = "CAD";
	public static final String CURRENCY_CNY = "CNY";
	
	private BigDecimal rateKRW2CNY;
	private BigDecimal rateUSD2CNY;
	private BigDecimal rateCAD2CNY;
	
	private BigDecimal rateKRW2CAD;
	private BigDecimal rateUSD2CAD;
	private BigDecimal rateCNY2CAD;
	
	
	private Map<String, HashMap<String, BigDecimal>> currencyRateMap = 
			new HashMap<String, HashMap<String, BigDecimal>>();
	
	public CurrencyUtils (BigDecimal rateKRW2CNY, BigDecimal rateUSD2CNY, BigDecimal rateCAD2CNY) {
		super();
		
		this.rateKRW2CNY = rateKRW2CNY;
		this.rateUSD2CNY = rateUSD2CNY;
		this.rateCAD2CNY = rateCAD2CNY;
		initCurrencyMap();
	}
	
	private void initCurrencyMap() {
		//CNY
		HashMap<String, BigDecimal> rateMapateMap = new HashMap<String, BigDecimal>();
		rateMapateMap.put(CURRENCY_KRW, rateKRW2CNY);
		rateMapateMap.put(CURRENCY_USD, rateUSD2CNY);
		rateMapateMap.put(CURRENCY_CAD, rateCAD2CNY);
		rateMapateMap.put(CURRENCY_CNY, new BigDecimal("1"));
		currencyRateMap.put(CURRENCY_CNY, rateMapateMap);
		
		//CAD
		rateMapateMap = new HashMap<String, BigDecimal>();
		rateMapateMap.put(CURRENCY_KRW, rateKRW2CAD);
		rateMapateMap.put(CURRENCY_USD, rateUSD2CAD);
		rateMapateMap.put(CURRENCY_CNY, rateCNY2CAD);
		rateMapateMap.put(CURRENCY_CAD, new BigDecimal("1"));
		currencyRateMap.put(CURRENCY_CAD, rateMapateMap);
	};
	
	public BigDecimal exchangeRateConverter(BigDecimal price, BigDecimal rate) {
		if (price == null || rate == null || rate.equals(0))
			return null;
		return price.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	
	public BigDecimal calcPriceByRate(BigDecimal price, String originalCurrency, String settlementCurrency) {
		if (price == null || originalCurrency == null || settlementCurrency == null || originalCurrency.length() <=0
				|| settlementCurrency.length() <= 0)
			return null;
		
		if (currencyRateMap.get(settlementCurrency) == null)
			return null;
		
		return exchangeRateConverter(price, currencyRateMap.get(settlementCurrency).get(originalCurrency));	
	}
	
	public static void main(String[] args) {
		CurrencyUtils currencyUtils = new CurrencyUtils(new BigDecimal(0.005), new BigDecimal(6.5), new BigDecimal(4.8));
		
		System.out.println(currencyUtils.calcPriceByRate(new BigDecimal(10000), CURRENCY_USD, CURRENCY_CNY));
		
	}

	public BigDecimal getRateKRW2CNY() {
		return rateKRW2CNY;
	}

	public void setRateKRW2CNY(BigDecimal rateKRW2CNY) {
		this.rateKRW2CNY = rateKRW2CNY;
	}

	public BigDecimal getRateUSD2CNY() {
		return rateUSD2CNY;
	}

	public void setRateUSD2CNY(BigDecimal rateUSD2CNY) {
		this.rateUSD2CNY = rateUSD2CNY;
	}

	public BigDecimal getRateCAD2CNY() {
		return rateCAD2CNY;
	}

	public void setRateCAD2CNY(BigDecimal rateCAD2CNY) {
		this.rateCAD2CNY = rateCAD2CNY;
	}

	public BigDecimal getRateKRW2CAD() {
		return rateKRW2CAD;
	}

	public void setRateKRW2CAD(BigDecimal rateKRW2CAD) {
		this.rateKRW2CAD = rateKRW2CAD;
	}

	public BigDecimal getRateUSD2CAD() {
		return rateUSD2CAD;
	}

	public void setRateUSD2CAD(BigDecimal rateUSD2CAD) {
		this.rateUSD2CAD = rateUSD2CAD;
	}

	public BigDecimal getRateCNY2CAD() {
		return rateCNY2CAD;
	}

	public void setRateCNY2CAD(BigDecimal rateCNY2CAD) {
		this.rateCNY2CAD = rateCNY2CAD;
	}
	
//	public BigDecimal calcPrice 
}

