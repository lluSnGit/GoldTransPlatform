package com.mall.controller;

import com.mall.common.Result;
import com.mall.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/minio")
@Tag(name = "文件管理接口", description = "文件上传操作")
@Slf4j
@RequiredArgsConstructor
public class MinioController {
    
    private final FileService fileService;
    
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    @Operation(summary = "上传文件")
    public Result<String> uploadFile(
            @Parameter(description = "文件") @RequestPart("file") MultipartFile file) {
        String fileUrl = fileService.uploadFile(file);
        return Result.success(fileUrl);
    }
} 