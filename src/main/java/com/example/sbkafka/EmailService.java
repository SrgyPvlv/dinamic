package com.example.sbkafka;

import java.io.FileNotFoundException;
import javax.mail.MessagingException;

public interface EmailService {

	void sendSimpleEmail(String message,String textorder,String frommail,String copyto);

	void sendEmailWithAttachment(String message, String attachment,String frommail,String copyto)
			throws MessagingException, FileNotFoundException;
	
}
