package com.vk.service;

import java.io.OutputStream;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.vk.dto.ExpenseDTO;
import com.vk.dto.IncomeDTO;

import lombok.SneakyThrows;

@Service
public class ExcelService {
	
	@SneakyThrows
	public void writeIncomeToExcel(OutputStream os,List<IncomeDTO> incomes) {
		try (Workbook workbook = new XSSFWorkbook()){
			Sheet sheet = workbook.createSheet("Incomes");
			Row header = sheet.createRow(0);
			header.createCell(0).setCellValue("Sr.No");
			header.createCell(1).setCellValue("Name");
			header.createCell(2).setCellValue("Category");
			header.createCell(3).setCellValue("Amount");
			header.createCell(4).setCellValue("Date");
			
			IntStream.range(0, incomes.size()).forEach(i -> {
				 IncomeDTO income = incomes.get(i);
				 Row row = sheet.createRow(i+1);
				 row.createCell(0).setCellValue(i+1);
				 row.createCell(1).setCellValue(income.getName() != null ? income.getName() : "N/A");
				 row.createCell(2).setCellValue(income.getCategoryId() != null ? income.getCategoryName() : "N/A");
				 row.createCell(3).setCellValue(income.getAmount() != null ? income.getAmount().doubleValue(): 0);
				 row.createCell(4).setCellValue(income.getDate() != null ? income.getDate().toString() : "N/A");
				 
			});
			
			workbook.write(os);
				
		}catch (Exception e) {
			
		}
	}
	
	
	
	@SneakyThrows
	public void writeExpenseToExcel(OutputStream os,List<ExpenseDTO> expense) {
		try (Workbook workbook = new XSSFWorkbook()){
			Sheet sheet = workbook.createSheet("Expense");
			Row header = sheet.createRow(0);
			header.createCell(0).setCellValue("Sr.No");
			header.createCell(1).setCellValue("Name");
			header.createCell(2).setCellValue("Category");
			header.createCell(3).setCellValue("Amount");
			header.createCell(4).setCellValue("Date");
			
			IntStream.range(0, expense.size()).forEach(i -> {
				 ExpenseDTO income = expense.get(i);
				 Row row = sheet.createRow(i+1);
				 row.createCell(0).setCellValue(i+1);
				 row.createCell(1).setCellValue(income.getName() != null ? income.getName() : "N/A");
				 row.createCell(2).setCellValue(income.getCategoryId() != null ? income.getCategoryName() : "N/A");
				 row.createCell(3).setCellValue(income.getAmount() != null ? income.getAmount().doubleValue(): 0);
				 row.createCell(4).setCellValue(income.getDate() != null ? income.getDate().toString() : "N/A");
				 
			});
			
			workbook.write(os);
				
		}catch (Exception e) {
			
		}
	}

}
