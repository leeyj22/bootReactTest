package com.example.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

    @RequestMapping(value = "/{page:^(?!.*[.].*$).*$}")
    public String requestPage(@PathVariable("page") String page){
        String htmlPage = "/"+page+".html";
        return htmlPage;
    }

}
