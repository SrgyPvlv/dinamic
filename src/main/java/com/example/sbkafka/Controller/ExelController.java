package com.example.sbkafka.Controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.sbkafka.MyDbConnection;

//сохранение базы заказов в exel

@Controller
public class ExelController {
	
	@GetMapping("/copyToCsv")
	ResponseEntity<Resource> getFileCsv() throws SQLException, IOException{
		String filename="dinamic_orders.csv";
		InputStreamResource file=copyToFile();
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
		        .contentType(MediaType.parseMediaType("text/csv"))
		        .body(file);
	}
	
	public static InputStreamResource copyToFile() throws SQLException, IOException {  
	      
		  String urls = MyDbConnection.urls;
	      String username = MyDbConnection.username;
	      String password = MyDbConnection.password;
	      Connection conn = null;
	      String myQuery="(select o.ordernumber,o.bsnumber,b.bsaddress,o.datestart,o.dateend,o.calc,o.calcnds,o.comm from orderform as o join bslist as b on o.bsnumber=b.bsnumber order by ordernumber)";
	      InputStreamResource file;
	      
	      try {  
	    	  conn = DriverManager.getConnection(urls, username, password);
	          CopyManager copyManager = new CopyManager((BaseConnection) conn);
	          ByteArrayOutputStream out = new ByteArrayOutputStream();
	          copyManager.copyOut("COPY " + myQuery + "TO STDOUT WITH (FORMAT CSV, HEADER)", out);
	          ByteArrayInputStream in=new ByteArrayInputStream(out.toByteArray());
	          file=new InputStreamResource(in);
	      } finally {      
	          if (conn != null) {
	              try {
	                  conn.close();
	              } catch (SQLException e) { e.printStackTrace();}
	          }
	}
	      return file;
	}
}