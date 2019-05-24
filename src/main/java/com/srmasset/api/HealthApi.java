package com.srmasset.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HealthApi {
	
	@GetMapping("/info")
	public ModelAndView checkHealth() {
		return new ModelAndView("redirect:actuator/health");
	}
}
