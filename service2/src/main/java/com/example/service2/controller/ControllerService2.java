package com.example.service2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class ControllerService2 {
    @Autowired
    HttpSession session;

    @GetMapping("/service2")
    public String get() {
        return "Service2 \n <a href='http://localhost:8088/service1'>Link to Service 1</a>";
    }
}
