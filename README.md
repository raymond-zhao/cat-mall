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



