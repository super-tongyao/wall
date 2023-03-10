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

## 开发者名单

Wall还有很多不足之处，比如部分移动端机型兼容等相关问题。

或许你可以加入Wall团队，我们一起贡献代码。[申请加入](#共同协作)



下面表格中出现你的头像及GitHub账号地址，视为Wall团队成员。

| 名称          | Github                           |
| ------------- | -------------------------------- |
| Tongyao       | https://github.com/super-tongyao |
| 期待你的入... | 期待你的入...                    |

## 安装教程

1、下载地址：[https://github.com/super-tongyao/wall/releases](https://github.com/super-tongyao/wall/releases)，以最新版为准。

2、解压缩包，里面有两个文件夹，一个SQL脚本。

![](https://img-blog.csdnimg.cn/02385e8546374571b45e70fab99c5333.png)

- wall：编译好的前端页面。
- wall-service-1.0.0：编译好的后端jar程序包。
- wall.sql：后端数据库。

### 前端安装

前端安装推荐、建议使用nginx代理。在此使用nginx做文章配置。

1、把wall文件夹放入nginx/html下，并编辑```config/nginx.conf```配置文件，新增如下配置。

```
server {
	listen       80;
	server_name  你的网站域名或公网IP;
	
	underscores_in_headers on;

	location / {
		# 映射你nginx/html目录下的wall文件夹
		root html/wall;
		try_files $uri $uri/ /index.html;
	}
	
	# 后端服务地址
	location /api{
		rewrite  ^/api/(.*)$ /$1 break;
		proxy_set_header Host $host;
		proxy_set_header X-Real-IP $remote_addr;
		proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
		
		# 转发你Wall的后台地址
		proxy_pass   http://localhost:9999;
	}
	
	error_page   500 502 503 504  /50x.html;
	location = /50x.html {
		root   html;
	}
}
```

2、至此，重启nginx不报错，前端部署完成。

### 后端安装

1、导入```Wall.sql```文件到MySQL数据库。

```
mysql> source wall.sql
```

2、修改config/application.yml配置文件，并修改你本地的MySQL数据库连接端口及账号密码。

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

3、后端支持两中环境下快捷启动，自带JDK，无需安装，直接启动。

- Windows下双击```startup.bat```文件启动。

- Linux下执行```startup.sh```文件启动，请先获取执行权限。

4、访问```http://你的网站域名或公网IP:80```正常显示页面及操作数据，至此完成安装。如有问题，请提交Issues。

## 更新日志

#### 2023－03－10（v1.0.1）

> 1. 修复上传点击标签归类时会唤起软键盘问题。
> 2. 修复上传选择图后又删除或重新选择时选不了相册问题。
> 3. 优化设置页面相应提示语。
> 4. 优化部分页面样式效果。
> 5. 优化上传选中标签时可见1个问题。

#### 2023－03－07（v1.0.0）

> 1. 初始版本发布

## 共同协作

叙述你的个人简介并发送至邮箱：super_tongyao@163.com，成为Wall团队成员。

让我们一起让Wall变的更好。

## 免责声明

[查看免责声明](/免责声明.md)
