package com.moditraders.utility;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.moditraders.models.EmailModel;

@Component("emailUtility")
public class EmailUtility {
	
	
	private static final Logger log = LoggerFactory.getLogger(EmailUtility.class);

	
	@Value("$mail.smtp.auth")
	private String smtpAuth;
	
	@Value("mail.smtp.starttls.enable")
	private String smtpStartTLSEnable;
	
	@Value("mail.smtp.host")
	private String smtpHost;
	
	@Value("mail.smtp.port")
	private String smtpPort;

	public void sendEmail(EmailModel emailModel) {
		
		log.debug("Creating smtp connection with details : mail.smtp.auth :" + smtpAuth 
				+ ", mail.smtp.starttls.enable : "+smtpStartTLSEnable+ ", mail.smtp.host : " + smtpHost + ", mail.smtp.port :" + smtpPort);
		
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
	}
	
}
