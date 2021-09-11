package com.example.sbkafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
	  simpleMailMessage.setText(message+"\n\n"+"Данные расчетов по Заказам:"+"\n\n"+textorder);
	  emailSender.send(simpleMailMessage);
	 }

	}
