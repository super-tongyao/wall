const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,

  // 关闭语法检查
  lintOnSave:false,

  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:9999',
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  },
  pages: {
    index: {
      entry: 'src/main.js', // 入口文件
      title: 'Wall资源共享平台'
    }
  }
})
