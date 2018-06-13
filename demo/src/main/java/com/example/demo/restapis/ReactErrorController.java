package com.example.demo.restapis;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;

@Controller
public class ReactErrorController implements ErrorController {

	@Override
	public String getErrorPath() {
		return "classpath:/static/index.html";
	}
}
