package com.wenmrong.community1.community.provider;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssClientAuthorization {

    @Value("${AliCloud.accessKeyId}")
    private String accessKeyId;

    @Value("${AliCloud.accessKeySecret}")
    private String accessKeySecret;

    @Value("${AliCloud.endpoint}")
    private String endpoint;

    @Value("${AliCloud.bucketName}")
    private String bucketName;

    // Endpoint以杭州为例，其它Region请按实际情况填写。
    // private  String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    // private String accessKeyId = "LTAI4FnvcGFkvgaysWEqGwub";
    // private String accessKeySecret = "tCTW7QsOtv24iD7r6BfmZS4l5WV0BM";

    public String upload(InputStream inputStream, String fileName) throws IOException {
        String generatedFileName;
        // .属于正则表达式,＋\\才能表示.
        String[] filePaths = fileName.split("\\.");
        if (filePaths.length > 1) {
            generatedFileName = UUID.randomUUID().toString() + "." + filePaths[filePaths.length - 1];
        } else {
            return null;
        }
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件流。
        ossClient.putObject(bucketName, generatedFileName, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
        return generatedFileName;
    }

}
