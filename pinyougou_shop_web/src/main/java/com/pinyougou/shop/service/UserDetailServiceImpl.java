package com.pinyougou.shop.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UserDetailServiceImpl implements UserDetailsService {
    @Reference
    private SellerService sellerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TbSeller tbSeller = sellerService.getById(username);
        //        构建角色l列表
        List<GrantedAuthority> authorties =new ArrayList<>();
        authorties.add(new SimpleGrantedAuthority("ROLE_SELLER"));
        if (tbSeller !=null && "1".equals(tbSeller.getStatus())){
            return new User(username,tbSeller.getPassword(),authorties);
        }else {
            return null;
        }
    }
}
