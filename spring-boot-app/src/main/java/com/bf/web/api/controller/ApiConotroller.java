package com.bf.web.api.controller;

import com.bf.web.api.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
public class ApiConotroller {
    
    @Autowired
    private ApiService apiService;
    
    @ResponseBody
    @RequestMapping(value="/api/saveFile.json")
    public String saveFiles(MultipartHttpServletRequest request){
        return apiService.saveFiles(request);
    }
}
