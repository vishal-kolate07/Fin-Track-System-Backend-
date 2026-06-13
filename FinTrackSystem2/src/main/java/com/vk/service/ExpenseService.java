package com.vk.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.vk.dto.ExpenseDTO;

import com.vk.entity.ExpenseEnity;
import com.vk.entity.ProfileEntity;
import com.vk.repository.ExpenseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {
	
	
	private final ProfileService profileService;
	private final ExpenseRepository expenseRepository;
	
	
	//add new expense into database
	
	public ExpenseDTO addExpense(ExpenseDTO dto) {
		
		ProfileEntity profile = profileService.getCurrentProfile();
		
		ExpenseEnity newExpense = toEntity(dto, profile);
		         newExpense = expenseRepository.save(newExpense);
		         
		         return toDTO(newExpense);
	}
	
	
	//Retrieves all expenses for current month/based on the start date and end date.
	
	public List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser(){
		
		ProfileEntity profile = profileService.getCurrentProfile();
		LocalDate now = LocalDate.now();
		LocalDate startDate = now.withDayOfMonth(1);
		LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
		
		List<ExpenseEnity> list = expenseRepository.findByProfileIdAndDateBetween(profile.getId(),startDate, endDate);
		return list.stream().map(this::toDTO).toList();
	}
	
	
	//delete expense by id.
	
	public void deleteExpense(Long expenseId) {
		
		ProfileEntity profile = profileService.getCurrentProfile();
		ExpenseEnity entity = expenseRepository.findById(expenseId)
				                           .orElseThrow(()-> new RuntimeException("Expense Not Found !"));
		
		if(!entity.getProfile().getId().equals(profile.getId())) {
			throw new RuntimeException("Unauthorized to delete the Expense.");
		}
		  expenseRepository.delete(entity);
	}
	
	
	//get latest 5 incomes for current user.
	
	public List<ExpenseDTO> getLatest5ExpensesForCurrentUser(){
		
		ProfileEntity profile = profileService.getCurrentProfile();
		List<ExpenseEnity> list = expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
		return list.stream().map(this::toDTO).toList();
		
	}
	
	
	
	//get total income for current user.
	
	public BigDecimal getTotalExpenseForCurrentUser() {
		
		ProfileEntity profile = profileService.getCurrentProfile();
		BigDecimal total =  expenseRepository.findTotalExpenseByProfileId(profile.getId());
		return total != null ? total : BigDecimal.ZERO;
		 
	}
	
	
	//filter expenses.
	
	public List<ExpenseDTO> filterExpenses(LocalDate startDate, LocalDate endDate,String keyword,Sort sort){
		
		ProfileEntity profile = profileService.getCurrentProfile();
		List<ExpenseEnity> list = expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyword, sort);
		return list.stream().map(this::toDTO).toList(); 
	}
	
	
	//notification
	
	public List<ExpenseDTO> getExpensesForUserOnDate(Long profileId,LocalDate date){
		
		List<ExpenseEnity> list = expenseRepository.findByProfileIdAndDate(profileId, date);
		return list.stream().map(this::toDTO).toList();
	}
	
	
	
	
	
	
	//helper method like model mapper we can use model mapper also 
	
	
	private ExpenseEnity toEntity(ExpenseDTO dto,ProfileEntity profile) {
		
		
		return ExpenseEnity.builder()
				           .name(dto.getName())
				           .icon(dto.getIcon())
				           .amount(dto.getAmount())
				           .date(dto.getDate())
				           .profile(profile)
				           .build();	
		
	}
	
	
	private ExpenseDTO toDTO(ExpenseEnity enity) {
		
		
		return ExpenseDTO.builder()
				         .id(enity.getId())
				         .name(enity.getName())
				         .icon(enity.getIcon())
				         .amount(enity.getAmount())
				         .date(enity.getDate())
				         .createdAt(enity.getCreatedAt())
				         .updatedAt(enity.getUpdatedAt())
				         .build();
				         
		
		
	}
	
	

}



















