package com.jeido.tournoisgamer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BaseController {


    @RequestMapping("/")
    public String index() {
        return "home";
    }
}
