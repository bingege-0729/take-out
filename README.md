# 校园外卖 

## 项目简介

校园外卖是一款基于 Spring Boot 的外卖点餐系统，采用前后端分离架构，实现了管理端和用户端的双端功能。项目涵盖了外卖业务的核心流程，包括菜品管理、订单处理、支付模拟、数据报表等功能模块。

## 技术栈

### 后端技术
| 技术 | 说明 |
|------|------|
| Spring Boot 2.7.3 | 基础框架 |
| MyBatis | 持久层框架 |
| MySQL | 关系型数据库 |
| Redis | 缓存 + 分布式锁 |
| WebSocket | 实时通信 |
| JWT | 身份认证 |
| Apache POI | Excel报表导出 |
| 阿里云OSS | 文件存储 |
| Knife4j | 接口文档 |

### 前端技术
- Vue.js
- Element UI
- Nginx（部署）

## 项目结构

```
sky-take-out
├── sky-common          # 公共模块（工具类、常量、异常等）
├── sky-pojo            # 实体模块（DTO、Entity、VO）
├── sky-server          # 服务模块（Controller、Service、Mapper）
│   ├── controller/
│   │   ├── admin/      # 管理端接口
│   │   └── user/       # 用户端接口
│   ├── service/        # 业务逻辑层
│   ├── mapper/         # 数据访问层
│   ├── config/         # 配置类
│   ├── aspect/         # AOP切面
│   ├── handler/        # 异常处理
│   └── websocket/      # WebSocket服务
└── nginx-1.20.2/       # 前端静态资源
```

## 功能模块

### 管理端
- **员工管理**：员工增删改查、启用禁用、登录认证
- **分类管理**：菜品分类、套餐分类管理
- **菜品管理**：菜品信息、口味管理、起售停售
- **套餐管理**：套餐信息、套餐菜品关联
- **订单管理**：订单查询、接单、拒单、派送
- **数据报表**：营业额统计、用户统计、订单统计、销量排名

### 用户端
- **用户登录**：微信登录（模拟）
- **菜品浏览**：分类浏览、菜品详情
- **购物车**：添加、删除、清空
- **订单管理**：下单、支付（模拟）、查看订单
- **地址管理**：地址增删改查

## 技术亮点

### 1. JWT 身份认证
使用 JWT 实现无状态身份认证，管理端和用户端使用不同的密钥和 Token 名称，通过拦截器校验 Token 有效性。

### 2. Redis 应用
- **缓存**：店铺营业状态缓存
- **分布式锁**：解决支付回调并发问题

### 3. AOP 公共字段自动填充
通过自定义注解 `@AutoFill` 和切面 `AutoFillAspect`，自动填充创建时间、更新时间、创建人、更新人等公共字段。

### 4. WebSocket 实时通信
实现服务端主动推送，用于来单提醒和订单状态变更通知。

### 5. 定时任务
- 超时订单自动取消（每分钟执行）
- 派送中订单自动完成（每天凌晨1点）

## 快速开始

### 环境要求
- JDK 17+
- MySQL 8.0+
- Redis 6.0+
- Maven 3.6+

### 配置说明

1. **数据库配置**
```sql
CREATE DATABASE sky_take_out;
```
导入数据库脚本（如有）

2. **修改配置文件**
复制 `application-dev.yml.example` 为 `application-dev.yml`，填入以下配置：

```yaml
sky:
  datasource:
    host: localhost
    port: 3306
    database: sky_take_out
    username: root
    password: your_password
  redis:
    host: localhost
    port: 6379
    password: your_redis_password
  alioss:
    access-key-id: your_access_key
    access-key-secret: your_access_secret
  wechat:
    appid: your_appid
    secret: your_secret
```

### 启动项目

```bash
# 进入项目目录
cd sky-take-out

# 编译项目
mvn clean install -DskipTests

# 启动服务
cd sky-server
mvn spring-boot:run
```

### 访问地址
- 后端接口：http://localhost:8080
- 接口文档：http://localhost:8080/doc.html
- 前端页面：配置 Nginx 后访问

## 运行测试

```bash
# 运行所有测试
mvn test

# 运行指定测试类
mvn test -Dtest=EmployeeServiceTest
```

## 接口文档

启动项目后访问 Knife4j 接口文档：
- 地址：http://localhost:8080/doc.html
- 管理端接口：包含员工、菜品、订单等管理接口
- 用户端接口：包含用户登录、购物车、订单等接口

## 项目亮点总结

| 亮点 | 说明 |
|------|------|
| 分层架构 | Controller-Service-Mapper 三层架构，职责清晰 |
| 参数校验 | 使用 JSR-303 注解进行参数校验 |
| 全局异常处理 | 统一异常处理，返回友好提示 |
| 分布式锁 | 解决并发问题，保证数据一致性 |
| AOP切面 | 公共字段自动填充，减少重复代码 |
| WebSocket | 实时推送订单状态 |

## 作者

本项目为学习实践项目，用于巩固 Spring Boot 开发技能。

## 许可证

MIT License
