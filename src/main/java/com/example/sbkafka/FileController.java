package com.example.sbkafka;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
		return "okloadfile";}
		else {return"stopupload";}
		} catch (Exception e) 
		{return"exception";}	}   	   
  }
	// Скачивание файла заказа из базы данных
	@PostMapping("/fileDownload")
	public ResponseEntity<Resource> filedown(@RequestParam("id") int id) throws IOException{
		
		OrderForm order=orderformservice.getById(id);
		FileDB fileDB=order.getFileDB();
		String filename=order.getOrderNumber()+" PL_"+order.getBsNumber()+".docx";	
		byte[] bodytext=fileDB.getData();
		InputStreamResource file=new InputStreamResource(new ByteArrayInputStream(bodytext));
			
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
			        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
			        .body(file);}
		
		
	// Удаление файла из базы данных
	@PostMapping("/fileDelete")
	@Transactional
	public String filedelete(@RequestParam("id") int id) throws IOException{
		
		try {
		OrderForm order=orderformservice.getById(id);
		FileDB fileDB=order.getFileDB();
		if (fileDB==null) {return"nofilebd";} else {
		order.setFileDB(null);
		orderformservice.saveOrderForm(order);
		   
		   return "okfiledelete";}
		} catch (Exception e)
		{return"exception";}
}
	
}
	  