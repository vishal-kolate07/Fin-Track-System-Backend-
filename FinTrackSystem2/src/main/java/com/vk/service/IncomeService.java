package com.vk.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.vk.dto.IncomeDTO;
import com.vk.entity.CategoryEntity;
import com.vk.entity.IncomeEntity;
import com.vk.entity.ProfileEntity;
import com.vk.repository.CategoryRepository;
import com.vk.repository.IncomeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncomeService {
	
	
	private final IncomeRepository incomeRepository;
	private final CategoryRepository categoryRepository;
	private final ProfileService profileService;
	
	
	public IncomeDTO addIncome(IncomeDTO dto) {
		
		ProfileEntity profile = profileService.getCurrentProfile();
		CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
				                                          .orElseThrow(()-> new RuntimeException("Category Not Found."));
		
		IncomeEntity newIncome = toEntity(dto, profile, category);
		         newIncome = incomeRepository.save(newIncome);
		         
		         return toDTO(newIncome);
	}
	
	
	//Retrieves all incomes for current month/based on the start date and end date.
	
	public List<IncomeDTO> getCurrentMonthIncomesForCurrentUser(){
		
		ProfileEntity profile = profileService.getCurrentProfile();
		LocalDate now = LocalDate.now();
		LocalDate startDate = now.withDayOfMonth(1);
		LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
		
		List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetween(profile.getId(),startDate, endDate);
		return list.stream().map(this::toDTO).toList();
	}
	
	
	//delete expense by id.
	
	public void deleteIncome(Long incomeId) {
		
		ProfileEntity profile = profileService.getCurrentProfile();
		IncomeEntity entity = incomeRepository.findById(incomeId)
				                           .orElseThrow(()-> new RuntimeException("Income Not Found !"));
		
		if(!entity.getProfile().getId().equals(profile.getId())) {
			throw new RuntimeException("Unauthorized to delete the Incomes.");
		}
		  incomeRepository.delete(entity);
	}
	
	//get latest 5 expense for current user.
	
		public List<IncomeDTO> getLatest5IncomesForCurrentUser(){
			
			ProfileEntity profile = profileService.getCurrentProfile();
			List<IncomeEntity> list = incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
			return list.stream().map(this::toDTO).toList();
			
		}
		
		
		
		//get total expenses for current user.
		
		public BigDecimal getTotalIncomeForCurrentUser() {
			
			ProfileEntity profile = profileService.getCurrentProfile();
			BigDecimal total =  incomeRepository.findTotalIncomeByProfileId(profile.getId());
			return total != null ? total : BigDecimal.ZERO;
			 
		}
		
	
		//filter expenses.
		
		public List<IncomeDTO> filterIncome(LocalDate startDate, LocalDate endDate,String keyword,Sort sort){
			
			ProfileEntity profile = profileService.getCurrentProfile();
			List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate,keyword, sort);
			return list.stream().map(this::toDTO).toList(); 
		}
	
	
	
	
	
	//helper method like model mapper we can use model mapper also 
	
	
	private IncomeEntity toEntity(IncomeDTO dto,ProfileEntity profile,CategoryEntity category) {
		
		
		return IncomeEntity.builder()
				           .name(dto.getName())
				           .icon(dto.getIcon())
				           .amount(dto.getAmount())
				           .date(dto.getDate())
				           .profile(profile)
				           .category(category)
				           .build();	
	}
	
	
	private IncomeDTO toDTO(IncomeEntity enity) {
		
		
		return IncomeDTO.builder()
				         .id(enity.getId())
				         .name(enity.getName())
				         .icon(enity.getIcon())
				         .categoryId(enity.getCategory() != null ? enity.getCategory().getId() : null)
				         .categoryName(enity.getCategory() != null ? enity.getCategory().getName() : "N/A")
				         .amount(enity.getAmount())
				         .date(enity.getDate())
				         .createdAt(enity.getCreatedAt())
				         .updatedAt(enity.getUpdatedAt())
				         .build();
	}
	
	

}
