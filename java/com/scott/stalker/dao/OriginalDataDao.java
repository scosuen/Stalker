package com.scott.stalker.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.scott.stalker.model.Stock;
import com.scott.stalker.model.User;

@Repository
public class OriginalDataDao {

	private static Logger logger = LoggerFactory.getLogger(OriginalDataDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void insertStock(List<Stock> stockList, User user) {
		if (stockList == null || stockList.size() <=0)
			return;
		
		StringBuffer sql = new StringBuffer();
		sql.append(" MERGE (seq:sequence {name:'stockSequence'}) ");
		sql.append(" ON CREATE SET seq.current = 1 ");
		sql.append(" ON MATCH SET seq.current = seq.current + 1 ");
		sql.append(" REMOVE seq.lock ");
		sql.append(" WITH seq.current as current ");
		sql.append(" match (u:user) ");
		sql.append(" where lower(u.userName) = '" + user.getLowercaseUserName() + "' ");
		sql.append(" create (u) -[:purchased]-> (s:stock {stockId:current, brand:?, product:?, ");//1, 2
		sql.append(" originalPrice:?, originalCurrency:?, "); //3, 4
		sql.append(" finalPrice:?, finalCurrency:?, "); //5, 6
		sql.append(" stockInDate:?, purchasedUploadTime:timestamp(),");  //7
		sql.append(" purchasedMemo:?, "); //8
		sql.append(" expectWholesalePrice:?, expectWholesaleCurrency:?, "); //9, 10
		sql.append(" expectRetailPrice:?, expectRetailCurrency:?, "); //11, 12
		sql.append(" soldDate:-1, deliveredUpdateTime:-1, "); 
		sql.append(" soldPrice:-1, soldCurrency:'', ");
		sql.append(" trackingInfo:'', deliveredMemo:'', status:'stockIn' ");
		sql.append(" }) return s");
		
		logger.debug("insertStock sql: " + sql);
		
		jdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {

			@Override
			public int getBatchSize() {
				return stockList.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Stock stock = stockList.get(i);
				int index = 1;
				ps.setString(index++, stock.getBrand());
				ps.setString(index++, stock.getProduct());
				ps.setBigDecimal(index++, stock.getOriginalPrice());
				ps.setString(index++, stock.getOriginalCurrency());
				ps.setBigDecimal(index++, stock.getFinalPrice());
				ps.setString(index++, stock.getFinalCurrency());
				ps.setLong(index++, stock.getStockInDateLong());
				ps.setString(index++, stock.getPurchasedMemoEmp());
				ps.setBigDecimal(index++, stock.getExpectWholesalePrice());
				ps.setString(index++, stock.getExpectWholesaleCurrency());
				ps.setBigDecimal(index++, stock.getExpectRetailPrice());
				ps.setString(index++, stock.getExpectRetailCurrency());
			}
			
		});
	}

	/**
	 * delivered
	 * @param deliveredList
	 * @param user
	 * @throws Exception 
	 */
	public void insertDelivered(List<Stock> deliveredList, User user) {
		if (deliveredList == null || deliveredList.size() <=0)
			return;
		
		for (Stock stock : deliveredList) {
			StringBuffer sql = new StringBuffer();
			sql.append(" match (s:stock) where s.status = 'stockIn' and lower(s.brand) = '" + stock.getLowerCaseBrand() + "' and lower(s.product) = '" + stock.getLowerCaseProduct() + "' ");
			sql.append(" with s.stockId as lastestStockId order by s.stockInDate limit 1  ");
			sql.append(" match (s2:stock {stockId:lastestStockId}), (u:user {userName:'" + user.getUserName() + "'})  ");
			sql.append(" set s2.status = 'delivery', s2.soldDate = " + stock.getSoldDateLong() + ",  ");
			sql.append(" s2.soldPrice = " + stock.getSoldPrice() + ", s2.soldCurrency = '" + stock.getSoldCurrency() + "',  ");
			sql.append(" s2.trackingInfo = '" + stock.getTrackingInfo() + "', s2.deliveredMemo = '" + stock.getDeliveredMemo() + "', ");
			sql.append(" s2.deliveredUpdateTime = timestamp() ");
			sql.append(" merge (u) -[:delivered]-> (s2) ");
			sql.append(" with s2.stockId as lastestStockId2 ");
			sql.append(" merge (c:customer {customerName:'" + stock.getCustomerName() + "'}) with c.customerName as customerName1, lastestStockId2 ");
			sql.append(" match (s3:stock {stockId:lastestStockId2}), (c2:customer {customerName:customerName1})  ");
			sql.append(" create (s3) -[:buy]-> (c2) ");
			
			logger.debug(" insertDelivered sql:" + sql);
			
			jdbcTemplate.update(sql.toString());
		}
	}
	
	/**
	 * 
	 * @param stock
	 * @return
	 */
	public long queryBrandProductCount (Stock stock) {
		
		if (stock.validateBrandNProduct() == false)
			return -1;
		
		StringBuffer sql = new StringBuffer();
		sql.append(" match (s:stock) ");
		sql.append(" where lower(s.brand) = '" + stock.getLowerCaseBrand() + "' and lower(s.product) = '" + stock.getLowerCaseProduct() + "' and s.status = 'stockIn' ");
		sql.append(" return count(*) ");
		
		logger.debug(" queryBrandProductCount sql:" + sql);
		List<Long> longs  = jdbcTemplate.queryForList(sql.toString(), Long.class);
		
		return longs.get(0);
	}
}
