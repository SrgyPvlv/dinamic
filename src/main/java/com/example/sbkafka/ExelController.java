package com.example.sbkafka;

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
		
	}

