package com.example.sbkafka;

import java.io.FileNotFoundException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class DefaultEmailService implements EmailService {

	 @Autowired
	 public JavaMailSender emailSender;


	@Override
	 public void sendSimpleEmail(String message,String textorder,String frommail,String copyto) {
	  String from="dguaudit@gmail.com";
	  String toAddress="spavlov@mts.ru";
	  String subject=frommail+" Проверка заказов Динамикс завершена";
	  SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
	  simpleMailMessage.setFrom(from);
	  simpleMailMessage.setTo(toAddress);
	  simpleMailMessage.setCc(copyto);
	  simpleMailMessage.setSubject(subject);
	  simpleMailMessage.setText(message+"\n"+"Данные расчетов по Заказам:"+"\n\n"+textorder);
	  emailSender.send(simpleMailMessage);
	 }

	@Override
	 public void sendEmailWithAttachment(String message, String attachment,String frommail,String copyto) throws MessagingException, FileNotFoundException {
		String from="dguaudit@gmail.com";
		String toAddress="spavlov@mts.ru";
		  String subject=frommail+". Проверка заказов Динамикс завершена";
	  MimeMessage mimeMessage = emailSender.createMimeMessage();
	  MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
	  messageHelper.setFrom(from);
	  messageHelper.setTo(toAddress);
	  messageHelper.setCc(copyto);
	  messageHelper.setSubject(subject);
	  messageHelper.setText(message);
	  FileSystemResource file = new FileSystemResource(ResourceUtils.getFile(attachment));
	  messageHelper.addAttachment("Order Table", file);
	  emailSender.send(mimeMessage);
	 }
	}
