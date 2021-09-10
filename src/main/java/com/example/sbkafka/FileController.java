package com.example.sbkafka;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileController {

	FileDB fileDB;
	
	@Autowired 
	private FileStorageService storageService;
	@Autowired
	private OrderFormService orderformservice;
	
	  // Загрузка файла заказа в базу данных
	@PostMapping("/fileLoad")
	public String load(@RequestParam("id") int id, @RequestParam("file") MultipartFile file) throws IOException{
		boolean isempty=file.isEmpty();
		if (isempty==true) {return "nofile";} else {
		try {
		OrderForm order=orderformservice.getById(id);
		FileDB isDBYes=order.getFileDB();
		if (isDBYes==null) {
		FileDB fileDB=storageService.load(file,id);
		order.setFileDB(fileDB);
		orderformservice.saveOrderForm(order);
		return "okLoadFile";}
		else {return"stopUpLoad";}
		} catch (Exception e) 
		{return"exception";}	}   	   
  }
	// Скачивание файла заказа из базы данных
	@GetMapping("/fileDownload")
	public ResponseEntity<Resource> fileDown(@RequestParam("id") int id, HttpServletResponse response,HttpServletRequest request) throws IOException{
		try {
		OrderForm order=orderformservice.getById(id);
		FileDB fileDB=order.getFileDB();
		String filename=order.getOrderNumber()+" PL_"+order.getBsNumber()+".doc";
		byte[] bodytext=fileDB.getData();
		InputStreamResource file=new InputStreamResource(new ByteArrayInputStream(bodytext));
			
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
			        .contentType(MediaType.parseMediaType("application/msword"))
			        .body(file);}
	catch(Exception e) {response.sendRedirect("/errorDownload");}
		return null;
	}	
	
	// Удаление файла из базы данных 
	@PostMapping("/fileDelete")
	@Transactional
	public String fileDelete(@RequestParam("id") int id) throws IOException{
		
		try {
		OrderForm order=orderformservice.getById(id);
		FileDB fileDB=order.getFileDB();
		if (fileDB==null) {return"noFileBD";} else {
		order.setFileDB(null);
		orderformservice.saveOrderForm(order);
		   
		   return "okFileDelete";}
		} catch (Exception e)
		{return"exception";}
}
	//редирект на страницу ошибки
	@GetMapping("/errorDownload")
	public String errorDownload() {
		
	   return "noFileBD";
	}
}
	  