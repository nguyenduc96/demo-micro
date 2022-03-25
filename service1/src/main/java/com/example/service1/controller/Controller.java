package com.example.service1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/service1")
    public String hello() {
        return "service 1 \n <a href='http://localhost:8088/service2'>Link to sv2</a>";
    }

}
