package com.wenmrong.community1.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublishController {
    @GetMapping("/publish")
    public String publish(){
        return "publish";

    }
}
