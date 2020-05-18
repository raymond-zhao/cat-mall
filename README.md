# 分布式基础篇-全栈开发

这是一个跟随 尚硅谷《谷粒商城》- 2020版课程学习开发的分布式电商项目，主要分为三个阶段。

- 第一阶段：分布式基础篇-全栈开发
  - 快速地开发一个前后端分离的电商系统
  - Spring Boot + Spring Cloud + Vue + Docker + MyBatis Plus
- 第二阶段：分布式高级-微服务架构
  - 打通分布式开发中的所有技术栈
  - 实现一整套的微服务整合，包括秒杀，结算，库存...
- 第三阶段：高可用集群-架构师提升
  - 搭建 Kubernetes 集群，实现全流程 DevOps

![谷粒商城-微服务架构图](https://tva1.sinaimg.cn/large/007S8ZIlly1geblwvpadsj31f10u07dn.jpg)

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
- 公共依赖: `common`
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
spring.application.name: catmall-coupon # 微服务名
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
@FeignClient("catmall-coupon") # 微服务名
public interface CouponFeign {
    @GetMapping("/coupon/coupon/member/list")
    R memberList();
}

// 主启动类
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
spring.application.name=catmall-coupon
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

## 使用逆向工程前后端代码

### 自定义调整

- 放权`src/util/index.js`

```javascript
export function isAuth (key) {
  // return JSON.parse(sessionStorage.getItem('permissions') || '[]').indexOf(key) !== -1 || false
  return true
}
```



- 可选关闭`eslint `，路径`build/webpack.base.conf.js`

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

- 表格--自定义列模板

### OSS对象存储

- 开通服务，设置子账户，给子账户授权。

- [Spring Cloud Alibaba OSS](https://help.aliyun.com/document_detail/91868.html?spm=a2c4g.11186623.2.15.17706e28EQIQWR#concept-ahk-rfz-2fb)

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alicloud-oss</artifactId>
</dependency>
```

- [OSS获取服务器签名](https://help.aliyun.com/document_detail/91868.html?spm=a2c4g.11186623.2.15.57276e2888qoXF#concept-ahk-rfz-2fb)

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
        for (int val : value) {
            set.add(val);
        }
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

`SPU: Standard Product Unit` （标准产品单位）

 SPU是商品信息聚合的最小单位，是一组可复用、易检索的标准化信息的集合，该集合描述了一个产品的特性。通俗点讲，属性值、特性相同的商品就可以称为一个SPU。
 例如：`iPhone 11`就是一个SPU，与商家，与颜色、款式、套餐都无关。

`SKU: Stock Keeping Unit`(库存量单位) SKU即库存进出计量的单位， 可以是以件、盒、托盘等为单位。
 SKU是物理上不可分割的最小存货单元。在使用时要根据不同业态，不同管理模式来处理。在服装、鞋类商品中使用最多最普遍。
 例如：`iPhone 11`的颜色(深空灰等)，存储容量(64GB 256GB)

# 分布式高级篇-微服务架构

## ElasticSearch

Docker

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

本机

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

### 倒排索引

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
  "name": John Snow
}
```

### 整合Spring Boot

[客户端](https://www.elastic.co/guide/en/elasticsearch/client/index.html)

### kibana 创建sku索引

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

## 商城业务-首页渲染

### 整合Thymeleaf

- 关闭缓存`spring.thymeleaf.cache=false`
- 静态资源都放在`static`文件夹下就可以按照路径直接访问
- 页面都在`templates`下直接访问
- `SpringBoot`访问项目时会默认寻找`index.html`

## Nginx域名配置

```shell
Docroot is: /usr/local/var/www

The default port has been set in /usr/local/etc/nginx/nginx.conf to 8080 so that
nginx can run without sudo.

nginx will load all files in /usr/local/etc/nginx/servers/.

To have launchd start nginx now and restart at login:
  brew services start nginx
Or, if you don't want/need a background service you can just run:
  nginx
```

- `Nginx`代理给网关的时候，会丢失请求的`host`信息,手动设置`proxy_set_header Host $host`

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

```shell
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

缓存逻辑：先查缓存，缓存有则直接返回，缓存无则查数据库，然后将数据库的查询结果放入缓存以便下次使用。

### Redis基本使用

用于缓存商品分类数据

- 堆外内存溢出 `OutOfDirectMemoryError`

```java
/**
* 利用Redis进行缓存商品分类数据
*
* @return
*/
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

在每一个微服务中的`synchronized(this)`加锁的对象只是当前实例，但是并未对其他微服务的实例产生影响，即使每个微服务加锁后只允许一个请求，加入有8个微服务，仍然会有8个线程存在。

#### 锁-时序问题

**确认缓存-查询数据库-结果放入缓存** 这三个操作必须当做一个事务来执行，放在同一把锁里面完成。

### Redis实现分布式锁🔐

- Redis 实现分布式锁的关键
  - 原子添加 `Boolean lockResult = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);`
  - 原子删除

```java
// lua 脚本
String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Collections.singletonList("lock"), uuid);
```

## Redisson

[Redisson-GitHub Wiki](https://github.com/redisson/redisson/wiki/目录)

### 看门狗

```java
RLock lock = redissonClient.getLock("my-lock");
lock,lock();
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
- 只要占锁成功，就会启动一个定时任务【**重新给锁设置过期时间，新的过期时间就是看门狗的默认时间，每隔10s自动续期成30s**】， internalLockLeaseTime[看门狗时间/3 = 10s]

### 缓存数据一致性

- 双写模式：修改数据后从数据库再查一遍放入缓存
  - 脏数据问题：部分脏数据，缓存过期后又能得到最新的正确数据
- 失效模式：修改数据后删除缓存，等待下一次请求到来时再重新查询后放入缓存
- 解决： `canal`
  - 使用 `canal`更新缓存
  - 使用 `canal`解决数据易购
- 本系统的一致性解决方案
  - 为所有缓存数据设置过期时间，数据过期下一次查询触发主动更新。
  - 读写数据的时候，加上分布式的读写锁。(读多写少时几乎无影响)

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

### 初始化线程的四种方式

### 线程池

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

