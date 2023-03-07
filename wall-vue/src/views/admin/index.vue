<template>
    <a-layout class="layout"  style="min-height: 100vh;">
        <a-layout-header style="height: 50px;line-height: 50px;padding: 0px ">
            <div class="logo">
                <a-tooltip title="返回首页" color="#1890ff" placement="bottom">
                    <router-link to="/" target="_blank" style="color: #fff">
                        WallAdmin
                    </router-link>
                </a-tooltip>
            </div>

            <div class="user" style="float: right;margin-right:15px">
                <a-space>
                    <a-dropdown>
                        <a class="ant-dropdown-link">
                            <a-avatar style="background-color: #1890ff">
                                <template #icon><UserOutlined /></template>
                            </a-avatar>&nbsp;
                            {{user.userName}}&nbsp;
                            &nbsp;<DownOutlined/>
                        </a>
                        <template #overlay>
                            <a-menu>
                                <a-menu-item key="2" @click="userPass=true">
                                    <a-space>
                                        <UserSwitchOutlined />修改密码
                                    </a-space>
                                </a-menu-item>
                                <a-menu-item key="4" @click="logout">
                                    <a-space>
                                        <arrow-right-outlined />退出登录
                                    </a-space>
                                </a-menu-item>
                            </a-menu>

                        </template>
                    </a-dropdown>
                </a-space>
            </div>

            <a-menu
                    theme="dark"
                    mode="horizontal"
                    v-model:selectedKeys="selectedMenu"
                    style="height: 50px;line-height: 50px; "
            >
                <a-menu-item key="upload">
                    <router-link to="/admin/upload">
                        <upload-outlined />
                        <span>上传资源</span>
                    </router-link>
                </a-menu-item>
                <a-menu-item key="list">
                    <router-link to="/admin/list">
                        <file-outlined />
                        <span>资源管理</span>
                    </router-link>
                </a-menu-item>
                <a-menu-item key="tag">
                    <router-link to="/admin/tag">
                        <tags-outlined />
                        <span>标签管理</span>
                    </router-link>
                </a-menu-item>
                <a-menu-item key="setting">
                    <router-link to="/admin/setting">
                        <setting-outlined />
                        <span>其他设置</span>
                    </router-link>

                </a-menu-item>
            </a-menu>


        </a-layout-header>
        <a-layout-content style="padding:  3%">
            <div :style="{ background: '#fff', padding: '24px', minHeight: '280px' ,marginBottom:'50px'}">
                <router-view></router-view>
            </div>
        </a-layout-content>
        <footerIndex />
    </a-layout>


    <a-modal v-model:visible="userPass" width="380px" title="修改用户密码"
              :footer="null">
        <a-form :model="passForm" ref="passForm" name="normal_login" class="login-form"  :rules="passFormRules" @finish="updatePass" >
            <a-form-item name="oldPass">
                <a-input-password v-model:value="passForm.oldPass"  placeholder="输入旧密码">
                    <template #prefix><LockOutlined style="color: rgba(0, 0, 0, 0.25)" /></template>
                </a-input-password>
            </a-form-item>

            <a-form-item name="newPass">
                <a-input-password v-model:value="passForm.newPass" placeholder="输入新密码">
                    <template #prefix><LockOutlined style="color: rgba(0, 0, 0, 0.25)" /></template>
                </a-input-password>
            </a-form-item>

            <a-form-item name="reNewPass">
                <a-input-password v-model:value="passForm.reNewPass" placeholder="再次输入新密码">
                    <template #prefix><LockOutlined style="color: rgba(0, 0, 0, 0.25)" /></template>
                </a-input-password>
            </a-form-item>

            <a-form-item style="margin-bottom: 0px">
                <div style="margin: 0px auto;width: 90px;">
                    <a-button type="primary" html-type="submit" class="login-form-button" :disabled="this.passForm.passState">
                        修改密码
                    </a-button>
                </div>
            </a-form-item>
        </a-form>

    </a-modal>


</template>
<script>
    import { defineComponent, ref } from 'vue';
    import { Modal,message } from 'ant-design-vue';
    import { clearLocalAll, getLocal, removeLocal, setLocal } from "@/utils/cache";
    import {get,post} from "@/utils/request";

    import footerIndex from '@/components/footer-index.vue';

    export default defineComponent({
        components:{footerIndex},
        data() {
            return {
                selectedMenu : [],
                userPass:false,
                passForm: {
                    oldPass:"",
                    newPass:"",
                    reNewPass:"",
                    passState:false
                },
                passFormRules: {
                    oldPass: [
                        {
                            required: true,
                            validator: (rule, value, callback)=>{
                                if(value == ""){
                                    return Promise.reject("请输入密码");
                                }else{
                                    return Promise.resolve();
                                }
                            },
                            trigger: 'change'
                        }
                    ],
                    newPass: [
                        {
                            required: true,
                            validator: (rule, value, callback)=>{
                                if(value == ""){
                                    return Promise.reject("请输入密码");
                                }else if(!(value.length >= 8 && value.length <= 16)){
                                    return Promise.reject("密码长度要大于等于8位或小于等于16位");
                                }else if(!/^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?\d)/.test(value)){
                                    return Promise.reject("密码必须包含大小写字符和数字");
                                }else{
                                    return Promise.resolve();
                                }
                            },
                            trigger: 'change'
                        }
                    ],
                    reNewPass: [
                        {
                            required: true,
                            validator: (rule, value, callback)=>{
                                if(value == ""){
                                    return Promise.reject("请再次输入密码");
                                }else if(this.passForm.newPass != value){
                                    return Promise.reject("两次密码输入不一致");
                                }else{
                                    return Promise.resolve();
                                }
                            },
                            trigger: 'blur'
                        }
                    ],
                },
                user:null
            };
        },
        created() {
            this.user = getLocal("user");
            var host = window.location.pathname;
            var lastItem = host.substring(host.lastIndexOf("/")+1,host.length)
            this.selectedMenu = [lastItem]
        },
        methods:{
            updatePass(){
                this.NProgress.start()
                var that = this;
                this.passForm.passState = true;
                post("/t-user/pass",this.passForm).then((res)=>{
                    this.NProgress.done()
                    message.success("密码修改成功，请重新登录！3秒后自动刷新。");
                    setTimeout(function () {
                        that.logout()
                    },3000)
                },(err) => {
                    this.passForm.passState = false;
                })
            },
            logout(){
                removeLocal("user")
                removeLocal("access_token")
                location.reload();
            },
        }
    });
</script>
<style>
    .logo{
        height: 50px;
        padding-left:20px;
        padding-right:20px;
        line-height: 50px;
        float: left;
        color: #fff;
    }

    .site-layout-content {
        min-height: 280px;
        padding: 24px;
        background: #fff;
    }
    #components-layout-demo-top .logo {
        float: left;
        width: 120px;
        height: 31px;
        margin: 16px 24px 16px 0;
        background: rgba(255, 255, 255, 0.3);
    }
    .ant-row-rtl #components-layout-demo-top .logo {
        float: right;
        margin: 16px 0 16px 24px;
    }

    [data-theme='dark'] .site-layout-content {
        background: #141414;
    }
</style>
