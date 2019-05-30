package com.pinyougou.shop.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("sellerLogin")
public class SellerLoginController {
    @RequestMapping("info")
    public HashMap<String ,Object> test(){
        HashMap<String ,Object> hm = new HashMap();
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        hm.put("loginName",name);
        return hm;
    }
}
