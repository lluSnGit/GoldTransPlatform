# 返点计算功能说明

## 功能概述

本功能实现了根据用户ID、贵金属类型和克重来计算并分配返点的功能。

## 业务流程

1. **查询用户费率**：根据用户ID查询用户等级，然后根据用户等级和贵金属类型查询对应的费率
2. **查询上级费率**：查询用户的直接上级和上上级的费率
3. **计算返点**：
   - 上级返点 = 用户费率 - 上级费率
   - 上上级返点 = 上级费率 - 上上级费率
4. **分配返点**：将计算出的返点金额分别充值到上级和上上级用户的账户余额中

## API接口

### 1. 计算返点（不分配）

**POST** `/api/rebate-calculation/calculate`

请求体：
```json
{
    "userId": "用户ID",
    "category": 0,
    "weight": 10.5
}
```

响应：
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "userId": "用户ID",
        "userLevel": 1,
        "userFeeRate": 0.05,
        "parentUserId": "上级用户ID",
        "parentUserLevel": 2,
        "parentFeeRate": 0.04,
        "parentRebateAmount": 10.50,
        "grandParentUserId": "上上级用户ID",
        "grandParentUserLevel": 3,
        "grandParentFeeRate": 0.03,
        "grandParentRebateAmount": 10.50,
        "category": 0,
        "weight": 10.5
    }
}
```

### 2. 计算并分配返点

**POST** `/api/rebate-calculation/calculate-and-distribute`

请求体：
```json
{
    "userId": "用户ID",
    "category": 0,
    "weight": 10.5
}
```

功能：计算返点并自动分配到上级和上上级用户账户

### 3. 简单计算返点（GET请求）

**GET** `/api/rebate-calculation/calculate-simple?userId=用户ID&category=0&weight=10.5`

### 4. 测试接口

**GET** `/api/rebate-calculation/test`

## 贵金属类型说明

- `0`: 黄金
- `1`: 铂金  
- `2`: 钯金

## 计算规则

1. **上级返点计算**：
   - 返点 = (用户费率 - 上级费率) × 克重
   - 只有当用户费率 > 上级费率时才计算返点

2. **上上级返点计算**：
   - 返点 = (上级费率 - 上上级费率) × 克重
   - 只有当上级费率 > 上上级费率时才计算返点

## 注意事项

1. 用户必须存在且有关联关系
2. 各用户等级必须有对应的费率配置
3. 返点金额会自动四舍五入到2位小数
4. 分配返点时会自动记录到账户交易记录中

## 错误处理

- 用户不存在：返回错误信息
- 费率配置不存在：返回错误信息
- 用户关系不存在：正常返回，但不计算返点
- 上级用户不存在：正常返回，但不计算上级返点
- 上上级用户不存在：正常返回，但不计算上上级返点 
