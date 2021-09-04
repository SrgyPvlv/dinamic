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
		
		/*try {
		OrderForm order=orderformservice.getById(id);
		FileDB fileDB=order.getFileDB();
		String filename=fileDB.getName();
		byte[] bodytext=fileDB.getData();
		String home = System.getProperty("user.home");
		FileOutputStream fos=new FileOutputStream(home+"/Downloads/"+filename);
      fos.write(bodytext, 0, bodytext.length);
      fos.close();
		   
		   return "okdownload";*/


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
	//оставил для будущих проектов
	/*@PostMapping("/upload") 
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

	  @GetMapping("/files")
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
	  }

	   @GetMapping("/files/{id}")
	  public ResponseEntity<byte[]> getFile(@PathVariable int id) {
	    FileDB fileDB = storageService.getFile(id);

	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
	        .body(fileDB.getData());
	  } */
	  