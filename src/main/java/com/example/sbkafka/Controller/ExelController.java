package com.example.sbkafka.Controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sbkafka.Service.ExelService;

import lombok.RequiredArgsConstructor;

//сохранение заказов в exel

@RestController
@RequiredArgsConstructor
public class ExelController {
	private final ExelService exelService;
	
	@GetMapping("/copyToExel")
	public ResponseEntity<Resource> getEmployeesFile(){
		String filename = "dgu_orders.xlsx";
		InputStreamResource file = new InputStreamResource(exelService.ordersLoad());
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
				.body(file);
	}
}