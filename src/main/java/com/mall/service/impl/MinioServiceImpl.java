package com.mall.service.impl;

import com.mall.service.MinioService;
import io.minio.*;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MinioServiceImpl implements MinioService {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucketName}")
    private String defaultBucketName;

    private MinioClient getMinioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Override
    public String uploadFile(MultipartFile file, String bucketName) throws Exception {
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = defaultBucketName;
        }

        String fileName = generateFileName(file.getOriginalFilename());
        MinioClient minioClient = getMinioClient();

        // 检查存储桶是否存在，不存在则创建
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        // 上传文件
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());

        // 返回文件访问URL，使用域名而不是IP
        return String.format("%s/%s/%s", endpoint, bucketName, fileName);
    }

    @Override
    public List<Map<String, String>> uploadFiles(List<MultipartFile> files, String bucketName) throws Exception {
        List<Map<String, String>> results = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = uploadFile(file, bucketName);
            Map<String, String> result = new HashMap<>();
            result.put("url", url);
            result.put("fileName", file.getOriginalFilename());
            results.add(result);
        }
        return results;
    }

    @Override
    public void downloadFile(String fileUrl, String bucketName, HttpServletResponse response) throws Exception {
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = defaultBucketName;
        }

        // 从URL中解析出对象名称
        URL url = new URL(fileUrl);
        String path = url.getPath();
        String objectName = path.substring(path.lastIndexOf('/') + 1);
        
        MinioClient minioClient = getMinioClient();

        try (InputStream inputStream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build())) {

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode(objectName, StandardCharsets.UTF_8.toString()));

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, bytesRead);
            }
            response.getOutputStream().flush();
        }
    }

    @Override
    public void deleteFile(String fileUrl, String bucketName) throws Exception {
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = defaultBucketName;
        }

        // 从URL中解析出对象名称
        URL url = new URL(fileUrl);
        String path = url.getPath();
        String objectName = path.substring(path.lastIndexOf('/') + 1);
        
        MinioClient minioClient = getMinioClient();

        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }

    @Override
    public List<Map<String, String>> listFiles(String bucketName, String prefix) throws Exception {
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = defaultBucketName;
        }

        MinioClient minioClient = getMinioClient();
        List<Map<String, String>> files = new ArrayList<>();

        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .prefix(prefix)
                        .recursive(true)
                        .build());

        for (Result<Item> result : results) {
            Item item = result.get();
            Map<String, String> fileInfo = new HashMap<>();
            fileInfo.put("name", item.objectName());
            fileInfo.put("size", String.valueOf(item.size()));
            fileInfo.put("url", String.format("%s/%s/%s", endpoint, bucketName, item.objectName()));
            fileInfo.put("lastModified", item.lastModified().toString());
            files.add(fileInfo);
        }

        return files;
    }

    @Override
    public String getPresignedUrl(String bucketName, String fileUrl) {
        try {
            // 从文件URL中提取对象名称
            String objectName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            
            // 生成预签名URL，使用固定的过期时间（1小时）
            return getMinioClient().getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(1, TimeUnit.HOURS)  // 固定1小时过期
                    .build()
            );
        } catch (Exception e) {
            log.error("获取预签名URL失败", e);
            throw new RuntimeException("获取预签名URL失败", e);
        }
    }

    private String generateFileName(String originalFilename) {
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        return UUID.randomUUID().toString().replace("-", "") + extension;
    }
} 