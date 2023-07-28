package com.bf.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    @RequestMapping(value = "/{page:^(?!.*[.].*$).*$}")
    public String requestPage(@PathVariable("page") String page) {
        String htmlPage = "/" + page + ".html";
        return htmlPage;
    }

}


