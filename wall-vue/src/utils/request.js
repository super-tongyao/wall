import axios from 'axios'
import {message} from "ant-design-vue";
import { clearLocalAll, getLocal, removeLocal, setLocal } from "@/utils/cache";
import NProgress from "nprogress";

const instance = axios.create({
    baseURL: "/api",
})

//请求前拦截器
instance.interceptors.request.use(
    function(config) {
        config.headers.access_token = getLocal("access_token");
        return config;
    },
    function(err) {
        return Promise.reject(err);
    }
);

const tishi = 0;
var that = this;
//全局请求后拦截器，没有做http状态码和返回状态码统一。
instance.interceptors.response.use(
    function(response) {
        // 200
        // console.log("response:",response.data)
        return response;
    },
    function(err) {
        if(err.response.data.code >= 500 || err.response.data.code == 404){
            message.warning(err.response.data.message);
        }else if(err.response.data.code == 401){
            if((getLocal("user") != null && getLocal("user") != "")){
                removeLocal("user")
                removeLocal("access_token")
                message.warning("登录已过期，1秒后自动刷新页面。");
                setTimeout(function () {
                    location.reload();
                },1000)
            }else{
                message.warning(err.response.data.message);
            }
        }
        NProgress.done()
        return Promise.reject(err);
    }
);

export function get(url) {
    return instance.get(url).then((res)=>{
        return res.data;
    });
}
export function post(url,data) {
    return instance.post(url, data).then((res)=>{
        return res.data;
    });
}
export function remove(url,data) {
    return instance.delete(url, data).then((res)=>{
        return res.data;
    });
}
