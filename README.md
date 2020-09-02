# 前言
> 做项目不是为了敲代码，而是为了学知识，学原理，不深入去理解底层原理的话就是普通的CRUD工程师。
> 项目中涉及的比较重要的内容可以查看 Wiki 页面。
>
> 就目前来看面试官喜欢问的主要还是RabbitMQ、Redis、线程池...

这是一个跟随 尚硅谷《谷粒商城》- 2020版课程学习开发的分布式电商项目，主要分为三个阶段。

- 第一阶段：[分布式基础篇-全栈开发](#分布式基础篇-全栈开发)
  - 快速地开发一个前后端分离的电商系统
  - Spring Boot + Spring Cloud + Vue + Docker + MyBatis Plus
- 第二阶段：[分布式高级-微服务架构](#分布式高级篇-微服务架构)
  - 打通分布式开发中的所有技术栈，ElasticSearch + Redis缓存与Lua脚本 + 性能压测 + Nginx动静分离、负载均衡 + 多线程与异步 + 单点登录与社交登录 + RabbitMQ消息队列 + Redisson分布式锁 + Seata分布式事务 + 定时任务与分布式调度 + Sentinel 服务容错 + Sleuth&Zipkin 链路追踪
  - 实现一整套的微服务整合，包括秒杀，结算，库存...
- 第三阶段：[高可用集群-架构师提升](#高可用集群篇-架构师提升)
  - 搭建 Kubernetes 集群，实现全流程 DevOps。
  - 搭建MySQL集群，Redis集群，RabbitMQ集群，ElasticSearch集群。

![谷粒商城-微服务架构图](https://tva1.sinaimg.cn/large/007S8ZIlly1geblwvpadsj31f10u07dn.jpg)

- [x] 《分布式基础篇-全栈开发》
- [x] 《分布式高级篇-微服务架构》
- [ ] 《高可用集群篇-架构师提升》
- [ ] 完善系统功能
  - [ ] 完善用户 评论、收藏、物流
  - [x] 系统自动生成了`Apache Shiro`权限控制
  - [ ] 增加卖家角色及相关功能
  - [ ] 增加推荐子系统
  - [ ] 增加数据仓库与数据挖掘

> 前两部分都已经基本结束了，剩下的就是修修补补，闲下来的时候添加点新功能，但是因为要准备秋招，所以大概率是秋招后才会进行大规模修改。
>
> 《高可用集群篇-架构师提升》因为系统配置已经跟不下动了，因为内存严重不足，考虑过在阿里云买几台服务器，但还是决定等到开学以后在学校的主机上折腾一遍，现在只进行理论学习。
>
> ---
>
> 这个项目在我本机是可以运行的，但是直接 `clone` 的话是不能直接运行的，因为很多资源我是配置在本地的，而不是配在服务器上的，需要运行的话至少需要以下几个条件。
>
> - 数据库基础表与数据库连接信息
> - `Redis`服务器
> - `ElasticSearch`服务器及相应的索引
> - `Nginx`服务器及相关页面的静态资源
> - `nacos server`用于服务注册与发现，以及服务配置。
> - 阿里云`OSS`对象存储(主要用于图片的显示，密钥配置在了`nacos server`对外不可见)
> - `OAuth2.0`(社交登录)
> - 最重要的是拥有一定的基础，可以自己调整运行过程中的各种问题。

# 分布式基础篇-全栈开发

## 基础环境

### CentOS虚拟机

- 购买云服务器
- 本地虚拟机

### Docker环境

- 安装 MySQL

```shell
$ docker pull mysql:5.7
$ docker run -p 3306:3306 --name mysql \
-v /mydata/mysql/log:/var/log/mysql \
-v /mydata/mysql/data:/var/lib/mysql \
-v /mydata/mysql/conf:/etc/mysql \
-e MYSQL_ROOT_PASSWORD=root \
-d mysql:5.7

$ docker ps
```

- 配置MySQL编码


```
[client]
default-character-set=utf8

[mysql]
default-character-set=utf8

[mysqld]
init_connect='SET collation_connection = utf8_unicode_ci'
init_connect='SET NAMES utf8'
character-set-server=utf8
collation-server=utf8_unicode_ci
skip-character-set-client-handshake
skip-name-resolve
```

```shell
$ docker restart mysql
$ docker exec -it mysql /bin/bash
```

- 安装 Redis

```shell
$ docker pull redis
$ mkdir -p /mydata/redis/conf
$ touch /mydata/redis/conf/redis.conf
$ docker run -p 6379:6379 --name redis -v /mydata/redis/data:/data \
-v /mydata/redis/conf/redis.conf:/etc/redis/redis.conf \
-d redis redis-server /etc/redis/redis.conf
$ docker ps
$ docker run --restart=always # 随机自启
$ docker update --restart=always <CONTAINER ID> # 随机自启
$ docker exec -it redis redis-cli
```

- 增加 Redis 持久化

```shell
$ vi /mydata/redis/conf/redis.conf
# appendonly yes
$ docker restart redis
```

- 免费的 mac/windows redis 客户端

[AnotherRedisDesktopManager - GitHub](https://github.com/qishibo/AnotherRedisDesktopManager/releases)

## 开发环境

### 微服务模块

- 项目基础模块: `Product/Ware/Member/Coupon/Order`
- 公共依赖: `commons`
- 后台管理模块: `renren-fast`

### 初始化数据库

### 逆向工程

[人人开源主页 - 码云](https://gitee.com/renrenio/)

[人人开源后台管理 - 码云](https://gitee.com/renrenio/renren-fast)

[人人开源前台Vue - 码云](https://gitee.com/renrenio/renren-fast-vue)

[人人开源代码生成器 - 码云](https://gitee.com/renrenio/renren-generator)

### Maven

```xml
<!-- 阿里云镜像 -->
<mirror>
    <id>aliyunmaven</id>
    <mirrorOf>*</mirrorOf>
    <name>阿里云公共仓库</name>
    <url>https://maven.aliyun.com/repository/public</url>
</mirror>
<mirror>
    <id>nexus-aliyun</id>
    <mirrorOf>central</mirrorOf>
    <name>Nexus Aliyun</name>
    <url>https://maven.aliyun.com/nexus/content/groups/public</url>
</mirror>
<mirror>
    <id>aliyunmaven</id>
    <mirrorOf>*</mirrorOf>
    <name>阿里云Google仓库</name>
    <url>https://maven.aliyun.com/repository/google</url>
</mirror>
<mirror>
    <id>aliyunmaven</id>
    <mirrorOf>*</mirrorOf>
    <name>阿里云Apache仓库</name>
    <url>https://maven.aliyun.com/repository/apache-snapshots</url>
</mirror>
<mirror>
    <id>aliyunmaven</id>
    <mirrorOf>*</mirrorOf>
    <name>阿里云Spring仓库</name>
    <url>https://maven.aliyun.com/repository/spring</url>
</mirror>
<mirror>
    <id>aliyunmaven</id>
    <mirrorOf>*</mirrorOf>
    <name>阿里云Spring插件仓库</name>
    <url>https://maven.aliyun.com/repository/spring-plugin</url>
</mirror>
```

```xml
<!-- 编译环境 -->
<profile>
    <id>jdk-1.8</id>
    <activation>
        <activeByDefault>true</activeByDefault>
        <jdk>1.8</jdk>
    </activation>
    <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
    </properties>
</profile>
```

### 修改NPM源

```shell
$ npm config set registry http://registry.npm.taobao.org/
$ npm config get registry
$ npm config set registry https://registry.npmjs.org/
```

### 视频16出现的坑 node-sass

```
Module build failed: Error: Node Sass does not yet support your current environment: OS X 64-bit with Unsupported runtime (72)
```

解决办法：

```shell
$ npm uninstall node-sass
$ npm i node-sass --sass_binary_site=https://npm.taobao.org/mirrors/node-sass/
$ npm run dev # 此时可成功
```

## 生成基本CRUD代码

利用逆向工程，运行`renren-generator`项目连接数据库后迅速生成所有后端基础增删改查代码以及前段`Vue`页面。

## Spring Cloud Alibaba

- [Spring Cloud Alibaba - GitHub](https://github.com/alibaba/spring-cloud-alibaba)

- `Spring Cloud Alibaba Nacos`: 注册中心(服务发现/注册)，配置中心(动态配置管理)
- `Spring Cloud Ribbon`: 负载均衡
- `Spring Cloud OpenFeign`: 声明式 `HTTP` 客户端，远程服务调用。
- `Spring Cloud Alibaba Sentinel`: 服务容错(限流、降级、熔断)
- `Spring Cloud Sleuth`: 调用链监控
- `Spring Cloud Alibaba Seata`: 分布式事务解决方案

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-alibaba-dependencies</artifactId>
            <version>2.2.1.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### Nacos服务注册与发现

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

```properties
spring.application.name: mall-coupon # 微服务名
spring.cloud.nacos.discovery.server-addr: localhost:8848 # 注册地址
```

```java
// 主启动类
@EnableDiscoveryClient
```

### OpenFeign使用

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

```java
// 编写接口
@FeignClient("mall-coupon") // 微服务名
public interface CouponFeign {
    @GetMapping("/coupon/coupon/member/list")  // 全限定路径
    R memberList();
}

// 主启动类 basePackages 可加可不加
@EnableFeignClients(basePackages = "edu.dlut.catmall.member.feign")
```

### Nacos配置中心

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```

```properties
# bootstrap.properties 启动优先级高于
spring.application.name=mall-coupon
spring.cloud.nacos.config.server-addr=localhost:8848
```

```java
// Controller 动态刷新
@RefreshScope
```

在`nacos`配置中心添加配置文件 `servicename.properties`

- 命名空间、配置集、配置集ID、配置分组

### Spring Cloud Gateway

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```

## 三级菜单

下次遇到再需要生成菜单的业务逻辑，这个基本上就可以直接拿来使用了。

### 业务逻辑层

```mysql
CREATE TABLE `pms_category` (
  `cat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `name` char(50) DEFAULT NULL COMMENT '分类名称',
  `parent_cid` bigint(20) DEFAULT NULL COMMENT '父分类id',
  `cat_level` int(11) DEFAULT NULL COMMENT '层级',
  `show_status` tinyint(4) DEFAULT NULL COMMENT '是否显示[0-不显示，1显示]',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `icon` char(255) DEFAULT NULL COMMENT '图标地址',
  `product_unit` char(50) DEFAULT NULL COMMENT '计量单位',
  `product_count` int(11) DEFAULT NULL COMMENT '商品数量',
  PRIMARY KEY (`cat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1433 DEFAULT CHARSET=utf8mb4 COMMENT='商品三级分类';
```

```java
@Data
@TableName("pms_category")
public class CategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 分类id
	 */
	@TableId
	private Long catId;
	/**
	 * 分类名称
	 */
	private String name;
	/**
	 * 父分类id
	 */
	private Long parentCid;
	/**
	 * 层级
	 */
	private Integer catLevel;
	/**
	 * 是否显示[0-不显示，1显示]
	 */
	private Integer showStatus;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 图标地址
	 */
	private String icon;
	/**
	 * 计量单位
	 */
	private String productUnit;
	/**
	 * 商品数量
	 */
	private Integer productCount;

	@TableField(exist = false)
	private List<CategoryEntity> children;

}
```

```java
public List<CategoryEntity> listWithTree() {
    // 这个类继承了 ServiceImpl
    // 1. 查出所有分类列表
    List<CategoryEntity> entities = baseMapper.selectList(null); // 传入 null 代表查询所有

    // 2. 组装成树形结构
    List<CategoryEntity> levelMenu = entities.stream()
        .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
        .map(menu -> {
            menu.setChildren(getChildren(menu, entities));
            return menu;
        }).sorted((m1, m2) -> m1.getSort() == null ? 0 : m1.getSort() - (m2.getSort() == null ? 0 : m2.getSort())).collect(Collectors.toList());
    return levelMenu;
}

/**
* 递归查找所有菜单的子菜单
*
* @param root
* @param all
* @return
*/
private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
    List<CategoryEntity> children = all.stream()
        .filter(categoryEntity -> categoryEntity.getParentCid() == root.getCatId())
        .map(categoryEntity -> {
            categoryEntity.setChildren(getChildren(categoryEntity, all));
            return categoryEntity;
        })
        .sorted((m1, m2) -> m1.getSort() == null ? 0 : m1.getSort() - (m2.getSort() == null ? 0 : m2.getSort()))
        .collect(Collectors.toList());
    return children;
}
```

### 跨域问题

除了添加这个，还要把`renren-fast`自带的跨域配置给关闭掉。

```java
@Configuration
public class CORSConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(source);
    }
}
```

### MyBatis Plus 逻辑删除

## 使用逆向工程生成前后端代码

### 自定义调整

- 放权`src/util/index.js`

```javascript
export function isAuth (key) {
  // return JSON.parse(sessionStorage.getItem('permissions') || '[]').indexOf(key) !== -1 || false
  return true
}
```

- 可选关闭`eslint `，路径`build/webpack.base.conf.js`，实际上是必关的。

```javascript
const createLintingRule = () => ({
  test: /\.(js|vue)$/,
  loader: 'eslint-loader',
  enforce: 'pre',
  include: [resolve('src'), resolve('test')],
  options: {
    formatter: require('eslint-friendly-formatter'),
    emitWarning: !config.dev.showEslintErrorsInOverlay
  }
})
```

### OSS对象存储

- 开通服务，设置子账户，给子账户授权，注意要把账号密码配置在`nacos-server`上，要不然总会收到`GitHub`与阿里云发送的微信提醒。
- [Spring Cloud Alibaba OSS](https://help.aliyun.com/document_detail/91868.html?spm=a2c4g.11186623.2.15.17706e28EQIQWR#concept-ahk-rfz-2fb)
- [OSS获取服务器签名](https://help.aliyun.com/document_detail/91868.html?spm=a2c4g.11186623.2.15.57276e2888qoXF#concept-ahk-rfz-2fb)

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alicloud-oss</artifactId>
</dependency>
```

### 数据验证

- [前端表单验证-自定义验证](https://element.eleme.cn/#/zh-CN/component/form)
- 后端`JSR303`校验
  - 添加校验注解  `javax.validation.constraints`，定义自己的校验规则。
  - `Controller @Valid`，校验的`Bean`之后添加`BindingResult`可以获得校验结果。
  - 编写异常处理类，`@RestControllerAdvice`，使用`ExceptionHandler`标注方法可以处理的异常。
- 统一异常处理类
- `JSR`分组校验
  - 创建标记接口`public interface UpdateGroup{},public interface AddGroup{}`
  - 注解分组`@NotBlank(message = "品牌名不能为空", groups = {AddGroup.class, UpdateGroup.class})`
  - `Controller`添加`@Validated({UpdateGroup.class})`
  - 默认没有指定分组的校验注解，在分组校验情况下不生效。
- 自定义注解校验
  - 编写一个自定义的校验注解
  - 编写一个自定义的校验器 `ConstraintValidator`
  - 关联自定义的校验器和自定义的校验注解

```java
@Documented
@Constraint(validatedBy = { ListValueConstraintValidator.class})
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface ListValue {

    String message() default "{edu.dlut.common.valid.ListValue.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    int[] value() default { };

}
```

```java
public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {

    private Set<Integer> set = new HashSet<>();
    //初始化方法
    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] value = constraintAnnotation.value();
        for (int val : value)
            set.add(val);
    }

    /**
     *
     * @param value 需要校验的值
     * @param context
     * @return
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return set.contains(value);
    }
}
```

```properties
# ValidationMessages.properties
edu.dlut.common.valid.ListValue.message=必须提交指定的值
```

```java
@ListValue(value = {0, 1}, groups = {AddGroup.class, UpdateStatusGroup.class})
```

## SKU与SPU

> 这两个名词将会贯穿从此开始到高级篇结束的所有内容。

`SPU: Standard Product Unit` （标准产品单位）

 SPU是商品信息聚合的最小单位，是一组可复用、易检索的标准化信息的集合，该集合描述了一个产品的特性。通俗点讲，属性值、特性相同的商品就可以称为一个SPU。
 例如：`iPhone 11`就是一个SPU，与商家，与颜色、款式、套餐都无关。

`SKU: Stock Keeping Unit`(库存量单位) SKU即库存进出计量的单位， 可以是以件、盒、托盘等为单位。
 SKU是物理上不可分割的最小存货单元。在使用时要根据不同业态，不同管理模式来处理。在服装、鞋类商品中使用最多最普遍。
 例如：`iPhone 11`的颜色(深空灰等)，存储容量(64GB 256GB)

# 分布式高级篇-微服务架构

## ElasticSearch

`Docker`安装

```shell
$ docker pull elasticsearch:7.4.2 # 存储和检索数据
$ dock pull kibana:7.4.2 # 可视化检索数据
```

```shell
$ mkdir -p /mydata/elasticsearch/config
$ mkdir -p /mydata/elasticsearch/data
$ echo "http.host:0.0.0.0" >> /mydata/elasticsearch/config/elasticsearch.yml
$ docker run --name elasticsearch -p 9200:9200 \
-e "discovery.type=single-node" \
-e ES_JAVA_OPTS="-Xms128m -Xmx128m" \
-v /mydata/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
-v /mydata/elasticsearch/data:/usr/share/elasticsearch/data \
-v /mydata/elasticsearch/plugins:/usr/share/elasticsearch/plugins \
-d elasticsearch:7.4.2
$ chmod -R 777 /mydata/elasticsearch
```

```shell
$ docker run --name kibana -e ELASTICSEARCH_HOSTS=http://xxx.xx.xx.xxx:9200 -p 5601:5601 -d kibana:7.4.2
# 其中IP地址一定要改为自己机器或服务器的IP
```

`Homebrew`安装

```shell
$ brew tap elastic/tap
$ brew install elastic/tap/elasticsearch-full
$ elasticsearch
$ brew services start elastic/tap/elasticsearch-full # 开机自启 可选
$ brew install kibana/tap/kibana-full
$ kibana
$ brew services start elastic/tap/kibana-full # 开机自启 可选

# ik 分词 
$ /usr/local/var/elasticsearch/plugins/ik/config
```

### 倒排索引(面试重点)

### 学习手册

[ElasticSearch Documentation](https://www.elastic.co/guide/index.html)

#### _cat

```json
GET/_cat/nodes
GET/_cat/health
GET/_cat/master
GET/_cat/indices // 查看所有索引
```

#### 索引文档

```json
// 保存一条数据 保存在哪个索引的哪个类型下 指定用哪一个标识
PUT customer/external/1 // PUT 和 POST 均可 PUT必须带ID，POST可带可不带
{
  "name": "John Snow"
}
```

### 整合Spring Boot

[客户端](https://www.elastic.co/guide/en/elasticsearch/client/index.html)

### kibana 创建sku索引

> 此索引后来会出现问题，下面有修改。

```json
PUT product
{
  "mappings": {
    "properties": {
      "skuId": {
        "type": "long"
      },
      "spuId": {
        "type": "keyword"
      },
      "skuTitle": {
        "type": "text",
        "analyzer": "ik_smart"
      },
      "skuPrice": {
        "type": "keyword"
      },
      "skuImg": {
        "type": "keyword",
        "index": false,
        "doc_values": false
      },
      "saleCount": {
        "type": "long"
      },
      "hasStock": {
        "type": "boolean"
      },
      "hotScore": {
        "type": "long"
      },
      "brandId": {
        "type": "long"
      },
      "catalogId": {
        "type": "long"
      },
      "catalogName": {
        "type": "keyword",
        "index": false,
        "doc_values": false
      },
      "brandName": {
        "type": "keyword",
        "index": false,
        "doc_values": false
      },
      "brandImg": {
        "type": "keyword",
        "index": false,
        "doc_values": false
      },
      "attrs": {
        "type": "nested",
        "properties": {
          "attrId": {
            "type": "long"
          },
          "attrName": {
            "type": "keyword",
            "index": false,
            "doc_values": false
          },
          "attrValue": {
            "type": "keyword"
          }
        }
      }
    }
  }
}

// 执行结果
{
  "acknowledged" : true,
  "shards_acknowledged" : true,
  "index" : "product"
}
```

### Feign调用流程

- 视频135
- Feign 底层实现原理：动态代理。

```
/**
 * Feign 调用流程
 * 1. 构造请求数据 将对象转为 JSON
 *      RequestTemplate 
 * 2. 发送请求进行之星(执行成功会解码相应数据)
 *     executeAndDecode(template)
 * 3. 执行请求会有重试机制
 */
```

## 首页渲染

### 整合Thymeleaf

- 关闭缓存`spring.thymeleaf.cache=false`
- 静态资源都放在`static`文件夹下就可以按照路径直接访问
- 页面都在`templates`下直接访问
- `SpringBoot`访问项目时会默认寻找`index.html`

## Nginx域名配置

`Homebrew`操作

```shell
# 查看 nginx 相关信息
$ brew info nginx
```

- `Nginx`代理给网关的时候，会丢失请求的`host`信息，手动设置`proxy_set_header Host $host`

```
#user  nobody;
worker_processes  1;

#pid        logs/nginx.pid;


events {
    worker_connections  1024;
}


http {
    upstream catmall{
       server 127.0.0.1:8888;
    }

    server {
        listen       80;
        server_name  catmall.com;

        location / {
	        proxy_set_header HOST $host;
	        proxy_pass http://catmall;
            #root   html;
            #index  index.html index.htm;
        }
    }

    include servers/*;
}
```

## 性能压测

### 基本概念

- HPS(Hits Per Second): 每秒点击次数
- TPS(Transaction Per Second)
- QPS(Query Per Scond)
- 最大响应时间
- 最少响应时间
- 90%响应时间

### JVM

- `jconsole`
- `jvisualvm` 安装插件 `visualgc`
- [visualvm插件更新地址](https://visualvm.github.io/pluginscenters.html)

### Apache JMeter

[NON_GUI](https://jmeter.apache.org/usermanual/get-started.html#non_gui)

```
================================================================================
Don't use GUI mode for load testing !, only for Test creation and Test debugging.
For load testing, use CLI Mode (was NON GUI):
   jmeter -n -t [jmx file] -l [results file] -e -o [Path to web report folder]
& increase Java Heap to meet your test requirements:
   Modify current env variable HEAP="-Xms1g -Xmx1g -XX:MaxMetaspaceSize=256m" in the jmeter batch file
Check : https://jmeter.apache.org/usermanual/best-practices.html
================================================================================
```

```shell
$ jmeter -n -t testplan/RedisLock.jmx -l testplan/result/result.txt -e -o testplan/webreport
```

```shell
-n
This specifies JMeter is to run in cli mode
-t
[name of JMX file that contains the Test Plan].
-l
[name of JTL file to log sample results to].
-j
[name of JMeter run log file].
-r
Run the test in the servers specified by the JMeter property "remote_hosts"
-R
[list of remote servers] Run the test in the specified remote servers
-g
[path to CSV file] generate report dashboard only
-e
generate report dashboard after load test
-o
output folder where to generate the report dashboard after load test. Folder must not exist or be empty
The script also lets you specify the optional firewall/proxy server information:
-H
[proxy server hostname or ip address]
-P
[proxy server port]
```

### Nginx动静分离

> [本项目可能问到的Nginx面试题](https://github.com/raymond-zhao/cat-mall/wiki/Nginx)

- 将项目中`static/`下的静态资源移动到`nginx`服务器中，`mac`为`/usr/local/var/www`

- 替换`index.html`中的文件路径
- 配置`nginx`
- 重载配置`nginx -s reload`

```
// 在server块中添加
location /static/ {
    root /usr/local/var/www;
}
```

### 压测优化

- JVM
- 索引
- 逻辑优化

## Redis缓存

> 相关问题已整理至 [Wiki 页面]([https://github.com/raymond-zhao/cat-mall/wiki/%E7%BC%93%E5%AD%98](https://github.com/raymond-zhao/cat-mall/wiki/缓存))，面试必备。

### Redis基本使用

用于缓存商品分类数据

- 堆外内存(直接内存)溢出 `OutOfDirectMemoryError`

```java
@Override
public Map<String, List<Catelog2VO>> getCatalogJson() {
    // TODO 产生堆外内存溢出 OutOfDirectMemoryError
    /**
     * 1. SpringBoot2.0之后默认使用 lettuce 作为操作 redis 的客户端，lettuce 使用 Netty 进行网络通信
     * 2. lettuce 的 bug 导致 Netty 堆外内存溢出 -Xmx300m   Netty 如果没有指定对外内存 默认使用 JVM 设置的参数
     *      可以通过 -Dio.netty.maxDirectMemory 设置堆外内存
     * 解决方案：不能仅仅使用 -Dio.netty.maxDirectMemory 去调大堆外内存
     *      1. 升级 lettuce 客户端   2. 切换使用 jedis
     *
     *      RedisTemplate 对 lettuce 与 jedis 均进行了封装 所以直接使用 详情见：RedisAutoConfiguration 类
     */
    // 给缓存中放入JSON字符串，取出JSON字符串还需要逆转为能用的对象类型

    // 1. 加入缓存逻辑， 缓存中存的数据是 JSON 字符串
    String catalogJSON = stringRedisTemplate.opsForValue().get("catalogJSON");
    if (StringUtils.isEmpty(catalogJSON)) {
        // 2 如果缓存未命中 则查询数据库
        Map<String, List<Catelog2VO>> catalogJsonFromDB = getCatalogJsonFromDB();
        // 3 查到的数据再放入缓存 将对象转为JSON放入缓存
        String cache = JSON.toJSONString(catalogJsonFromDB);
        stringRedisTemplate.opsForValue().set("catalogJSON", cache);

        // 4 返回从数据库中查询的数据
        return catalogJsonFromDB;
    }

    Map<String, List<Catelog2VO>> result = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2VO>>>() {});
    return result;
}
```

### 缓存击穿、穿透、雪崩

|   类型   | 描述                                                         | 解决                                                         |
| :------: | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 缓存击穿 | 对于一些设置了过期时间的 key，如果这些 key 可能会在某些时间点被超高并发地访问，是一种非常“热点”的数据。如果这个 key 在大量请求同时进来前正好失效，那么所有对这个 key 的数据査询都落到db. | 加锁。大量并发只让一个去查，其他人等待，査到以后释放锁，其他人获取到锁，先查缓存，就会有数据，不用去 db |
| 缓存穿透 | 指查询一个一定不存在的数据，由于缓存是不命中，将去查询数据库，但是数据库也无此记录，我们没有将这次查询的null写入缓存，这将导致这个不存在的数据每次请求都要到存储层去査询，失去了缓存的意义。利用不存在的数据进行攻击，数据库瞬时压力增大，最终导致崩溃 | nul 结果缓存，并加入短暂过期时间                             |
| 缓存雪崩 | 缓存雪崩是指在我们设置缓存时 key 采用了相同的过期时间，导致缓存在某一时刻同时失效，请求全部转发到 DB, DB 瞬时压力过重雪崩。 | 原有的失效时间基础上增加一个随机值，比如 1-5 分钟随机，这样每一个缓存的过期时间的重复率就会降低，就很难引发集体失效的事件。 |

### 缓存数据一致性

- 双写模式：修改数据后(写到数据库)从数据库再查一遍放入缓存(写到缓存)
  - 脏数据问题：部分脏数据，缓存过期后又能得到最新的正确数据
- 失效模式：修改数据后删除缓存，等待下一次请求到来时再重新查询后放入缓存
- 解决： `canal`
  - 使用 `canal`更新缓存
  - 使用 `canal`解决数据易购
- 本系统的一致性解决方案
  - 为所有缓存数据设置过期时间，数据过期下一次查询触发主动更新。
  - 读写数据的时候，加上分布式的读写锁。(读多写少时几乎无影响)

### 分布式锁

```java
/**
* SpringBoot 所有的组件在容器中默认都是单例的，使用 synchronized (this) 可以实现加锁
*
* 得到锁之后 应该再去缓存中确定一次，如果没有的话才需要继续查询
*
* 假如有100W个并发请求，首先得到锁的请求开始查询，此时其他的请求将会排队等待锁
* 等到获得锁的时候再去执行查询，但是此时有可能前一个加锁的请求已经查询成功并且将结果添加到了缓存中
*/
```

![分布式锁下如何加锁](https://tva1.sinaimg.cn/large/007S8ZIlly1get5dp2mdsj31hp0u0tfq.jpg)

在每一个微服务中的`synchronized(this)`加锁的对象只是当前实例，但是并未对其他微服务的实例产生影响，即使每个微服务加锁后只允许一个请求，假如有 8 个微服务，仍然会有 8 个线程存在。

#### 锁-时序问题

**确认缓存-查询数据库-结果放入缓存** 这三个操作必须当做一个事务来执行，放在同一把锁里面完成。

### Redis实现分布式锁🔐

## Redisson

[Redisson-GitHub Wiki](https://github.com/redisson/redisson/wiki/目录)

### 看门狗

```java
RLock lock = redissonClient.getLock("my-lock");
lock.lock();
```

- 阻塞式等待，默认加的锁都是 30s 时间
- 锁的自动续期，如果业务超长，如果业务运行时间较长，运行期间自动给锁续上新的 30s，不用担心业务时间过长(大于锁的过期时间)导致锁被删掉。
- 加锁的业务只要运行完成就不会给当前锁续期，即使不手动解锁，锁也会在 30s 后自动删除。

```java
lock.lock(10, TimeUnit.SECONDS);
```

- 在锁时间到了以后，不会自动续期。
- 如果我们传递了锁的超时时间，就发送给 redis 执行脚本，进行占锁，默认超时就是我们指定的时间
- 如果我们未指定锁的超时时间，就使用 30*1000[**Lockwatchdog Timeout 看门狗的默认时间**]
- 只要占锁成功，就会启动一个定时任务【**重新给锁设置过期时间，新的过期时间就是看门狗的默认时间，每隔10s自动续期成30s**】， `internalLockLeaseTime`[看门狗时间/3 = 10s]

## Spring Cache

[Spring Cache Documentation](https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/integration.html#cache)

### 常用注解

- `@Cacheable`: Triggers cache population.
- `@CacheEvict`: Triggers cache eviction. 失效模式下
- `@CachePut`: Updates the cache without interfering with the method execution.
- `@Caching`: Regroups multiple cache operations to be applied on a method.
- `@CacheConfig`: Shares some common cache-related settings at class-level.

### 重点类

- `CacheManager`
- `Cache`

### 默认行为(`@Cacheable({"category"})`)

- 如果命中缓存，方法不再被调用。
- `key`默认自动生成`category::SimpleKey []` 
  - 自定义接收SpEL：`@Cacheable(value = {"category"}, key= "'name'")`
  - `@Cacheable(value = {"category"}, key = "#root.method.name")`
- 缓存的`value`的值，默认使用`jdk`序列化机制，将序列化后的数据存到`redis`
  - 保存为`JSON`格式原理
  - `CacheAutoConfiguration` -> `RedisCacheConfiguration` -> 自动配置了`RedisCacheManager` -> 初始化所有的缓存 -> 每个缓存决定用什么配置 -> 如果`redisCacheConfiguration`有就用已有的，没有就用默认配置 -> 想改缓存配置，只需要给容器中存放一个`RedisCacheConfiguration`即可 -> 就会应用到当前`RedisCacheManager`管理的所有缓存分区中。
- 默认`TTL=-1`
  - `spring.cache.redis.time-to-live=3600000`
- 自定义缓存配置

```java
@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class MyCacheConfig {

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        config = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        CacheProperties.Redis redisProperties = cacheProperties.getRedis();

        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixKeysWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }
}
```

### `Spring Cache`总结

- 读模式
  - 缓存穿透：`cache-null-values=true`
  - 缓存击穿：`sync=true`
  - 缓存雪崩：`spring.cache.redis.time-to-live=时间`
- 写模式
  - 读写加锁
  - 引入 `Canal` , 感知到`MySQL`的更新则去更新缓存
  - 读多写多，直接去数据库查询
- 总结
  - 常规数据(度多写少，即时性，一致性要求不高的数据)，完全可以使用`Spring Cache`
  - 特殊数据，特殊设计。

## 检索服务

> 坑：在从首页点击分类名跳转到搜索页时，跳转链接在`catalogLoader.js`中，原静态资源链接为`http://search.gmall.com/`，需要改为自己在HOST文件中配置的域名。

### 迁移索引`mapping`

```json
// 不要直接删除重建 会丢失已上架的商品数据

PUT product
{
  "mappings": {
    "properties": {
      "skuId": {
        "type": "long"
      },
      "spuId": {
        "type": "keyword"
      },
      "skuTitle": {
        "type": "text",
        "analyzer": "ik_smart"
      },
      "skuPrice": {
        "type": "keyword"
      },
      "skuImg": {
        "type": "keyword"
      },
      "saleCount": {
        "type": "long"
      },
      "hasStock": {
        "type": "boolean"
      },
      "hotScore": {
        "type": "long"
      },
      "brandId": {
        "type": "long"
      },
      "catalogId": {
        "type": "long"
      },
      "catalogName": {
        "type": "keyword"
      },
      "brandName": {
        "type": "keyword"
      },
      "brandImg": {
        "type": "keyword"
      },
      "attrs": {
        "type": "nested",
        "properties": {
          "attrId": {
            "type": "long"
          },
          "attrName": {
            "type": "keyword"
          },
          "attrValue": {
            "type": "keyword"
          }
        }
      }
    }
  }
}

PUT mall_product

POST _reindex
{
    "source": {
        "index": "product"
    },
    "dest": {
        "index": "mall_product"
    }
}
```

### 构建检索请求与封装检索结果

- `MallSearchServiceImpl`

### 页面排序

- [ ] `list.html`排序样式未完成，可参考视频`187-189`

## 异步

[进程、线程与线程池](https://raymond-zhao.top/2020/07/19/2020-07-19-ProcessAndThread/)

### `CompletableFuture<T>`

#### 业务场景

- 获取SKU基本信息
- 获取SKU图片信息
- 获取SKU促销信息
- 获取SPU所有销售属性
- 获取规格参数组及组下的规格参数
- SPU详情

#### 创建异步对象

#### 计算完成时回调方法

#### `handle`方法

#### 线程串行化

#### 两任务组合

#### 多任务组合

## 商品详情页

## 认证服务

### 短信验证

- 接口防刷

### OAuth2.0之微博登录

[微博开放平台](https://open.weibo.com/)

[微博OAuth2.0文档](https://open.weibo.com/wiki/%E6%8E%88%E6%9D%83%E6%9C%BA%E5%88%B6%E8%AF%B4%E6%98%8E)

> 微博登录视频坑:
>
> 微博回调域名: `auth.catmall.com`，而不是`catmall.com`
>
> `OAuthController`: `doPost`方法后三个参数需要的是`Map`，均不能写视频中的`null`，需要传入空的`Map`，而且`Map`的顺序有变化，在构建请求条件时应该将`map`传入查询参数`querys`中，而不是请求体`bodys`
>
> `HttpResponse response = HttpUtils.doPost("https://api.weibo.com", "/oauth2/access_token", "post", new HashMap<>(), map, new HashMap<>());`

## Spring Session

[Spring Session Documentation](https://docs.spring.io/spring-session/docs/2.3.0.RELEASE/reference/html5/#introduction)

### 对象JDK序列化

### Spring Session核心原理

- `@EnableRedisHttpSession`注解中导入了`RedisHttpSessionConfiguration`类
  - 给容器中添加了一个组件：`SessionRepository` -> `RedisOperationsSessionRepository`，利用`Redis`来进行`Session`的增删改查等各种操作。
- `SessionRepositoryFilter`: `Session`存储过滤器，每个请求都必须经过`filter`
  - 创建的时候，自动从容器中获取`SessionRepository`
  - 原始的`request,response`都被包装成`SessionRepositoryRequestWrapper.SessionRepositoryResponseWrapper`
  - 使用装饰者模式进行包装

### 单点登录

## 购物车

- 用户可以在登录状态下将商品加入[在线购物车/用户购物车]
  - 放入`MongoDB`
  - 放入`MySQL`
  - 放入`Redis`(采用)，登录以后，会将临时购物车中的数据合并过来
- 用户可以在未登录状态下将商品加入[离线购物车/游客购物车]
  - 放入`localStorage`
  - 放入`Cookie`
  - 放入`WebSQL`
  - 放入`Redis`(采用)，即使浏览器关闭，临时购物车数据都在。

- 用户可以使用购物车一起结算下单
- 用户可以添加商品到购物车
- 用户可以查询自己购物车
- 用户可以选中商品
- 用户可以在购物车中修改购买的商品数量
- 用户可以在购物车中删除商品
- 在购物车中展示优惠信息
- 提示购物车商品价格变化

> 京东给每个用户生成一个值类似于UUID的`user-key`，有效期一个月，存储在`Cookie`，浏览器保存以后，每次访问都会带上这个`cookie`。
>
> 登录后：`session`有用户信息
>
> 未登录：`cookie`中的`user-key`
>
> 第一次使用时，如果没有临时用户，帮忙创建一个临时用户。

### ThreadLocal用户身份鉴别

- `public class CartInterceptor implements HandlerInterceptor{}`重写`preHandle, postHandle` ，不用加`@Component`
- 添加`MallWebConfig`

```java
@Configuration
public class MallWebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CartInterceptor()).addPathPatterns("/**");
    }
}
```

## 消息队列-RabbitMQ(面试超高频)

本系统消息队列工作图

![本系统消息队列工作图](https://tva1.sinaimg.cn/large/007S8ZIlly1ghtsf6mboij30yl0t7afn.jpg)

### 应用场景

- 异步处理
- 应用解耦
- 流量控制(削峰、填谷)

### 类型

- 队列(点对点)
- 订阅

### 执行标准

- JMS
- AMQP

### 安装

- Docker
  - 4369,25672: Erlang 发现 & 集群端口
  - 5672,5671: AMQP端口
  - 15672: Web管理后台端口
  - 1883,8883: MQTT协议端口
  - 61613, 61614: STOMP协议端口

```shell
$ docker pull rabbitmq
$ docker run -d --name rabbitmq -p 5671:5671 -p 5672:5672 -p 4369:4369 -p 25672:25672 -p 15671:15671 -p 15672:15672 rabbitmq:management
```

- 手动下载安装

```shell
$ cd 安装目录
$ # 启用rabbitmq management插件
$ sudo sbin/rabbitmq-plugins enable rabbitmq_management
$ # 配置环境变量(可选)
$ rabbitmq-server -detached # 后台启动
$ rabbitmqctl status # 查看状态 浏览器内输入 http://localhost:15672,默认的用户名密码都是guest
$ rabbitmqctl stop # 关闭
```

```
# Setting for RabbitMQ
export RABBIT_HOME=/Users/raymond/Documents/GitHub/rabbitmq_server-3.8.3
export PATH=$PATH:$RABBIT_HOME/sbin
```

### 发送消息

- `@RabbitListener`
- `@RabbitHandler`

如果是传输对象的话，传输的对象必须实现序列化接口，默认的序列化方式是JDK序列化，但是也可以手动指定序列化的方式。

```java
@Configuration
public class MyRabbitConfig {
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
```

### 可靠投递

## 订单服务

### 基本环境搭建

`Nginx`静态资源，网关等。

### 登录拦截

### Feign远程调用丢失请求头问题

`Feign`在远程调用之前要构造请求，此时会丢失请求头`headers`，`request`中包含许多拦截器。

在构建新请求的时候需要吧“老请求”中的数据获取并保存传递到新请求中。

```java
@Configuration
public class MallFeignConfig {
    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                String cookie = requestAttributes.getRequest().getHeader("Cookie");
                template.header("Cookie", cookie);
            }
        };
    }
}
```

### Feign异步编排丢失请求头问题

- 原因：因为`RequestContextHolder`中的`ThreadLocal`只在当前线程可用，线程间独立，而在异步编排时会创建不同的线程执行任务，`ThreadLocal`中的数据将会丢失。
- 解决办法：在异步编排前首先获取`RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();`，然后在异步任务开始前重新设置进去，`RequestContextHolder.setRequestAttributes(requestAttributes);`

### 接口幂等性

在确认页点击 **提交订单** 时，用户可能不小心点击多次，所以即使用户点击次数大于1次，也应该保证只提交一次。

- 接口幂等性：保证用户对统一操作发起的一次请求或多次请求的结果时一致的。

#### 应用情况

- 用户多次点击按钮
- 用户页面回退后再次提交
- 微服务相互调用，由于网络问题导致请求失败，触发`feign`重试机制
- 其他业务情况

#### 幂等性解决方案

- Token机制
  - `Redis Lua`脚本
- 各种锁机制
  - 数据库悲观锁、乐观锁
  - 业务层分布式锁
- 各种唯一性约束
  - 数据库唯一性约束
  - `redis set`防重
  - 防重表
  - 全局请求唯一ID

#### 下单流程

```
下单 创建订单 验证令牌 核算价格 锁定库存
```

### 分布式事务

![image-20200525153506445](https://tva1.sinaimg.cn/large/007S8ZIlly1gf4qgbev2nj31jx0u049h.jpg)

- CAP定理
  - C: 一致性，在分布式系统中的所有数据备份，在同一时刻是否有同样的值。
  - A: 可用性，再急群众一部分结点故障后，集群整体是否还能响应客户端的读写请求。
  - P: 分区容错性，大多数分布式系统都分布在多个子网络，每个子网络就叫做一个区，分区容错的意思是，区间通信可能失败。
  - CAP定理指的是以上三点至多只能同时保证两点，不能三者兼顾，一般来说在分布式系统中P不可避免，所以一个系统至多只能包租CP或AP。
- [Raft定理动画](http://thesecretlivesofdata.com/raft/)
- BASE定理
  - 选择AP，舍弃实现C(强一致性)，选择实现弱一致性，保证实现最终一致性。
  - 基本可用
  - 软状态
  - 最终一致性

### 事务传播

- 本地事务失效问题
  - 同一个对象内事务互调默认失败，原因是绕过了代理对象，而事务是通过代理对象来控制的。
- 解决方法
  - 使用代理对象来调用事务方法，引入`spring-boot-starter-aop`，`aop`又引入了`aspectj`
  - `@EnableAspectJAutoProxy(exposeProxy = true)`，开启`aspectj`动态代理功能，如果不开启的话，默认使用的是`JDKProxy`，开启后以后创建对象采用`aspectj`动态代理(即使没有接口也可以创建代理对象, JDKProxy要求被代理的对象有接口定义)
  - 本类事务互相调用此时可以实现`AopContext.currentProxy`

### 解决方案

- 2PC(2 phase commit, 二阶段提交)模式
- 柔性事务-TCC事务补偿性方案
  - 刚性事务：遵循ACID
  - 柔性事务：遵循BASE
- 柔性事务-最大努力通知型方案
- 柔性事务-可靠消息+最终一致性(异步确保型)

## Seata

> 持续启动失败，选择放弃。
>
> `no available service 'null' found, please make sure registry config correct`

```mysql
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```

## 延时队列

### Dead Letter Exchanges(DLX) - 死信路由

### 消息积压、重复、丢失等问题解决方案

- 消息丢失
  - 消息发送出去，由于网络问题没有抵达服务器
    - 做好容错方法(`try-catch`)，发送消息可能会网络失败，失败后要有重试机制，可记录到系统数据库，采用定期扫描重发的方式。
    - 做好日志记录，每个消息状态是否都被服务器收到都应该被记录
    - 做好定期重发，如果消息没有发送成功，定期去数据库扫描未成功的消息进行重发
  - 消息抵达`Broker`，`Broker`要将消息写入磁盘才算成功，此时`Broker`尚未持久化完成，宕机。
    - `publisher`必须加入确认回调机制，确认成功的消息，修改数据库消息状态
  - 自从`ACK`状态下，消费者收到消息，但没来得及消费便宕机
    - 一定开启手动`ACK`，消息成功才移除，失败或者没来得及处理就`noACK`并重新入队。
- 消息重复
  - 消息消费成功，事务已经提交，`ack`时，机器宕机，导致没有`ack`成功，`Broker`的消息重新由`unack-> ready`，并发送给其他消费者。
  - 消息消费失败，由于重试机制，自动又将消息发送出去。
  - 成功消费，`ack`时宕机，消息由`unack`变为`ready`，`Broker`又重新发送
    - 消费者的业务消费接口应该设计成幂等性的，比如扣库存工作单的状态标志
    - 使用 **防重表(redis, mysql)** ，发送消息每一个都有业务的唯一标识，处理过就不用再处理。
    - `RabbitMQ`的每一个消息都有`redelivered`字段，可以获取消息是否是被重新投递的。
- 消息积压
  - 消费者宕机积压
  - 消费者消费能力不足积压
  - 发送者发送流量太大
    - 上线更多的消费者，进行正常消费。
    - 上线专门的队列消费服务，将消息先批量取出来，记录数据库，离线慢慢处理。

```mysql
create table `mq_message` (
    `message_id` char(32) not null,
    `content` text,
    `to_exchange` varchar(255) default null,
    `routing_key` varchar(255) default null,
    `class_type` varchar(255) default null,
    `message_status` int(1) default '0' comment '0-新建 1-已发送 2-错误抵达 3-已抵达',
    `create_time` datetime default null,
    `update_time` datetime default null,
    primary key (`message_id`)
) engine InnoDB default charset=utf8mb4
```

## 支付宝支付

[支付宝开放平台](https://open.alipay.com/platform/home.htm)

### 加密与解密

- 对称加密
- 非对称加密
- 公钥、私钥、签名、验签

### 沙箱环境

[文档-沙箱环境](https://opendocs.alipay.com/open/200/105311)

[RSA生成器](https://opendocs.alipay.com/open/291/106097/)

### 内网穿透

### 支付宝异步通知

## 秒杀业务

![秒杀(高并发)系统关注的问题](https://tva1.sinaimg.cn/large/007S8ZIlly1ghtt8gm4nuj30l50ff413.jpg)

秒杀业务具有瞬间高并发的特点，必须要做限流+异步+缓存(页面静态化)+独立部署

限流方式:

- 前端限流: 一些高并发的网站直接在前端页面开始限流。
- `nginx`限流: 直接负载部分请求到错误的静态页面，令牌算法，漏斗算法。
- 网关限流: 限流的过滤器
- 代码中使用分布式信号量
- `RabbitMQ`限流，保证发挥所有服务器的性能。

### CRON表达式

### 定时与异步

- 定时任务
  - `@EnableScheduling`开启定时任务
  - `@Scheduled`开启一个定时任务
- 异步任务
  - `@EnableAsync`开启异步任务
  - `@Async` 标注在需要异步执行的方法上

### 幂等性保证

- 随机码

> 查看总的代码行，包括添加了多少行，删除了多少行，现在总共多少行。

```shell
$ git log  --pretty=tformat: --numstat | awk '{ add += $1; subs += $2; loc += $1 - $2 } END { printf "added lines: %s, removed lines: %s, total lines: %s\n", add, subs, loc }' -
```

## Sentinel流控熔断降级

[Sentinel Wiki - 中文](https://github.com/alibaba/Sentinel/wiki/%E4%BB%8B%E7%BB%8D)

| Sentinel       | Hystrix                                        |                               |
| -------------- | ---------------------------------------------- | ----------------------------- |
| 隔离策略       | 信号量隔离                                     | 线程池隔离/信号量隔离         |
| 熔断降级策略   | 基于响应时间或失败比率                         | 基于失败比率                  |
| 实时指标实现   | 滑动窗口                                       | 滑动窗口（基于 RxJava）       |
| 规则配置       | 支持多种数据源                                 | 支持多种数据源                |
| 扩展性         | 多个扩展点                                     | 插件的形式                    |
| 基于注解的支持 | 支持                                           | 支持                          |
| 限流           | 基于 QPS，支持基于调用关系的限流               | 有限的支持                    |
| 流量整形       | 支持慢启动、匀速器模式                         | 不支持                        |
| 系统负载保护   | 支持                                           | 不支持                        |
| 控制台         | 开箱即用，可配置规则、查看秒级监控、机器发现等 | 不完善                        |
| 常见框架的适配 | Servlet、Spring Cloud、Dubbo、gRPC 等          | Servlet、Spring Cloud Netflix |

## Sleuth+Zipkin链路追踪

[Zipkin](https://zipkin.io/pages/quickstart.html)

```shell
# docker
$ docker run -d -p 9411:9411 openzipkin/zipkin

# java
$ curl -sSL https://zipkin.io/quickstart.sh | bash -s
$ java -jar zipkin.jar
```

# 高可用集群篇-架构师提升

## Kubernetes

[Kubernetes-中文文档](https://kubernetes.io/zh/)

### 环境准备

- 进入三个虚拟机，开启`root`的密码访问权限

```shell
$ vargrant ssh xxxxx
$ su root # password vargrant
$ vi /etc/ssh/sshd_config
# 修改 PasswordAuthentication yes
$ service sshd restart
# 所有虚拟机设置为 4 core 4G
```

- 设置`linux`环境(三个结点都要执行)

```shell
# 关闭防火墙
$ systemctl stop firewalld
$ systemctl disable firewalld
# 关闭 selinux
$ sed -i 's/enforcing/disabled/' /etc/selinux/config
# 关闭内存交换
$ swapoff -a # 临时
$ sed -ri 's/.*swap.*/#&/' /etc/fstab # 永久
$ free -g # 验证 swap 必须为 0
```

- 添加主机名与IP映射

```shell
$ vi /etc/hosts
# 前边为网卡地址 后边为集群结点名
xxxxxxx  k8s-node1
xxxxxxx  k8s-node2
xxxxxxx  k8s-node3
```

- 将桥接的IPv4流量传递到iptables链

```shell
cat > /etc/sysctl.d/k8s.conf << EOF
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF
sysctl --system
```

### 安装docker

- 卸载系统之前到docker

```shell
$ sudo yum remove docker docker-client docker-client-latest docker-common docker-latest docker-latest-logrotate docker-logrotate docker-engine
```

- 安装Docker-CE

```shell
$ sudo yum install -y yum-utils device-mapper-persistent-data lvm2
# 设置 docker repo 到 yum 位置
$ sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
# 安装 docker docker-cli
$ sudo yum install -y docker-ce docker-ce-cli containerd.io
```

- docker加速

```shell
$ sudo mkdir -p /etc/docker
$ sudo tee /etc/docker/daemon.json << -'EOF'
{
	"registry-mirrors": [阿里云是个不错的选择]
}
EOF
$ sudo systemctl daemon-reload
$ sudo systemctl restart docker
$ sudo systemctl enable docker # 开机自启
```

## MySQL集群

### 集群类型

- 主从式
  - 主从复制，同步方式。
  - 主从调度，控制方式。
- 分片式
  - 数据分片存储，片区之间备份。
- 选主式
  - 出现容灾时选主，调度时选主。

### Docker安装MySQL-一主两从

> 感觉哪里少了东西

```shell
$ docker run -p 3306:3306 --name mysql-master \
-v /mydata/mysql/master/log:/var/log/mysql \
-v /mydata/mysql/master/data:/var/lib/mysql \
-v /mydata/mysql/master/conf:/etc/mysql \
-e MYSQL_ROOT_PASSWORD=root \
-d mysql:5.7

$ docker run -p 3306:3306 --name mysql-slave-1 \
-v /mydata/mysql/slave/log:/var/log/mysql \
-v /mydata/mysql/slave/data:/var/lib/mysql \
-v /mydata/mysql/slave/conf:/etc/mysql \
-e MYSQL_ROOT_PASSWORD=root \
-d mysql:5.7

$ docker run -p 3306:3306 --name mysql-slave-2 \
-v /mydata/mysql/slave/log:/var/log/mysql \
-v /mydata/mysql/slave/data:/var/lib/mysql \
-v /mydata/mysql/slave/conf:/etc/mysql \
-e MYSQL_ROOT_PASSWORD=root \
-d mysql:5.7
```

```shell
$ vi /mydata/mysql/master/conf/my.cnf
```

```
[client]
default-character-set=utf8

[mysql]
default-character-set=utf8

[mysqld]
init_connect='SET collation_connection = utf8_unicode_ci'
init_connect='SET NAMES utf8'
character-set-server=utf8
collation-server=utf8_unicode_ci
skip-character-set-client-handshake
skip-name-resolve

# 主从的这个配置仅在于 id 的不同
server_id=1
log-bin=mysql-bin
read-only=0
binlog-do-db=catmall_ums
binlog-do-db=catmall_pms
binlog-do-db=catmall_oms
binlog-do-db=catmall_sms
binlog-do-db=catmall_wms
binlog-do-db=catmall_admin

replicate-ignore-db=mysql
replicate-ignore-db=sys
replicate-ignore-db=information_schema
replicate-ignore-db=performance_schema
```

- 为master授权用户来同步数据

```shell
# 进入 master 容器
$ docker exec -it mysql /bin/bash
$ mysql -uroot -p
mysql> grant all priviledges on *.* to 'root'@'%' identified by 'root' with grant option;
mysql> flush priviledges;
mysql> GRANT REPLICATION SLAVE ON *.* to 'backup'@'%' identified by '123456';
show master status
```

- 设置主库连接

```shell
change master to master_host='xxxxxxx', matser_user='backup', master_password='123456', master_log_file='mysql-bin.000001', master_log_pos=0, master_port=3307;
# 启动主从同步
start slave
# 查看从库状态
show slave status
```

### [Sharding-Sphere](https://shardingsphere.apache.org/document/legacy/4.x/document/cn/overview/)

### Redis集群-三主三从

```shell
for port in $(seq 7001 7006) \
do \
mkdir -p /mydata/redis/node-${port}/conf
touch /mydata/redis/node-${port}/conf/redis.conf
cat << EOF >/mydata/redis/node-${port}/conf/redis.conf
port ${port}
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 5000
cluster-announce-ip xxxxxx
cluster-announce-port ${port}
cluster-announce-bus-port ${port}
appendonly yes
EOF
docker run -p ${port}:${port} -p 1${port}:1${port} --name redis-${port} \
-v /mydata/redis/node-${port}/data:data \
-v /mydata/redis/node-${port}/conf/redis.conf:/etc/redis/redis.conf \
-d redis:5.0.7 redis-server /etc/redis/redis.conf
done
```

```shell
$ docker stop ${docker ps -a | grep redis-700 | awk '{print $1}'}
$ docker rm ${docker ps -a | grep redis-700 | awk '{print $1}'}
```

- 使用 Redis 建立集群

```shell
$ docker exec -it redis-7001 bash
$ redis-cli --cluster create 127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005 127.0.0.1:7006 --cluster-replicas 1
```

## ElasticSearch集群

## RabbitMQ集群

## Kubernetes部署

## 流水线