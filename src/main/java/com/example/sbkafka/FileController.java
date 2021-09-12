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
import org.springframework.ui.Model;
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
	public String load(@RequestParam("id") int id, @RequestParam("file") MultipartFile file,Model model) throws IOException{
		boolean isempty=file.isEmpty();
		if (isempty==true) { 
			String note="Ошибка  - Не выбран файл для загрузки!";
		    model.addAttribute("note", note);
		    return "noDone";} else {
		try {
		OrderForm order=orderformservice.getById(id);
		FileDB isDBYes=order.getFileDB();
		if (isDBYes==null) {
		FileDB fileDB=storageService.load(file,id);
		order.setFileDB(fileDB);
		orderformservice.saveOrderForm(order);
		String title="Сохранение файла Заказа";
		String note="Файл Заказа сохранен в базе данных!";
		model.addAttribute("title", title);
		model.addAttribute("note", note);
		return "okDone";}
		else {
			  String note="Ошибка  - Файл Заказа уже существует в базе! (Удалите его перед загрузкой новой версии.)";
		      model.addAttribute("note", note);
			  return"noDone";}
		} catch (Exception e) 
		{ String note="Ошибка  - Что-то пошло не так!";
	      model.addAttribute("note", note);
		  return"noDone";}	}   	   
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
	public String fileDelete(@RequestParam("id") int id,Model model) throws IOException{
		
		try {
		OrderForm order=orderformservice.getById(id);
		FileDB fileDB=order.getFileDB();
		if (fileDB==null) {
			String note="Ошибка  - В базе нет файла для этого Заказа!";
		      model.addAttribute("note", note);
			  return"noDone";} else {
		order.setFileDB(null);
		orderformservice.saveOrderForm(order);
		String title="Удаление файла Заказа";
		String note="Файл Заказа удален!";
		model.addAttribute("title", title);
		model.addAttribute("note", note);
		   return "okDone";}
		} catch (Exception e)
		{String note="Ошибка  - Что-то пошло не так!";
	      model.addAttribute("note", note);
		  return"noDone";}
}
	//редирект на страницу ошибки
	@GetMapping("/errorDownload")
	public String errorDownload(Model model) {
		
		  String note="Ошибка  - В базе нет файла для этого Заказа!";
	      model.addAttribute("note", note);
		  return"noDone";
	}
}
	  