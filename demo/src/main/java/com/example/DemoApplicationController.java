package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by User on 21.12.2016..
 */
@Controller
public class DemoApplicationController {

    @RequestMapping(value="/",method = RequestMethod.GET)
    public String homepage(){
        return "index";
    }

}
