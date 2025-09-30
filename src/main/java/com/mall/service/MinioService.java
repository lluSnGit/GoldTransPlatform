package com.mall.service;

import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface MinioService {
    /**
     * 上传单个文件
     * @param file 文件
     * @param bucketName 存储桶名称
     * @return 文件访问URL
     */
    String uploadFile(MultipartFile file, String bucketName) throws Exception;
    
    /**
     * 批量上传文件
     * @param files 文件列表
     * @param bucketName 存储桶名称
     * @return 文件访问URL列表
     */
    List<Map<String, String>> uploadFiles(List<MultipartFile> files, String bucketName) throws Exception;
    
    /**
     * 下载文件
     * @param fileUrl 文件URL
     * @param bucketName 存储桶名称
     * @param response HTTP响应
     */
    void downloadFile(String fileUrl, String bucketName, HttpServletResponse response) throws Exception;
    
    /**
     * 删除文件
     * @param fileUrl 文件URL
     * @param bucketName 存储桶名称
     */
    void deleteFile(String fileUrl, String bucketName) throws Exception;
    
    /**
     * 获取文件列表
     * @param bucketName 存储桶名称
     * @param prefix 前缀
     * @return 文件信息列表
     */
    List<Map<String, String>> listFiles(String bucketName, String prefix) throws Exception;
    
    /**
     * 获取预签名URL
     * @param bucketName 存储桶名称
     * @param fileUrl 文件URL
     * @return 预签名URL
     */
    String getPresignedUrl(String bucketName, String fileUrl);
} 