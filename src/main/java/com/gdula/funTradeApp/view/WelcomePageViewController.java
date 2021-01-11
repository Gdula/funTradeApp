package com.gdula.funTradeApp.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WelcomePageViewController {
    @GetMapping("/")
    public ModelAndView showHomePage() {
        return new ModelAndView("index-v2");
    }
}
