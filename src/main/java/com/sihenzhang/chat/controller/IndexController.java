package com.sihenzhang.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
    @GetMapping({"/", "/index"})
    public ModelAndView chat() {
        return new ModelAndView("chat");
    }
}
