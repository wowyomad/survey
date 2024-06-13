package org.wowyomad.survey.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping("/test")
    public String test(HttpServletRequest request) {
        request.getSession().setAttribute("redirectedFromTest", true);
        return "forward:/simple_page.html";
    }
}
