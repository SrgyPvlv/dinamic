package com.example.sbkafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class EmailController {

    @Autowired
    public EmailService emailService;
	
	@GetMapping("/mailSend")
	public String showMail() {
		
	    return "mailpage";
	}
	
	@PostMapping("/mailSend")
	public String sendMailWithAttachment(@RequestParam(value="comment") String textemail,@RequestParam(value="file") MultipartFile file,@RequestParam(value="from") String frommail,@RequestParam(value="copy") String copyto) {
		
		try {
			/*
			*byte[] bytes = file.getBytes();
	        *Path path = Paths.get(UPLOADED_fOLDER+file.getOriginalFilename());
            *Files.write(path, bytes);
            *String path1 = path.toString(); оставил для будущих проектов
            */
            String path1=MainController.outputFileName;
            this.emailService.sendEmailWithAttachment(textemail,path1,frommail,copyto);
            
        } catch (Exception e) {e.printStackTrace();}
		
	    return "oksend";
	}
	@PostMapping("/mailSendTest")
	public String sendMailWithAttachment(@RequestParam(value="comment") String textemail,@RequestParam(value="from") String frommail,@RequestParam(value="copy") String copyto) {
		
		try {
			/*
			*byte[] bytes = file.getBytes();
	        *Path path = Paths.get(UPLOADED_fOLDER+file.getOriginalFilename());
            *Files.write(path, bytes);
            *String path1 = path.toString(); оставил для будущих проектов
            */
            //String path1=MainController.outputFileName;
            this.emailService.sendSimpleEmail(textemail,frommail,copyto);
            
        } catch (Exception e) {e.printStackTrace();}
		
	    return "oksend";
	}
}
