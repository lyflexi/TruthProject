分布式黑马商城
reference：https://b11et3un53m.feishu.cn/wiki/NNAtw4CFQijiYakX8tgczWvWn0b
# 技术背景

- nacos
- seata

# 环境
- java11
- mysql 8.0.27
- springboot 2.7.12
- spring-cloud 2021.0.3
- spring-cloud-alibaba 2021.0.4.0

# 导入hmall工程

- Windows下启动后端服务
访问http://localhost:8080/hi

- Windows下启动nginx，比Linux还方便，nginx-hmall里面已经打包好了前端工程，并且配置好了nginx.conf
```shell
# 启动nginx
start nginx.exe
# 停止
nginx.exe -s stop
# 重新加载配置
nginx.exe -s reload
# 重启
nginx.exe -s restart
```
访问http://localhost:18080

## 登录业务
登录入口在com.hmall.controller.UserController中的login方法：
![img.png](01.登录业务.png)

## 搜索业务
该页面会调用接口：/search/list，对应的服务端入口在com.hmall.controller.SearchController中的search方法：
![img.png](02.搜索业务.png)

## 购物车业务

在搜索到的商品列表中，点击按钮加入购物车，即可将商品加入购物车：
![img.png](03.购物车业务.png)
同时这里还可以对购物车实现修改、删除等操作。
相关功能全部在com.hmall.controller.CartController中

其中，查询购物车列表时，由于要判断商品最新的价格和状态，所以还需要查询商品信息，业务流程如下：
![img.png](03.购物车业务-时序图.png)

## 下单业务
在购物车页面点击结算按钮，会进入订单结算页面：
![img.png](04.下单业务.png)
点击提交订单，会提交请求到服务端，服务端做3件事情：
- 创建一个新的订单
- 扣减商品库存
- 清理购物车中商品
  
业务入口在com.hmall.controller.OrderController中的createOrder方法

## 支付业务
下单完成后会跳转到支付页面，目前只支持余额支付：
![img.png](05.支付业务.png)
在选择余额支付这种方式后，会发起请求到服务端，服务端会立刻创建一个支付流水单，并返回支付流水单号到前端。
当用户输入用户密码，然后点击确认支付时，页面会发送请求到服务端，而服务端会做几件事情：
- 校验用户密码
- 扣减余额
- 修改支付流水状态
- 修改交易订单状态
  
请求入口在com.hmall.controller.PayController中

# 服务拆分
我们在做服务拆分时一般有两种方式：
- 纵向拆分。就是按照项目的功能模块来拆分。例如黑马商城中，就有用户管理功能、订单管理功能、购物车功能、商品管理功能、支付功能等。那么按照功能模块将他们拆分为一个个服务，就属于纵向拆分。这种拆分模式可以尽可能提高服务的内聚性。

- 横向拆分 。就是看各个功能模块之间有没有公共的业务部分，如果有将其抽取出来作为通用服务。这样可以避免重复开发，避免多处修改。
  - 例如用户登录以及下单业务都需要发送消息通知，和记录风控数据。因此消息发送、风控数据记录就是通用的业务功能，可以将他们分别抽取为公共服务：消息中心服务、风控管理服务。
  - 购物车服务和下单业务都需要执行feign远程调用查询商品价格，因此需要将feign远程调用客户端抽取为公共模块

将来的每一个微服务都会有自己的一个database，服务拆分后的sql脚本见sql目录，这为后面的分布式事务做铺垫
# 商品服务item-service

- 拆分出item-service并启动，访问swagger接口文档：http://localhost:8081/doc.html
- 拆分出cart-service并启动，访问swagger文档页面：http://localhost:8082/doc.html
  - 引入RestTemplate方式远程服务调用：cart-service远程访问item-service
  - 引入服务注册中心nacos，记得导入nacos的sql脚本，访问http://192.168.18.100:8848/nacos/#/login
  - item-service引入服务注册发现依赖spring-cloud-starter-alibaba-nacos-discovery，注册item-service的多实例副本进行测试，拷贝Application，配置vm options:-Dserver.port=8083避免端口冲突
  - cart-service引入服务注册发现依赖spring-cloud-starter-alibaba-nacos-discovery
  - cart-service引入feign，超级简单的方式远程调用item-service
  - cart-service引入feign连接池，feign-okhttp
- 拆分出hm-api专门创建所有的feignclient
- 拆分出user-service并启动，访问swagger接口文档：http://localhost:8084/doc.html#/home
- 拆分出trade-service并启动，访问swagger接口文档：http://localhost:8085/doc.html
- 拆分出pay-service并启动，访问swagger接口文档：http://localhost:8086/doc.html
