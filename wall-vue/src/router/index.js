import {createRouter, createWebHashHistory, createWebHistory} from 'vue-router'
import {getLocal, removeLocal} from "@/utils/cache";

const routes = [
    {
        path: '/',
        name: 'index',
        meta:{
            title: '首页'
        },
        component: () => import('@/views/index')
    },
    {
        path: '/:tagId',
        name: 'index-type',
        meta:{
            title: '首页'
        },
        component: () => import('@/views/index')
    },
    {
        path: '/admin',
        name: 'admin',
        meta:{
            title: '加载后台'
        },
        component: () => import('@/views/admin/index'),
        children:[
            {
                path: '/admin/upload',
                name: 'upload',
                meta:{
                    title: '上传资源'
                },
                component: () => import('@/views/admin/upload'),
            },
            {
                path: '/admin/list',
                name: 'list',
                meta:{
                    title: '资源列表'
                },
                component: () => import('@/views/admin/list'),
            },
            {
                path: '/admin/tag',
                name: 'tag',
                meta:{
                    title: '标签管理'
                },
                component: () => import('@/views/admin/tag'),
            },
            {
                path: '/admin/setting',
                name: 'setting',
                meta:{
                    title: '其他设置'
                },
                component: () => import('@/views/admin/setting'),
            }
        ]
    },
    {
        path: '/login',
        name: 'login',
        meta:{
            title: '登录'
        },
        component: () => import('@/views/auth/login')
    },
    {
        path: '/:catchAll(.*)',
        meta:{
            title: '404'
        },
        component: () => import('@/views/other/404')
    }
]


const router = createRouter({
    history: createWebHistory(),  //hash
    routes
})

router.beforeEach(function (to,from,next){
    if(to.meta.title == null){
        document.title = to.params.key + ' - Wall';
    }else{
        document.title = to.meta.title + ' - Wall';
    }

    // 先看访问是不是需要登陆页面
    if(to.path.indexOf("admin") != -1){
        if(getLocal("user") != null && getLocal("user") != "") {
            // 判断默认是不是通过浏览器直接输入/admin，如果是自动重定向到上传资源页面
            if(to.path == '/admin'){
                next(to.path+"/upload")
            }
        }else{
            next("/");
        }
    }
    next()

})

router.afterEach(function() {})

export default router
