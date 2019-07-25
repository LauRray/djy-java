package com.djy.core;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


public class OssSign {



      private static final String accessId = "LTAIqPycv7jvtl9p"; // 请填写您的AccessKeyId。
         private static final String accessKey = "55SFqMDY38aHFexmrjMsq4yv35rvGM"; // 请填写您的AccessKeySecret。
         private static final String endpoint = "oss-cn-shanghai.aliyuncs.com"; // 请填写您的 endpoint。
         private static final String bucket = "jffy"; // 请填写您的 bucketname 。.oss-cn-beijing.aliyuncs.com
        private static final String host = "https://" + bucket + "." + endpoint;// host的格式为 bucketname.endpoint
          // callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
         //String callbackUrl = "http://88.88.88.88.:8888";
         private static final String dir = "user-dir-prefix/"; // 用户上传文件时指定的前缀。

//    private static final String accessId = "x7l8VUPvURfSnnrV"; // 请填写您的AccessKeyId。
//    private static final String accessKey = "qo9GGeQgOmrxUzdSsd6nIfIkVrpqC3"; // 请填写您的AccessKeySecret。
//    private static final String endpoint = "oss-cn-hangzhou.aliyuncs.com"; // 请填写您的 endpoint。
//    private static final String bucket = "mini-mall-image"; // 请填写您的 bucketname 。.oss-cn-beijing.aliyuncs.com
//    private static final String host = "https://" + bucket + "." + endpoint;// host的格式为 bucketname.endpoint
//    // callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
//    //String callbackUrl = "http://88.88.88.88.:8888";
//    private static final String dir = "user-dir-prefix/"; // 用户上传文件时指定的前缀。


    public Map<String, String> ossSign(HttpServletRequest request, HttpServletResponse response) {
        OSSClient client = new OSSClient(endpoint, accessId, accessKey);
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            respMap.put("accesssecret", accessKey);
            respMap.put("bucket", bucket);
            return respMap;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
