import { createApp } from 'vue'
import App from './App.vue'
import router from '@/router'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css'
import {VueMasonryPlugin} from "vue-masonry";
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
NProgress.configure({
    showSpinner: false,
})

const app = createApp(App)
app.use(router).use(Antd).use(VueMasonryPlugin).use(NProgress).mount('#app')

app.config.globalProperties.NProgress = NProgress;

import * as Icons from "@ant-design/icons-vue";
const icons = Icons;
for(const i in icons){
    app.component(i,icons[i]);
}

import * as pub from "@/utils/pub.js";
app.config.globalProperties.$pub = pub;

var deviceName = navigator.userAgent.toLowerCase();
if (/android|iphone|webos|ipod|blackberry/.test(deviceName)) {
    app.config.globalProperties.maxTagCount = 2;
    app.config.globalProperties.waterfallWidth = 175;
    app.config.globalProperties.waterfallStyle = {
        overflow:'hidden',margin: '0px auto',width: '376px'
    };
    app.config.globalProperties.width = {
        width: '100%'
    };
    app.config.globalProperties.videoWidthHeighut = {
        width: '300px',
        height:'200px'
    }
}else{
    app.config.globalProperties.maxTagCount = 5;
    app.config.globalProperties.waterfallWidth = 372;
    app.config.globalProperties.waterfallStyle = {
        overflow:'hidden',margin: '0px auto',width: '1537px'
    };
    app.config.globalProperties.width = {};
    app.config.globalProperties.videoWidthHeighut = {
        width: '880px',
        height:'500px'
    }
}


if (window.console && window.console.log) {
    const e = (...e) => setTimeout(console.log.bind(console, ...e));
    e("\n %c Wall %c https://github.com/super-tongyao/wall \n", "color:#FFFFFB;background:#ffa628;padding:5px 0;border-radius:.5rem 0 0 .5rem;", "background: #efefef;padding:5px 0 5px;border-radius:0 .5rem .5rem 0;")
    e(`  当前程序使用Wall搭建，作者：https://ityao.cn`);
    e(`\n\n\n\n\n`);
}
