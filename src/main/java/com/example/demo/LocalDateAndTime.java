package com.example.demo;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class LocalDateAndTime {

    @RequestMapping(method = RequestMethod.GET, path = "/time")
    public String displayTime(){
        return LocalDateTime.now().toString();
    }

}
