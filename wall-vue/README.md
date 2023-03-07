# wall-vue

## 安装
```
npm install
```
## 修改地址

编辑根目录```vue.config.js```文件，修改你的后端请求地址和端口。
```
devServer: {
 proxy: {
   '/api': {
     target: 'http://localhost:9999/',
     ws: true,
     changeOrigin: true,
     pathRewrite: {
       '^/api': ''
     }
   }
 }
}
```

## 运行项目
```
npm run serve
```

## 编译项目
```
npm run build
```
## 更多配置
请看 [Configuration Reference](https://cli.vuejs.org/config/).
