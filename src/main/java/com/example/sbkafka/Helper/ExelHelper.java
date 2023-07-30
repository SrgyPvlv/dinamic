package com.example.sbkafka.Helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.sbkafka.Model.OrderForm;

public class ExelHelper {
	  public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	  static String[] HEADERs = { "№ Заказа", "№ БС", "Дата начала", "Дата окончания", "Стоимость без НДС", "Стоимость с НДС", "Комментарий"};
	  static String SHEET = "Заказы";

	  public static ByteArrayInputStream orderFormToExcel(List<OrderForm> orderlistFile) {

	    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
	      Sheet sheet = workbook.createSheet(SHEET);

	      // Header
	      Row headerRow = sheet.createRow(0);

	      for (int col = 0; col < HEADERs.length; col++) {
	        Cell cell = headerRow.createCell(col);
	        cell.setCellValue(HEADERs[col]);
	      }

	      int rowIdx = 1;
	      for (OrderForm order : orderlistFile) {
	        Row row = sheet.createRow(rowIdx++);

	        try {row.createCell(0).setCellValue(order.getOrdernumber());} catch(Exception ex) {row.createCell(0).setCellValue("");};
	        try {row.createCell(1).setCellValue(order.getBsnumber());} catch(Exception ex) {row.createCell(1).setCellValue("");};
	        try {row.createCell(2).setCellValue(order.getDatestart());} catch(Exception ex) {row.createCell(2).setCellValue("");};
	        try {row.createCell(3).setCellValue(order.getDateend());} catch(Exception ex) {row.createCell(3).setCellValue("");};
	        try {row.createCell(4).setCellValue(order.getCalc());} catch(Exception ex) {row.createCell(4).setCellValue("");};
	        try {row.createCell(5).setCellValue(order.getCalcnds());} catch(Exception ex) {row.createCell(5).setCellValue("");};
	        try {row.createCell(6).setCellValue(order.getComm());} catch(Exception ex) {row.createCell(6).setCellValue("");};
	      }

	      workbook.write(out);
	      return new ByteArrayInputStream(out.toByteArray());
	    } catch (IOException e) {
	      throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
	    }
	  }
	}