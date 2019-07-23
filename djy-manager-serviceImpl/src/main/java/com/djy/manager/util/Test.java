package com.djy.manager.util;

import org.springframework.util.DigestUtils;

public class Test {


    public static void main(String[] args){
        System.out.println(test());
    }

    public static String test(){
        String str = DigestUtils.md5DigestAsHex("wyjmlf1314".getBytes());
        return str;
    }
}
