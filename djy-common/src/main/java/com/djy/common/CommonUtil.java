package com.djy.common;


import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class CommonUtil {

    /**
     * spring密码加密
     * @param password
     * @return
     */
    public static String password(String password){
        String str = DigestUtils.md5DigestAsHex(password.getBytes());
        return str;
    }

    public static String uuid(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }



    /**
     * 获取请求的IP
     * @param request
     */
    public static String getIpAddress(HttpServletRequest request) {
        // 这个一般是Nginx反向代理设置的参数
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多IP的情况（只取第一个IP）
        if (ip != null && ip.contains(",")) {
            String[] ipArray = ip.split(",");
            ip = ipArray[0];
        }
        return ip;

    }
    public static void main(String[] args){
        System.out.println(password("wyj"));
    }

}
