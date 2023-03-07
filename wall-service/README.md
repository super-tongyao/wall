# wall-service

## 修改配置

修改```src/main/resources/application.yml```配置文件，及修改你的数据库地址及端口。
```
# project prot
server:
  port: 9999

# database config
mysql:
  database: wall
  port: 3306
  ip: localhost
  username: root
  password: root
```
## 启动项目

打开```WallApplication.java```类，右键Run运行此项目。
