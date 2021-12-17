package com.example.sbkafka.Service;

public interface EmailService {

	void sendSimpleEmail(String message,String textorder,String frommail,String copyto);

}
