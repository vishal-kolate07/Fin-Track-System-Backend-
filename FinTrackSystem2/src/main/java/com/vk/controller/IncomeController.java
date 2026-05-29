package com.vk.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vk.dto.IncomeDTO;
import com.vk.service.IncomeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/income")
public class IncomeController {
	
	
	private final IncomeService incomeService;
	
	@PostMapping
	public ResponseEntity<IncomeDTO> addIncome(@RequestBody IncomeDTO incomeDTO){
		
		IncomeDTO save = incomeService.addIncome(incomeDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(save);
	}
	
	@GetMapping
	public ResponseEntity<List<IncomeDTO>> getIncomes(){
		
		List<IncomeDTO> income = incomeService.getCurrentMonthIncomesForCurrentUser();
		return ResponseEntity.ok(income);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteIncome(@PathVariable Long id){
		
		incomeService.deleteIncome(id);
		return ResponseEntity.noContent().build();
	}
	

	
	
	
}
