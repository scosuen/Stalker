package com.scott.stalker.bll;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.format.CellFormatType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.scott.stalker.dao.OriginalDataDao;
import com.scott.stalker.model.Stock;
import com.scott.stalker.model.User;

@Component
public class OriginalDataBll {

	@Autowired
	private OriginalDataDao originalDataDao;
	
	@Autowired
	private UserBll userBll;
	
	
	@Autowired
	private PlatformTransactionManager transactionManager;

	/**
	 * Purchased
	 * @param excelFile
	 * @param user
	 * @return
	 */
	@Transactional
	public String importPurchasedOriginalData(File excelFile, User user) {
		//validate user
		if (userBll.validate(user) == null)
			return "Invalid user or login has expired.";
		
		// get work book
		Workbook workbook = null;
		List<Stock> stockList = new ArrayList<Stock>();
		try {
			workbook = new XSSFWorkbook(new FileInputStream(excelFile));
		} catch (Exception ex) {
			try {
				workbook = new HSSFWorkbook(new FileInputStream(excelFile));
			} catch (Exception e) {
				return excelFile.getName() + " is not a Excel file.";
			}
		}

		try {
			// get first sheet
			Sheet sheet = workbook.getSheetAt(0);

			for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
				Row row = sheet.getRow(rowNum);
				if (row == null)
					continue;
				Long quantitiy = (long) row.getCell(7).getNumericCellValue();

				if (quantitiy <= 0)
					return "line:" + rowNum + " quantity is not currect.";

				for (int item = 0; item < quantitiy; item++) {
					Stock stock = new Stock();
					try {
						stock.setBrand(row.getCell(1).getStringCellValue().trim());
						stock.setProduct(row.getCell(2).getStringCellValue().trim());
						stock.setOriginalPrice(new BigDecimal(row.getCell(3).getNumericCellValue()));
						stock.setOriginalCurrency(row.getCell(4).getStringCellValue().trim());
						stock.setFinalPrice(new BigDecimal(row.getCell(5).getNumericCellValue()));
						stock.setFinalCurrency(row.getCell(6).getStringCellValue().trim());
						stock.setStockInDate(row.getCell(0).getDateCellValue());
						stock.setPurchasedMemo(getNullableCellStringValue(row.getCell(8)));
						stock.setExpectWholesalePrice(getNullableCellBigDValue(row.getCell(9)));
						stock.setExpectWholesaleCurrency(getNullableCellStringValue(row.getCell(10)));
						stock.setExpectRetailPrice(getNullableCellBigDValue(row.getCell(11)));
						stock.setExpectRetailCurrency(getNullableCellStringValue(row.getCell(12)));
						stock.setCreateBy(user.getUserName());
					} catch (Exception ex) {
						return "line:" + row.getRowNum() +  ". Data error.";
					}
					stockList.add(stock);
				}
			}
		} catch (Exception ex) {
			return "Data error.";
		}

		//insert stock
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = transactionManager.getTransaction(txDef);
		try {
			originalDataDao.insertStock(stockList, user);
			transactionManager.commit(txStatus);
		} catch (Exception e) {
			transactionManager.rollback(txStatus);
			return "Database exception.";
		}

		return null;
	}

	
	/**
	 * Delivered
	 * @param excelFile
	 * @param user
	 * @return
	 */
	@Transactional
	public String importDeliveredOriginalData(File excelFile, User user) {
		//validate user
		if (userBll.validate(user) == null)
			return "Invalid user or login has expired.";
		
		//get work book
		Workbook workbook = null;
		List<Stock> deliveredList = new ArrayList<Stock>();
		try {
			workbook = new XSSFWorkbook(new FileInputStream(excelFile));
		} catch (Exception ex) {
			try {
				workbook = new HSSFWorkbook(new FileInputStream(excelFile));
			} catch (Exception e) {
				return excelFile.getName() + " is not a Excel file.";
			}
		}
		
		try {
			// get first sheet
			Sheet sheet = workbook.getSheetAt(0);

			for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
				Row row = sheet.getRow(rowNum);
				if (row == null)
					continue;
				
				try {
					//validate brand and product
					Stock brandNProduct = new Stock();
					brandNProduct.setBrand(row.getCell(1).getStringCellValue().trim());
					brandNProduct.setProduct(row.getCell(2).getStringCellValue().trim());
					if (originalDataDao.queryBrandProductCount(brandNProduct) <= 0) 
						return "Brand:" + brandNProduct.getBrand() + " Product:" + brandNProduct.getProduct() + " is not in stock";
					
					//insert customer
					String customerName = row.getCell(3).getStringCellValue();
					
	
					//validate quantity
					Long quantitiy = (long) row.getCell(6).getNumericCellValue();
					if (quantitiy <= 0)
						return "line:" + rowNum + " quantity is not currect.";
	
					for (int item = 0; item < quantitiy; item++) {
						Stock stock = new Stock();
						
						stock.setSoldDate(row.getCell(0).getDateCellValue());
						stock.setBrand(row.getCell(1).getStringCellValue().trim());
						stock.setProduct(row.getCell(2).getStringCellValue().trim());
						stock.setCustomerName(row.getCell(3).getStringCellValue().trim());
						stock.setSoldPrice(new BigDecimal(row.getCell(4).getNumericCellValue()));
						stock.setSoldCurrency(row.getCell(5).getStringCellValue().trim());
						stock.setTrackingInfo(getNullableCellStringValue(row.getCell(7)));
						stock.setDeliveredMemo(getNullableCellStringValue(row.getCell(8)));
						stock.setSoldBy(user.getUserName());
						stock.setStatus(Stock.STATUS_DELIVERY);
						
						deliveredList.add(stock);
					}
				} catch (Exception ex) {
					return "Row:" + row.getRowNum() + ". Data error.";
				}
			}
		} catch (Exception ex) {
			return "Data error.";
		}
		
		//insert delivered
		//transaction
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = transactionManager.getTransaction(txDef);
		try {
			originalDataDao.insertDelivered (deliveredList, user);
			transactionManager.commit(txStatus);
		} catch (Exception e) {
			transactionManager.rollback(txStatus);
			return "Database exception.";
		}
		
		return null;
	}
	
	public String getNullableCellStringValue (Cell cell) {
		if (cell == null)
			return null;
		return cell.getStringCellValue().trim();
	}
	
	public BigDecimal getNullableCellBigDValue (Cell cell) {
		if (cell == null)
			return null;
		return new BigDecimal(cell.getNumericCellValue());
	}
}
