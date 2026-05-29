package com.vk.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.vk.dto.ExpenseDTO;
import com.vk.entity.ProfileEntity;
import com.vk.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
	
	
	private final ProfileRepository profileRepository;
	private final ExpenseService expenseService;
	private final EmailService emailService;
	
	
	@Value("${fintrack.system.frontend.url}")
	private String frontedURL;
	
	//@Scheduled(cron = "0 * * * * * ",zone = "IST")     // this is only testing purpose in 1 min to send mail
	@Scheduled(cron = "0 0 22 * * * ",zone = "IST")
	public void sendDailyIncomeExpenseRemainder() {
		
		log.info("Job Started : sendDailyIncomeExpenseRemainder");
		List<ProfileEntity> profiles = profileRepository.findAll();
		
		for(ProfileEntity profile : profiles) {
			
			String body = "Hi " + profile.getFullName() + ",<br><br>"
				    + "This is a friendly reminder to update your expenses and track your budget today.<br><br>"
				    + "<a href='" + frontedURL + "' "
				    + "style='display:inline-block; padding:10px 18px; "
				    + "background-color:#0b5cff; color:#ffffff; "
				    + "text-decoration:none; border-radius:6px; "
				    + "font-size:14px; font-family:Arial, sans-serif;'>"
				    + "Update Expenses</a><br><br>"
				    + "Staying consistent helps you stay in control of your finances. 💰✨<br><br>"
				    + "Best regards,<br>"
				    + "<strong>FinTrack Team</strong>";
			
			
			emailService.sendEmail(profile.getEmail(), "Keep Your Budget in Control – Update Now", body);
		}
		
		log.info("Job Complted : sendDailyIncomeExpenseRemainder");	
	}
	
	//@Scheduled(cron = "0 * * * * * ",zone = "IST") // this is testing purpose
	@Scheduled(cron = "0 0 23 * * *", zone = "IST")
	public void sendDailyExpenseSummary() {

	    log.info("Job Started : sendDailyExpenseSummary");

	    List<ProfileEntity> profiles = profileRepository.findAll();

	    for (ProfileEntity profile : profiles) {

	        List<ExpenseDTO> todaysExpenses =
	                expenseService.getExpensesForUserOnDate(profile.getId(), LocalDate.now());

	        if (!todaysExpenses.isEmpty()) {

	            StringBuilder table = new StringBuilder();

	            table.append("<table style='border-collapse: collapse; width:100%; font-family: Arial, sans-serif; font-size:14px;'>");

	            // Header
	            table.append("<tr style='background-color:#0b5cff; color:#ffffff; text-align:left;'>");
	            table.append("<th style='padding:8px; border:1px solid #ddd;'>Sr No</th>");
	            table.append("<th style='padding:8px; border:1px solid #ddd;'>Name</th>");
	            table.append("<th style='padding:8px; border:1px solid #ddd;'>Amount</th>");
	            table.append("<th style='padding:8px; border:1px solid #ddd;'>Category</th>");
	            table.append("<th style='padding:8px; border:1px solid #ddd;'>Date</th>");
	            table.append("</tr>");

	            // Data Rows
	            int i = 1;
	            for (ExpenseDTO expense : todaysExpenses) {
	                table.append("<tr style='background-color:" + (i % 2 == 0 ? "#ffffff" : "#f9f9f9") + ";'>");
	                table.append("<td style='padding:8px; border:1px solid #ddd;'>" + i + "</td>");
	                table.append("<td style='padding:8px; border:1px solid #ddd;'>" + expense.getName() + "</td>");
	                table.append("<td style='padding:8px; border:1px solid #ddd;'>₹" + expense.getAmount() + "</td>");
	                table.append("<td style='padding:8px; border:1px solid #ddd;'>" + expense.getCategoryName() + "</td>");
	                table.append("<td style='padding:8px; border:1px solid #ddd;'>" + expense.getDate() + "</td>");
	                table.append("</tr>");
	                i++;
	            }

	            table.append("</table>");

	            // Now you can use this table in your email body
	            String body = "Hi " + profile.getFullName() + ",<br><br>"
	                    + "Here is your expense summary for today:<br><br>"
	                    + table.toString()
	                    + "<br><br>Staying consistent helps you stay in control of your finances. 💰✨<br><br>"
	                    + "Best regards,<br><strong>FinTrack Team</strong>";

	            // TODO: send email with body
	             emailService.sendEmail(profile.getEmail(), "Daily Expense Summary", body);
	        }
	    }
	    
		log.info("Job Complted : Send Daily Summary");	
	}

	
	

}
