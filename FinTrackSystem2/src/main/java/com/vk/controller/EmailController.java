package com.vk.controller;

import java.io.ByteArrayOutputStream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vk.entity.ProfileEntity;
import com.vk.service.EmailService;
import com.vk.service.ExcelService;
import com.vk.service.ExpenseService;
import com.vk.service.IncomeService;
import com.vk.service.ProfileService;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {
	
	
	private final ExcelService excelService;
	private final IncomeService incomeService;
	private final ExpenseService expenseService;
	private final EmailService emailService;
	private final ProfileService profileService;
	
	
	@SneakyThrows
	@GetMapping("/income-excel")
	public ResponseEntity<Void> emailIncomeExcel(){

		ProfileEntity profile = profileService.getCurrentProfile();
		if(profile == null || profile.getEmail() == null || profile.getEmail().isEmpty()) {
		    throw new IllegalArgumentException("Current user profile or email is missing!");
		}

		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		excelService.writeIncomeToExcel(baos, incomeService.getCurrentMonthIncomesForCurrentUser());
		emailService.sendEmailWithAttachment(profile.getEmail(),
				                            "Income Report Summary",
				                            "I’ve attached your income report — take a look.",
				                            baos.toByteArray(), "income.xlsx");
		return ResponseEntity.ok(null);
	}
	
	
	
	@SneakyThrows
	@GetMapping("/expense-excel")
	public ResponseEntity<Void> emailExpenseExcel(){
		
		
		ProfileEntity profile = profileService.getCurrentProfile();
		if(profile == null || profile.getEmail() == null || profile.getEmail().isEmpty()) {
		    throw new IllegalArgumentException("Current user profile or email is missing!");
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		excelService.writeExpenseToExcel(baos, expenseService.getCurrentMonthExpensesForCurrentUser());
		emailService.sendEmailWithAttachment(profile.getEmail(),
				                            "Expense Report Summary",
				                            "I’ve attached your expense report — take a look.",
				                            baos.toByteArray(), "expense.xlsx");
		return ResponseEntity.ok().build();
	}
	
	

}











