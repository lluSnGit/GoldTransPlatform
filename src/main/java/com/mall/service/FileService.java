package com.mall.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    /**
     * 上传文件
     * @param file 文件
     * @return 文件访问URL
     */
    String uploadFile(MultipartFile file);
} 