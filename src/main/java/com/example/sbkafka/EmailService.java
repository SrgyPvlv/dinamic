package com.example.sbkafka;

public interface EmailService {

	void sendSimpleEmail(String message,String textorder,String frommail,String copyto);

}
