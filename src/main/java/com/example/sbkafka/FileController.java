package com.example.sbkafka;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class FileController {

	FileDB fileDB;
	
	@Autowired 
	private FileStorageService storageService;
	@Autowired
	private OrderFormService orderformservice;
	
	@PostMapping("/upload")
	  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file,@RequestParam("id") int id) {
	    String message = "";
	    try {
	      storageService.store(file,id);

	      message = "Uploaded the file successfully: " + file.getOriginalFilename();
	      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	    } catch (Exception e) {
	      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
	      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
	    }
	  }

	 /*  @GetMapping("/files")
	  public ResponseEntity<List<ResponseFile>> getListFiles() {
	    List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
	      String fileDownloadUri = ServletUriComponentsBuilder
	          .fromCurrentContextPath()
	          .path("/files/")
	          .path(dbFile.getId())
	          .toUriString();

	      return new ResponseFile(
	          dbFile.getName(),
	          fileDownloadUri,
	          dbFile.getType(),
	          dbFile.getData().length);
	    }).collect(Collectors.toList());

	    return ResponseEntity.status(HttpStatus.OK).body(files);
	  }  */

	   @GetMapping("/files/{id}")
	  public ResponseEntity<byte[]> getFile(@PathVariable int id) {
	    FileDB fileDB = storageService.getFile(id);

	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
	        .body(fileDB.getData());
	  } 
	  
	  // Загрузка файла заказа в базу данных
	@PostMapping("/fileLoad")
	public String load(@RequestParam("id") int id, @RequestParam("file") MultipartFile file) throws IOException{
		
		OrderForm order=orderformservice.getById(id);
		FileDB fileDB=storageService.load(file,id);
		order.setFileDB(fileDB);
		orderformservice.saveOrderForm(order);
		   
		   return "redirect:/admin/bdEdit";
}
	// Скачивание файла заказа из базы данных
	@PostMapping("/fileDownload")
	public String filedown(@RequestParam("id") int id) throws IOException{
		
		OrderForm order=orderformservice.getById(id);
		FileDB fileDB=order.getFileDB();
		String filename=fileDB.getName();
		byte[] bodytext=fileDB.getData();
		FileOutputStream fos=new FileOutputStream("C:/My/"+filename);
        fos.write(bodytext, 0, bodytext.length);
        fos.close();
		   
		   return "redirect:/bdShow";
}
}