package com.pinyougou.sellergoods.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("login")
public class LoginController {
    @RequestMapping("info")
    public HashMap<String,String> loginName(){
        HashMap<String,String> hm = new HashMap();
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        hm.put("loginName",name);
        return hm;
    }
}
