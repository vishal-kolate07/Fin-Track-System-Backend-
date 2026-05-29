package com.vk.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vk.entity.ExpenseEnity;

public interface ExpenseRepository extends JpaRepository<ExpenseEnity, Long> {

    // Get all expenses by profileId, sorted by date (latest first)
    List<ExpenseEnity> findByProfileIdOrderByDateDesc(Long profileId);

    // Get top 5 latest expenses for a profile
    List<ExpenseEnity> findTop5ByProfileIdOrderByDateDesc(Long profileId);

    // Calculate total expense amount for a profile
    @Query("SELECT SUM(e.amount) FROM ExpenseEnity e WHERE e.profile.id = :profileId")
    BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileId);

    // Search expenses with keyword (case-insensitive) within date range and sorting
    List<ExpenseEnity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            Long profileId,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            Sort sort
    );

    // Get all expenses within date range for a profile
    List<ExpenseEnity> findByProfileIdAndDateBetween(
            Long profileId,
            LocalDate startDate,
            LocalDate endDate
    );
    
    
     List<ExpenseEnity> findByProfileIdAndDate(Long profileId, LocalDate date);
     
     	
    
    
    
    
    
    
    
}











/*package com.abhee.company.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import org.springframework.data.repository.query.Param;

import com.abhee.company.entity.ExpenseEnity;

public interface ExpenseRepository extends JpaRepository<ExpenseEnity, Long> {
	
	
	
	List<ExpenseEnity> findByProfileIdOrderByDateDesc(Long profileId);
	
	
	List<ExpenseEnity> findTop5ByProfileIdOrderByDateDesc(Long profileId);
	

	
	@Query("SELECT SUM(e.amount) FROM ExpenseEnity e WHERE e.profile.id = :profileId")
	BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileId);

	
	
	List<ExpenseEnity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
			Long profileId,
			LocalDate startDate,
			LocalDate endDate,
			String keyword,
			Sort sort
			);
	
	
	
	List<ExpenseEnity> findByProfileIdAndDateBetween(Long profileId,LocalDate startDate,LocalDate endDate);
	

}*/
