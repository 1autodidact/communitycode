package com.wenmrong.community1.community.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class IndexController {
    @GetMapping("/")
    public String index(){

        return "index";
    }
}
