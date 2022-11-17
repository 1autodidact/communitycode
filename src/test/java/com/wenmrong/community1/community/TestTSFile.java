package com.wenmrong.community1.community;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.*;
import com.wenmrong.community1.community.config.TsCosClient;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;


/**
 * <h3>community</h3>
 * <p></p>
 *
 * @author : Autodidact
 * @date : 2022-10-12 20:45
 **/
@SpringBootTest
//@RunWith(SpringJUnit4ClassRunner.class)

public class TestTSFile {
    @Autowired
    TsCosClient tsCosClient;
    @Test
    public void uploadFile() {
        COSClient cosClient = tsCosClient.getCosClient();
        File localFile = new File("C:\\Users\\AutodidactR\\Pictures\\Saved Pictures\\2500359.png");
        // 指定文件将要存放的存储桶
        String bucketName = "community-1301191929";
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        String key = "exampleobject";
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);

        System.out.println("aa");

    }
    @Test
    public void downLoadFile() throws IOException {
        COSClient cosClient = tsCosClient.getCosClient();
        // Bucket的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
        String bucketName = "community-1301191929";
    // 指定文件在 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示下载的文件 picture.jpg 在 folder 路径下
        String key = "exampleobject";
    // 方法1 获取下载输入流
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        COSObject cosObject = cosClient.getObject(getObjectRequest);
        COSObjectInputStream cosObjectInput = cosObject.getObjectContent();

        // 关闭输入流
        cosObjectInput.close();

        // 方法2 下载文件到本地的路径，例如 D 盘的某个目录
        String outputFilePath = "C:\\Users\\AutodidactR\\Desktop\\蒂法图\\aa.png";
        File downFile = new File(outputFilePath);
        getObjectRequest = new GetObjectRequest(bucketName, key);
        ObjectMetadata downObjectMeta = cosClient.getObject(getObjectRequest, downFile);
    }
}
