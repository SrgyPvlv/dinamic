package com.example.sbkafka.Controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.sbkafka.MyDbConnection;

@Controller
public class CsvBsListController {

	@PostMapping("/admin/csvBsListToDateBase") //загрузка БС сразу списком в БД (список в формате .csv)
	public String uploadFile(@RequestParam("file") MultipartFile  file,Model model){
		
		try {
			copyToDateBase(file);
	        return "redirect:/admin/findBsByBsName";
	      } catch (Exception e) {
	        model.addAttribute("note", "Не удалось загрузить список БС в БД!");
	        return "noUpload";
	      }
	}
	
	public static void copyToDateBase(MultipartFile file) throws SQLException, IOException {  
	      
		  String urls = MyDbConnection.urls;
	      String username = MyDbConnection.username;
	      String password = MyDbConnection.password;
	      Connection conn = null;
	      String myQuery="COPY bslist (bsnumber,bsaddress) FROM STDIN WITH CSV";
	      
	      try {  
	    	  conn = DriverManager.getConnection(urls, username, password);
	          CopyManager copyManager = new CopyManager((BaseConnection) conn);
	          InputStream inputStream =  new BufferedInputStream(file.getInputStream());
	          copyManager.copyIn(myQuery,inputStream);  
	          
	      }catch (Exception e) {
	          }
	      finally {      
	          if (conn != null) {
	              try {
	                  conn.close();
	              } catch (SQLException e) { e.printStackTrace();}
	          }
	}
	}
	
	@GetMapping("/bsListToCSV") //сохранение из базы списка БС в файл .csv
	ResponseEntity<Resource> getFileCsv() throws SQLException, IOException{
		String filename="bsList.csv";
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
	      String myQuery="COPY (select bsnumber,bsaddress from bslist order by bsnumber) TO STDOUT WITH (FORMAT CSV, HEADER)";
	      InputStreamResource file;
	      
	      try {  
	    	  conn = DriverManager.getConnection(urls, username, password);
	          CopyManager copyManager = new CopyManager((BaseConnection) conn);
	          ByteArrayOutputStream out = new ByteArrayOutputStream();
	          copyManager.copyOut(myQuery, out);
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