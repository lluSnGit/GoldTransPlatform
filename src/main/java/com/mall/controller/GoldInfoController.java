package com.mall.controller;

import com.mall.common.Result;
import com.mall.dto.GoldInfoDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/gold")
public class GoldInfoController {
    
    /**
     * 更新黄金信息
     */
    @PutMapping("/{orderId}")
    public Result<Void> updateGoldInfo(
            @PathVariable Long orderId,
            @RequestBody @Valid GoldInfoDTO dto) {
        // TODO: 实现更新黄金信息逻辑
        return Result.success();
    }
    
    /**
     * 上传黄金图片
     */
    @PostMapping("/{orderId}/images")
    public Result<List<String>> uploadGoldImages(
            @PathVariable Long orderId,
            @RequestParam("files") MultipartFile[] files) {
        // TODO: 实现上传图片逻辑
        return Result.success(null);
    }
} 