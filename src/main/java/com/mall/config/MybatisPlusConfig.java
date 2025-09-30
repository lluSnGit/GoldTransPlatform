package com.mall.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    /**
     * 分页插件配置
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 创建分页插件
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        
        // 设置分页插件属性
        paginationInterceptor.setMaxLimit(1000L); // 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInterceptor.setOptimizeJoin(true); // 优化 count sql
        paginationInterceptor.setOverflow(false); // 溢出总页数后是否进行处理，默认不处理
        
        // 添加分页插件
        interceptor.addInnerInterceptor(paginationInterceptor);
        
        return interceptor;
    }
} 