package com.vk.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vk.service.ExcelService;
import com.vk.service.ExpenseService;
import com.vk.service.IncomeService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RestController
@RequestMapping("/excel")
@RequiredArgsConstructor
public class ExcelController {
	
	
	private final ExcelService excelService;
	private final IncomeService incomeService;
	private final ExpenseService expenseService;
	
	@SneakyThrows
	@GetMapping("/dowload/income")
	public void downloadIncome(HttpServletResponse response) {
		
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=income.xlsx");
        excelService.writeIncomeToExcel(response.getOutputStream(), incomeService.getCurrentMonthIncomesForCurrentUser());
        
	}
	
	
	@SneakyThrows
	@GetMapping("/dowload/expense")
	public void downloadExpense(HttpServletResponse response) {
		
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=expense.xlsx");
        excelService.writeExpenseToExcel(response.getOutputStream(), expenseService.getCurrentMonthExpensesForCurrentUser());
        
	}
	

}
