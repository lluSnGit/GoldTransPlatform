package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.PriceControl;
import com.mall.service.PriceControlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/price-controls")
@Tag(name = "价格控制接口", description = "价格控制相关接口")
public class PriceControlController {
    
    @Autowired
    private PriceControlService priceControlService;
    
    @GetMapping("/type/{priceType}")
    @Operation(summary = "根据价格类型获取价格控制信息")
    public Result<PriceControl> getByPriceType(
            @Parameter(description = "价格类型(1:金价,2:铂金价)") @PathVariable Integer priceType) {
        return Result.success(priceControlService.getByPriceType(priceType));
    }
    
    @PutMapping("/type/{priceType}")
    @Operation(summary = "根据价格类型更新价格控制信息")
    public Result<Boolean> updatePriceControl(
            @Parameter(description = "价格类型(1:金价,2:铂金价)") @PathVariable Integer priceType,
            @Parameter(description = "回收价") @RequestParam BigDecimal recyclePrice,
            @Parameter(description = "出售价") @RequestParam BigDecimal sellPrice) {
        PriceControl existingControl = priceControlService.getByPriceType(priceType);
        if (existingControl == null) {
            // 如果不存在，则创建新记录
            PriceControl newControl = new PriceControl();
            newControl.setRecyclePrice(recyclePrice);
            newControl.setSellPrice(sellPrice);
            newControl.setPriceType(priceType);
            return Result.success(priceControlService.save(newControl));
        }
        
        // 如果存在，则更新记录
        PriceControl priceControl = new PriceControl();
        priceControl.setId(existingControl.getId());
        priceControl.setRecyclePrice(recyclePrice);
        priceControl.setSellPrice(sellPrice);
        priceControl.setPriceType(priceType);
        return Result.success(priceControlService.updatePriceControl(priceControl));
    }
    
    @GetMapping
    @Operation(summary = "获取所有价格控制信息")
    public Result<List<PriceControl>> getAllPriceControls() {
        return Result.success(priceControlService.getAllPriceControls());
    }
    
    @PostMapping
    @Operation(summary = "创建价格控制信息")
    public Result<Boolean> createPriceControl(
            @Parameter(description = "回收价") @RequestParam BigDecimal recyclePrice,
            @Parameter(description = "出售价") @RequestParam BigDecimal sellPrice,
            @Parameter(description = "价格类型(1:金价,2:铂金价)") @RequestParam Integer priceType) {
        PriceControl priceControl = new PriceControl();
        priceControl.setRecyclePrice(recyclePrice);
        priceControl.setSellPrice(sellPrice);
        priceControl.setPriceType(priceType);
        return Result.success(priceControlService.save(priceControl));
    }
    
    @DeleteMapping("/type/{priceType}")
    @Operation(summary = "根据价格类型删除价格控制信息")
    public Result<Boolean> deletePriceControl(
            @Parameter(description = "价格类型(1:金价,2:铂金价)") @PathVariable Integer priceType) {
        PriceControl existingControl = priceControlService.getByPriceType(priceType);
        if (existingControl == null) {
            return Result.success(false);
        }
        return Result.success(priceControlService.removeById(existingControl.getId()));
    }
} 