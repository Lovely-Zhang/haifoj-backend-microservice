package com.haif.haifojcommon.config;

import cn.hutool.core.codec.Base64;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.haif.haifojcommon.common.ErrorCode;
import com.haif.haifojcommon.exception.BusinessException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "aliyah.oss")
public class OssConfig implements InitializingBean {

    private String endpoint;
    private String keyId;
    private String keySecret;
    private String bucketName;
    private String module;

    public static String ENDPOINT;
    public static String KEY_ID;
    public static String KEY_SECRET;
    public static String BUCKET_NAME;
    public static String MODULE;


    /**
     * 当私有成员被赋值后，此方法自动被调用，从而初始化常量
     */
    @Override
    public void afterPropertiesSet() {
        ENDPOINT = endpoint;
        KEY_ID = keyId;
        KEY_SECRET = keySecret;
        BUCKET_NAME = bucketName;
        MODULE = module;
    }

    /**
     * 创建OSSClient实例（OSS客户端实例）
     */
    private static OSS createOSSClient() {
        return new OSSClientBuilder().build(OssConfig.ENDPOINT, OssConfig.KEY_ID, OssConfig.KEY_SECRET);
    }

    /**
     * 关闭资源
     */
    private static void closeResources(OSS ossClient) {
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }

    /**
     * 文件上传
     *
     * @param file 文件流
     * @param folder 文件夹
     * @throws IOException 异常抛出
     */
    public static String upload(MultipartFile file, String folder) throws IOException {
        if (file == null) {
            throw new BusinessException(ErrorCode.FILE_NOT_EXIST_ERROR);
        }

        OSS ossClient = createOSSClient();
        // 获取选择文件的输入流
        InputStream inputStream = file.getInputStream();
        // 获取文件名称
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            originalFilename = originalFilename.replace(" ", "");
        }
        // 拼接Oss中文件的路径
        String objectName = folder + "/" + originalFilename;
        try {
            // 上传文件的同时指定进度条参数。此处PutObjectProgressListenerDemo为调用类的类名，请在实际使用时替换为相应的类名。
            ossClient.putObject(new PutObjectRequest(OssConfig.BUCKET_NAME, objectName, inputStream));
        } catch (OSSException oe) {
            throw new BusinessException(ErrorCode.OSS_EXCEPTION_ERROR);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        // 返回url路径
        return "https://" + OssConfig.BUCKET_NAME + "." + OssConfig.ENDPOINT + "/" + objectName;
    }

    /**
     * 文件下载
     *
     * @param response 响应头
     * @param objectName 文件完整路径
     */
    public static void downLoad(HttpServletResponse response, String objectName) {
        OSS ossClient = createOSSClient();
        try {
            OSSObject ossObject = ossClient.getObject(OssConfig.BUCKET_NAME, objectName);
            // 设置响应头，使得浏览器能够直接下载文件
            response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + objectName);
            response.setContentType(ossObject.getObjectMetadata().getContentType());
            IOUtils.copy(ossObject.getObjectContent(), response.getOutputStream());
        } catch (IOException e) {
            // 处理IOException异常
            throw new BusinessException(ErrorCode.OSS_DOWNLOAD_ERROR);
        } finally {
            closeResources(ossClient);
        }
    }

    /**
     * 文件删除
     *
     * @param url 文件地址
     */
    public static boolean deleteFileSucceed(String url) {
        // 判断字符串有没有经过 Base64 加密处理 
        boolean base64Encoded = Base64.isBase64(url);
        if (base64Encoded) {
            // 解密
            byte[] decode = Base64.decode(url);
            url = new String(decode);
        }

        OSS ossClient = createOSSClient();
        //文件名（服务器上的文件路径）
        String host = "https://" + OssConfig.BUCKET_NAME + "." + OssConfig.ENDPOINT + "/";
        String objectName = url.substring(host.length());
        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(OssConfig.BUCKET_NAME, objectName);
            return true;
        } catch (OSSException oe) {
            throw new BusinessException(ErrorCode.OSS_DELETE_ERROR);
        } catch (ClientException ce) {
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            closeResources(ossClient);
        }
        return false;
    }


    /**
     * 文件流上传
     * @param file 文件流
     * @param fileName 文件名
     */
    public static String uploader(MultipartFile file, String folder, String fileName) throws IOException {
        if (file == null) {
            throw new BusinessException(ErrorCode.FILE_NOT_EXIST_ERROR);
        }

        OSS ossClient = createOSSClient();
        // 获取文件的输入流
        InputStream inputStream = file.getInputStream();
        // 获取上传的文件名
        fileName = fileName.replace("\r", "");
        // 填写Object完整路径，完整路径中不能包含Bucket名称(此处为模块名 + 文件名)
        String objectName = folder + "/" + fileName;
        try {
            // 上传文件的同时指定进度条参数
            ossClient.putObject(new PutObjectRequest(OssConfig.BUCKET_NAME, objectName, inputStream));
        } catch (OSSException oe) {
            throw new BusinessException(ErrorCode.OSS_EXCEPTION_ERROR);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        // 返回url路径
        return "https://" + OssConfig.BUCKET_NAME + "." + OssConfig.ENDPOINT + "/" + objectName;
    }

}
