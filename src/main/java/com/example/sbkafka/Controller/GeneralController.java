package com.example.sbkafka.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.annotation.SessionScope;

@Controller
@SessionScope
public class GeneralController {

	@GetMapping("/login")
    public String login() {
        return "login";
    }
	
	@GetMapping("/admin")
    public String admin() {
        return "admin";
    }
	
	@GetMapping("/403")
    public String error403() {
        return "403";
    }
}
