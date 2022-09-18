package com.example.sbkafka;

//данные для подключения к БД, класс используется для up-download файлов .csv
public class MyDbConnection {
	public static String urls = new String("jdbc:postgresql://10.232.3.49:5432/dguaudit?sslmode=require");
	public static String username = new String("sergey");
	public static String password = new String("rbs123");
}
