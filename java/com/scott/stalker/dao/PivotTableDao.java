package com.scott.stalker.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.scott.stalker.model.CustomerSpending;
import com.scott.stalker.model.Inventory;
import com.scott.stalker.model.ItemProfit;

@Repository
public class PivotTableDao {
	private static Logger logger = LoggerFactory.getLogger(PivotTableDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<ItemProfit> itemProfit(ItemProfit itemProfitParams) {
		StringBuffer sql = new StringBuffer();
		sql.append(" match (s:stock)  ");
		sql.append(" where s.status='delivery' ");
		sql.append(" and s.soldDate >= " + itemProfitParams.getDeliveredStartDateLong() + " and s.soldDate < " + itemProfitParams.getDeliveredEndDateLong());
		sql.append(" return DISTINCT s.brand as brand, s.product as product, s.finalCurrency as finalCurrency, s.soldCurrency as soldCurrency,  ");
		sql.append(" count(s.finalPrice) as quanatity,  ");
		sql.append(" avg(s.soldPrice) as avgSoldPrice, min(s.soldPrice) as minSoldPrice, max(s.soldPrice) as maxSoldPrice, sum(s.soldPrice) as sumSoldPrice, ");
		sql.append(" avg(s.finalPrice) as avgFinalPrice, min(s.finalPrice) as minFinalPrice, max(s.finalPrice) as maxFinalPrice, sum(s.finalPrice) as sumFinalPrice ");
		sql.append(" order by quanatity desc, avgSoldPrice desc, avgFinalPrice desc ");
		
		logger.debug(" itemProfit sql:" + sql);
		
		return jdbcTemplate.query(sql.toString(), new itemProfitMapper());
	}
	
	public List<Inventory> inventory() {
		StringBuffer sql = new StringBuffer();
		sql.append(" match (s:stock {status:'stockIn'}) return distinct s.brand as brand, s.product as product, count(*) as quantity");
		logger.debug(" inventory sql:" + sql);
		
		return jdbcTemplate.query(sql.toString(), new InventoryMapper());
	}
	
	public List<CustomerSpending> customerSpending(CustomerSpending params) {
		StringBuffer sql = new StringBuffer();
		sql.append(" match (s:stock) -[:buy]-> (c:customer)  ");
		sql.append(" where s.soldDate >= " + params.getStartDate().getTime() + " and s.soldDate < " + params.getEndDate().getTime());
		sql.append(" return distinct c.customerName as customerName, s.soldCurrency as soldCurrency, sum(s.soldPrice) as spending ");
		logger.debug(" customerSpending sql:" + sql);
		
		return jdbcTemplate.query(sql.toString(), new CustomerSpendingMapper());
	}
	
	
	private class itemProfitMapper implements RowMapper<ItemProfit> {

		@Override
		public ItemProfit mapRow(ResultSet rs, int rowNum) throws SQLException {
			ItemProfit itemProfit = new ItemProfit();
			itemProfit.setBrand(rs.getString("brand"));
			itemProfit.setProduct(rs.getString("product"));
			itemProfit.setQuantity(rs.getLong("quanatity"));
			itemProfit.setFinalCurrency(rs.getString("finalCurrency"));
			itemProfit.setFinalPriceAvg(rs.getBigDecimal("avgFinalPrice"));
			itemProfit.setFinalPriceMax(rs.getBigDecimal("maxFinalPrice"));
			itemProfit.setFinalPriceMin(rs.getBigDecimal("minFinalPrice"));
			itemProfit.setFinalPriceSum(rs.getBigDecimal("sumFinalPrice"));
			itemProfit.setSoldCurrency(rs.getString("soldCurrency"));
			itemProfit.setSoldPriceAvg(rs.getBigDecimal("avgSoldPrice"));
			itemProfit.setSoldPriceMax(rs.getBigDecimal("maxSoldPrice"));
			itemProfit.setSoldPriceMin(rs.getBigDecimal("minSoldPrice"));
			itemProfit.setSoldPriceSum(rs.getBigDecimal("sumSoldPrice"));
			
			return itemProfit;
		}

	}
	
	private class InventoryMapper implements RowMapper<Inventory> {
		@Override
		public Inventory mapRow(ResultSet rs, int rowNum) throws SQLException {
			Inventory inventory = new Inventory();
			inventory.setBrand(rs.getString("brand"));
			inventory.setProduct(rs.getString("product"));
			inventory.setQuantity(rs.getLong("quantity"));
			return inventory;
		}
	}
	
	private class CustomerSpendingMapper implements RowMapper<CustomerSpending> {
		@Override
		public CustomerSpending mapRow(ResultSet rs, int rowNum) throws SQLException {
			CustomerSpending customerSpending = new CustomerSpending();
			customerSpending.setCustomerName(rs.getString("customerName"));
			customerSpending.setSpending(rs.getBigDecimal("spending"));
			customerSpending.setSpendingCurrency(rs.getString("soldCurrency"));
			
			return customerSpending;
		}
	}

}
