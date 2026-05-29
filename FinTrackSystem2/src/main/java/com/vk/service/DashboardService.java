/*
package com.abhee.company.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.abhee.company.dto.ExpenseDTO;
import com.abhee.company.dto.IncomeDTO;
import com.abhee.company.dto.RecentTransactionDTO;
import com.abhee.company.entity.ProfileEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {
	
	private final ProfileService  profileService;
	private final IncomeService incomeService;
	private final ExpenseService expenseService;
	
	
	
	public Map<String, Object> getDashboardData(){
		
		ProfileEntity profile = profileService.getCurrentProfile();
		Map<String, Object> returnValue = new LinkedHashMap<>();
		List<IncomeDTO> latestIncome = incomeService.getLatest5IncomesForCurrentUser();
		List<ExpenseDTO> latestExpense = expenseService.getLatest5ExpensesForCurrentUser();
		
	    List<RecentTransactionDTO>  recentTransaction  = concat(latestIncome.stream().map(income -> RecentTransactionDTO.builder()
	    		                                                       .id(income.getId())
	    		                                                       .profileId(profile.getId())
	    		                                                       .icon(income.getIcon())
	    		                                                       .name(income.getName())
	    		                                                       .amount(income.getAmount())
	    		                                                       .date(income.getDate())
	    		                                                       .createdAt(income.getCreatedAt())
	    		                                                       .updatedAt(income.getUpdatedAt())
	    		                                                       .type("income")
	    		                                                       .build()),
	    		
	    		                                  latestExpense.stream().map(expense -> 
	    		                                    RecentTransactionDTO.builder()
	    		                                                        .id(expense.getId())
	    		                                                        .profileId(profile.getId())
		    		                                                    .icon(expense.getIcon())
		    		                                                    .name(expense.getName())
		    		                                                    .amount(expense.getAmount())
		    		                                                    .date(expense.getDate())
		    		                                                    .createdAt(expense.getCreatedAt())
		    		                                                    .updatedAt(expense.getUpdatedAt())
		    		                                                    .type("expense")
		    		                                                    .build()))
		.sorted((a,b) -> {
			
			int cmp = b.getDate().compareTo(a.getDate());
			if(cmp == 0 &&  a.getCreatedAt() != null &&  b.getCreatedAt() != null ) {
				
				return b.getCreatedAt().compareTo(a.getCreatedAt());
			}
			
			return cmp;
		}).collect(Collectors.toList());
	    
	    returnValue.put("totalBalance", incomeService.getTotalIncomeForCurrentUser().subtract(expenseService.getTotalExpenseForCurrentUser()));
	    
	    returnValue.put("totalIncome", incomeService.getTotalIncomeForCurrentUser());
	    returnValue.put("totalExpenses", expenseService.getTotalExpenseForCurrentUser());
	    returnValue.put("recent5Expenses",latestExpense);
	    returnValue.put("recent5Incomes", latestIncome);
	    returnValue.put("recentTransaction", recentTransaction);
	    return returnValue;
	}

}
*/

package com.vk.service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.vk.dto.ExpenseDTO;
import com.vk.dto.IncomeDTO;
import com.vk.dto.RecentTransactionDTO;
import com.vk.entity.ProfileEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {
	
    private final ProfileService profileService;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;
	
    public Map<String, Object> getDashboardData() {
        
        ProfileEntity profile = profileService.getCurrentProfile();
        Map<String, Object> returnValue = new LinkedHashMap<>();

        // Fetch latest incomes & expenses
        List<IncomeDTO> latestIncome = incomeService.getLatest5IncomesForCurrentUser();
        List<ExpenseDTO> latestExpense = expenseService.getLatest5ExpensesForCurrentUser();

        // Merge incomes & expenses into recent transactions
        List<RecentTransactionDTO> recentTransaction = Stream.concat(
                latestIncome.stream().map(income -> RecentTransactionDTO.builder()
                        .id(income.getId())
                        .profileId(profile.getId())
                        .icon(income.getIcon())
                        .name(income.getName())
                        .amount(income.getAmount())
                        .date(income.getDate())
                        .createdAt(income.getCreatedAt())
                        .updatedAt(income.getUpdatedAt())
                        .type("income")
                        .build()),

                latestExpense.stream().map(expense -> RecentTransactionDTO.builder()
                        .id(expense.getId())
                        .profileId(profile.getId())
                        .icon(expense.getIcon())
                        .name(expense.getName())
                        .amount(expense.getAmount())
                        .date(expense.getDate())
                        .createdAt(expense.getCreatedAt())
                        .updatedAt(expense.getUpdatedAt())
                        .type("expense")
                        .build())
        )
        .sorted((a, b) -> {
            int cmp = b.getDate().compareTo(a.getDate()); // Sort by date (desc)
            if (cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null) {
                return b.getCreatedAt().compareTo(a.getCreatedAt()); // If same date, sort by createdAt (desc)
            }
            return cmp;
        })
        .collect(Collectors.toList());

        // Calculate totals once (avoid multiple service calls)
        BigDecimal totalIncome = incomeService.getTotalIncomeForCurrentUser();
        BigDecimal totalExpense = expenseService.getTotalExpenseForCurrentUser();
        BigDecimal totalBalance = totalIncome.subtract(totalExpense);

        // Populate return values
        returnValue.put("totalBalance", totalBalance);
        returnValue.put("totalIncome", totalIncome);
        returnValue.put("totalExpenses", totalExpense);
        returnValue.put("recent5Expenses", latestExpense);
        returnValue.put("recent5Incomes", latestIncome);
        returnValue.put("recentTransaction", recentTransaction);

        return returnValue;
    }
}
