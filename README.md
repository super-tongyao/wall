<div align="center">

![](https://img-blog.csdnimg.cn/1842462da13147fea1c48f8c38fc6125.png)
<h3 align="center"> Wall</h3>

![Vue](https://img.shields.io/badge/Vue-3.2.13-brightgreen.svg)
![Spring Boot 2.2.6](https://img.shields.io/badge/Spring%20Boot-2.2.6-brightgreen.svg)

</div>

Wall是一款快速分享资源应用程序。俗称“照片墙、视频墙”，基于Vue3 + Spring Boot开发的云共享资源应用系统。快速分享发布照片和视频平台。兼容PC端和移动端，支持端对端跨平台上传资源文件。

- 演示地址：[https://demo-wall.ityao.cn](https://demo-wall.ityao.cn)

- 后台管理：[https://demo-wall.ityao.cn/login](https://demo-wall.ityao.cn/login)，账号密码：admin/123456

🌟如果这个项目让你有所收获，记得 Star 关注哦，这对我是非常不错的鼓励与支持。

## 演示截图

![](https://img-blog.csdnimg.cn/d90a3605852049e59e6129b6ea356d21.png)

![](https://img-blog.csdnimg.cn/b09c6dd1af074f77ba17575a32a49f6e.png)

![](https://img-blog.csdnimg.cn/42a33e6001104fda84fd2a66955cad90.png)

![](https://img-blog.csdnimg.cn/f170a54d25f54aeaacf75aa62177f207.png)

![](https://img-blog.csdnimg.cn/da2f0ec64c97424cae4e4c57e71d4488.png)

![](https://img-blog.csdnimg.cn/f8e02fe368c04000b82f875af872681b.jpeg)

## 开发者名单

Wall还有很多不足之处，比如部分移动端机型兼容等相关问题。

或许你可以加入Wall团队，我们一起贡献代码。[申请加入](#共同协作)



下面表格中出现你的头像及GitHub账号地址，视为Wall团队成员。

| 名称          | Github                           |
| ------------- | -------------------------------- |
| Tongyao       | https://github.com/super-tongyao |
| Chen Shunhong | https://github.com/ChenShunhong  |
| 期待你的入... | 期待你的入...                    |

## 安装教程

1、下载地址：[https://github.com/super-tongyao/wall/releases](https://github.com/super-tongyao/wall/releases)，以最新版为准。

2、解压缩包，里面有两个文件夹，两个执行脚本，一个SQL脚本。

![](https://img-blog.csdnimg.cn/d41c213041d543ddacdc3b71f7a3a3ec.png)

- wall：编译好的前端页面。
- wall-service-1.0.0：编译好的后端jar程序包。
- wall.sql：后端数据库。
- startup.bat：Wall启动脚本。
- stop.bat：Wall停止脚本。

3、如果你是开发者（程序员），如只需要前端编译程序页面和后端服务jar包，那么只从如下路径中抽取文件，到你的服务器手动命令启动Wall服务。

- Wall前端页面编译文件：wall/html/wall

- Wall后端服务Jar包：wall-service/jar/wall.jar

### 程序安装


自2.0.0版本以上起，所有Wall程序采用一键启动，内置Nginx、JDK，无需再配置Nginx等相关文件操作。

1、导入```Wall.sql```文件到MySQL数据库。

```
mysql> source wall.sql
```

2、修改wall-service/config/application.yml配置文件，并修改你本地的MySQL数据库连接端口及账号密码。

```
# project prot
server:
  port: 9999

# database config
mysql:
  database: wall
  port: 3306
  ip: 127.0.0.1
  username: root
  password: root
```

3、后端服务支持两种环境下快捷启动。

- Windows：双击```startup.bat```文件启动。

- Linux：执行根目录```./startup.sh```文件启动。


在Linux下，如果你想关闭会话后继续运行Wall，那么可以搭配nohup使用。

```
nohup ./startup.sh r> /dev/null 2> /dev/null &
```

4、浏览器访问Wall程序。

浏览器输入：```http://localhost:80```，正常显示页面及操作数据，至此完成安装。

后台管理：```http://localhost:80/login```，默认账号密码：admin/123456，如有问题，请提交Issues。

### 拓展配置（可选）

主要简化Wall安装程序后的一些基本配置，如暂不需要，无需配置。

1、前端页面端口修改

默认端口为80，修改wall/config/nginx.conf配置文件。并修改你的前端端口，本文设置为8888。

```
server {
	listen       8888;
	server_name  localhost;
	
	underscores_in_headers on;

	location / {
		# 映射对应nginx/html下的wall前端程序
		root html/wall;
		try_files $uri $uri/ /index.html;
	}
	.......
}
```

2、修改后端服务端口

默认端口为9999，修改wall-service/config/nginx.conf配置文件。并修改你的前端端口，本文设置为8080。

```
# project prot
server:
  port: 8080
  
......
```

3、绑定域名

首先云服务商解析域名到本服务器IP，修改wall/config/nginx.conf配置文件，改成你的域名。更多内容请查看[Nginx官网](http://nginx.org/en/docs/)。

```
server {
	listen       80;
	server_name  你的域名地址;
	
	underscores_in_headers on;

	location / {
		# 映射对应nginx/html下的wall前端程序
		root html/wall;
		try_files $uri $uri/ /index.html;
	}
	.......
}
```

4、使用https

确保你有可用的SSL证书，修改wall/config/nginx.conf配置文件，增加如下配置。更多内容请查看[SSL配置](http://nginx.org/en/docs/http/configuring_https_servers.html)。

```
server {
	listen       80;
	server_name  你的域名地址;
	
	ssl_certificate 你的SSL域名pem证书.pem;
	ssl_certificate_key 你的SSL域名key证书.key;
	
	underscores_in_headers on;

	location / {
		# 映射对应nginx/html下的wall前端程序
		root html/wall;
		try_files $uri $uri/ /index.html;
	}
	.......
}
```

5、修改Wall上传文件大小限制

Wall默认上传大小为1024MB（1GB），修改wall/config/nginx.conf配置文件，暂改为2048（2GB）。

```
http {
	......
	
	client_max_body_size 2048m;
```

修改wall-service/config/application.yml配置文件，暂改为2048（2GB）。

```
# spring config
spring:
  ......

# file upload size
  servlet:
    multipart:
      max-file-size: 2048MB
      max-request-size: 2048MB
```

## 更新日志

#### 2023－03－14（v2.0.0）

> 1. 修复并兼容Linux操作系统。
> 1. 修复图片视频资源上传路径相关小问题。
> 1. 简化安装配置及实现一键启动Wall程序。

#### 2023－03－10（v1.0.1）

> 1. 修复上传点击标签归类时会唤起软键盘问题。
> 2. 修复上传选择图后又删除或重新选择时选不了相册问题。
> 3. 优化设置页面相应提示语。
> 4. 优化部分页面样式效果。
> 5. 优化上传选中标签时可见1个问题。

#### 2023－03－07（v1.0.0）

> 1. 初始版本发布

## 共同协作

根据下列模板，编辑修改：

- 擅长技术：你熟悉和擅长的技术。

- GitHub主页地址：你的GitHub主页地址。

- 原因理由：申请加入Wall团队原因和理由。


并发送至邮箱：super_tongyao@163.com，通过后成为Wall团队成员，让我们一起把Wall变的更好。

## 免责声明

[查看免责声明](/免责声明.md)
