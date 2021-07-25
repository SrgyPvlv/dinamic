package com.example.sbkafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class EmailController {

    @Autowired
    public EmailService emailService;
	
	@GetMapping("/mailSend")
	public String showMail(Model model) {
		String textorder=MainController.orderArray.toString().replace("[","").replace("]","").replace(",", " ").//
	    replace(".", ",").replace("|","\n");
		model.addAttribute("textorder", textorder);
	    return "mailpage";
	}
	
	@PostMapping("/mailSend")
	public String sendMail(@RequestParam(value="comment") String textemail,@RequestParam(value="from") String frommail,@RequestParam(value="copy") String copyto) {
		
		try {
			
            String textorder=MainController.orderArray.toString().replace("[","").replace("]","").replace(",", " ").//
            replace(".", ",").replace("|","\n");
            this.emailService.sendSimpleEmail(textemail,textorder,frommail,copyto);
            
        } catch (Exception e) {e.printStackTrace();}
		
	    return "oksend";
	}
	
	@PostMapping("/mailSendWithAttech")
	public String sendMailWithAttachment(@RequestParam(value="comment") String textemail,@RequestParam(value="file") MultipartFile file,@RequestParam(value="from") String frommail,@RequestParam(value="copy") String copyto) {
		
		try {
						
            String path1=MainController.outputFileName;
            this.emailService.sendEmailWithAttachment(textemail,path1,frommail,copyto);
            
        } catch (Exception e) {e.printStackTrace();}
		
	    return "oksend";
	}
	
}

/*
*byte[] bytes = file.getBytes();
*Path path = Paths.get(UPLOADED_fOLDER+file.getOriginalFilename());
*Files.write(path, bytes);
*String path1 = path.toString(); оставил для будущих проектов
*/