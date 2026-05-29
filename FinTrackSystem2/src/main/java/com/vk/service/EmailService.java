package com.vk.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class EmailService {
	
	
	private final JavaMailSender mailSender;
	
	/*
	 * @Value("${spring.mail.properties.mail.smtp.from}") private String fromEmail;
	 */
	
	@SneakyThrows
	public void sendEmail(String to ,String subject , String body) {
		
		
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setFrom("hmr18717@gmail.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		
		mailSender.send(message);
	}
	
	
	@SneakyThrows
	public void sendEmailWithAttachment(String toEmail, String subject, String body, byte[] attachment, String filename) {
	    
	    if(toEmail == null || toEmail.isBlank()) {
	        throw new IllegalArgumentException("Recipient email cannot be null or empty!");
	    }

	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, true);

	    helper.setFrom("hmr18717@gmail.com"); //  your sender email
	    helper.setTo(toEmail);                 //  recipient email
	    helper.setSubject(subject);
	    helper.setText(body);
	    helper.addAttachment(filename, new ByteArrayResource(attachment));

	    mailSender.send(message);
	}


}












