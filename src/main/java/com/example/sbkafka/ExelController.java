package com.example.sbkafka;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//сохранение базы заказов в exel

@Controller
public class ExelController {
	
	@Autowired OrderFormService orderformservice;
	
	@GetMapping("/downLoadExel")
		ResponseEntity<Resource> getFile(){
			String filename="dinamic_orders.xlsx";
			InputStreamResource file=new InputStreamResource(orderformservice.load());
			
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
			        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
			        .body(file);
		}
		
	@GetMapping("/saveToCsv")
	public String saveToCsv() {
		orderformservice.saveToCsv();
		return "redirect:/bdShow";
	}
	
	@GetMapping("/copyToCsv")
	ResponseEntity<Resource> getFileCsv() throws SQLException, IOException{
		String filename="dinamic_orders.xlsx";
		InputStreamResource file=copyToFile();
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
		        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
		        .body(file);
	}
	
	public static InputStreamResource copyToFile() //(Connection connection, String filePath, String tableOrQuery)
	          throws SQLException, IOException {  
	      
		  String urls = new String("jdbc:postgresql://ec2-54-155-254-112.eu-west-1.compute.amazonaws.com:5432/d747lpck3drmh3?sslmode=require"); //URL of the database
	      String username = new String("ecwnrxyfpgybjl"); //User name
	      String password = new String("834cddf4fd794b36ec9d57ba6fe2e7812022aa521f39ddc899ea959680229c79"); //Password
	      Connection conn = null;
	      //FileOutputStream fileOutputStream = null;  
	      String mytableOrQuery="(select ordernumber,bsnumber,datestart,dateend,calc,calcnds,comm from orderform order by ordernumber)";
	      //String home = System.getProperty("user.home");
	      //String myfilePath=home+"/Downloads/"+"orders.csv";
	      //String myfilePath="C:/My/orders.csv";
	      InputStreamResource file;
	      try {  
	    	  conn = DriverManager.getConnection(urls, username, password);
	          CopyManager copyManager = new CopyManager((BaseConnection) conn);  
	          //fileOutputStream = new FileOutputStream(myfilePath);
	          ByteArrayOutputStream out = new ByteArrayOutputStream();
	          copyManager.copyOut("COPY " + mytableOrQuery + "TO STDOUT WITH (FORMAT CSV, HEADER)", out);
	          ByteArrayInputStream in=new ByteArrayInputStream(out.toByteArray());
	          file=new InputStreamResource(in);
	      } finally 
	      {  
	          /*if (fileOutputStream != null) {  
	              try {  
	                  fileOutputStream.close();  
	              } catch (IOException e) {  
	                  e.printStackTrace();  
	              }  
	          } */ 
	          if (conn != null) {
	              try {
	                  conn.close();
	              } catch (SQLException e) { e.printStackTrace();}
	          }
	}
	      return file;
	}
}