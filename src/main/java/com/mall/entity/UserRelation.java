package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@TableName("user_relation")
@Schema(description = "用户关系实体")
public class UserRelation {
    @TableId(type = IdType.AUTO)
    @Schema(description = "关系ID")
    private Long id;
    
    @Schema(description = "用户ID")
    private String userId;
    
    @Schema(description = "父级ID")
    private String parentId;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
} 