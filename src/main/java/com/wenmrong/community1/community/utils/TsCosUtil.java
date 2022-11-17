package com.wenmrong.community1.community.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import com.wenmrong.community1.community.config.TsCosClient;
import com.wenmrong.community1.community.exception.CustomizeException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import static com.wenmrong.community1.community.exception.CustomizeErrorCode.UPLOAD_FAILURE;


/**
 * <h3>community</h3>
 * <p></p>
 *
 * @author : Autodidact
 * @date : 2022-10-11 23:01
 **/
@Slf4j
public class TsCosUtil {
    static SnowFlake snowFlake = new SnowFlake(2, 3);

    public static void downLoadFile(TsCosClient tosClient, File downFile) throws IOException {
        COSClient cosClient = tosClient.getCosClient();
        // Bucket的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
        // 指定文件在 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示下载的文件 picture.jpg 在 folder 路径下

        GetObjectRequest getObjectRequest = new GetObjectRequest(tosClient.getBuckName(), String.valueOf(snowFlake.nextId()));
        ObjectMetadata downObjectMeta = cosClient.getObject(getObjectRequest, downFile);
    }

    public static String uploadLocalFile(TsCosClient tosClient, File uploadFile) throws URISyntaxException {
        COSClient cosClient = tosClient.getCosClient();
        String key = String.valueOf(snowFlake.nextId());
        PutObjectRequest putObjectRequest = new PutObjectRequest(tosClient.getBuckName(), key, uploadFile);
        putObjectRequest.putCustomRequestHeader("Content-Type", "image/png");
        cosClient.putObject(putObjectRequest);
        return cosClient.getObjectUrl(tosClient.getBuckName(), key).toString();

    }


    public static String uploadByStream(TsCosClient tsCosClient, InputStream inputStream) {
        String bucketName = tsCosClient.getBuckName();
        String key = String.valueOf(snowFlake.nextId());
        TransferManager transferManager = tsCosClient.getTransferManager();
        ObjectMetadata objectMetadata = new ObjectMetadata();
//        objectMetadata.setContentDisposition("attachment");
        objectMetadata.setContentType("image/jpeg");
        objectMetadata.setServerSideEncryption(SSEAlgorithm.AES256.getAlgorithm());

        // 上传的流如果能够获取准确的流长度，则推荐一定填写 content-length
        // 如果确实没办法获取到，则下面这行可以省略，但同时高级接口也没办法使用分块上传了
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, objectMetadata);

        // 设置存储类型（如有需要，不需要请忽略此行代码）, 默认是标准(Standard), 低频(standard_ia)
        // 更多存储类型请参见 https://cloud.tencent.com/document/product/436/33417
        putObjectRequest.setStorageClass(StorageClass.Standard_IA);

        try {
            // 高级接口会返回一个异步结果Upload
            // 可同步地调用 waitForUploadResult 方法等待上传完成，成功返回UploadResult, 失败抛出异常
            Upload upload = transferManager.upload(putObjectRequest);
            UploadResult uploadResult = upload.waitForUploadResult();
            return tsCosClient.getCosClient().getObjectUrl(bucketName, key).toString();
        } catch (InterruptedException | CosClientException e) {
            throw new CustomizeException(UPLOAD_FAILURE);
        }

    }
}