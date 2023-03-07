<template>
    <div id="container">
        <div id="main">
            <div class="login-logo">
                <div class="logo">
                    <img class="logo-svg" src="@/assets/logo.png"/>
                    <div class="logo-text">Wall</div>
                </div>
            </div>
            <a-tabs centered>
                <a-tab-pane key="1" tab="账号密码登录">
                    <a-form :model="loginForm" ref="loginForm" name="normal_login" class="login-form"
                            :rules="loginFormRules" @finish="login">
                        <a-form-item name="userName">
                            <a-input v-model:value="loginForm.userName" style="height: 40px" placeholder="账号">
                                <template #prefix>
                                    <UserOutlined class="site-form-item-icon" style="color: #1890ff"/>
                                </template>
                            </a-input>
                        </a-form-item>

                        <a-form-item name="passWord">
                            <a-input-password v-model:value="loginForm.passWord" style="height: 40px"
                                              placeholder="请输入密码">
                                <template #prefix>
                                    <LockOutlined class="site-form-item-icon" style="color: #1890ff"/>
                                </template>
                            </a-input-password>
                        </a-form-item>
                        <a-form-item>
                            <a-button type="primary" html-type="submit" class="login-form-button"
                                      :disabled="this.loginButState">
                                登录
                            </a-button>
                        </a-form-item>
                    </a-form>
                </a-tab-pane>
            </a-tabs>
        </div>
        　　
        <div id="footer">
            <p><a href="https://github.com/super-tongyao/wall/issues" target="_blank">提交Bug问题及建议</a> </p>
        </div>

    </div>
</template>

<script>
    import {message} from "ant-design-vue";
    import {get} from "@/utils/request";
    import {clearLocalAll, getLocal, removeLocal, setLocal} from "@/utils/cache";
    import md5 from 'js-md5'

    export default {
        components: {md5},
        data() {
            return {
                loginForm: {
                    userName: "",
                    passWord: "",
                },
                loginButState: false,
                loginFormRules: {
                    userName: [
                        {
                            required: true,
                            message: "请输入账号",
                            trigger: 'blur'
                        }
                    ],
                    passWord: [
                        {
                            required: true,
                            message: "请输入密码",
                            trigger: 'blur'
                        }
                    ],
                },
            };
        },
        methods: {
            login(e) {
                this.NProgress.start()
                this.loginButState = true;
                get("/login?username=" + this.loginForm.userName + "&password=" + md5(this.loginForm.passWord)).then((res) => {
                    message.success("登录成功！");
                    setLocal('user', res.data)
                    setLocal('access_token', res.data.access_token)
                    this.NProgress.done()
                    this.$router.push("/admin")
                }, (err) => {
                    this.loginButState = false;
                })
            },
        }
    }
</script>

<style scoped>
    .login-logo {
        width: 100%;
        overflow: hidden;
        margin-top: 60px;
        margin-bottom: 40px;
    }

    .logo {
        width: 155px;
        height: 42px;
        margin: 0px auto;
    }

    .logo-svg {
        line-height: 0px;
        width: 40px;
        display: inline-block;
        float: left;
    }

    .logo-text {
        width: 100px;
        font-size: 24px;
        color: #000000;
        font-weight: bold;
        float: left;
        margin-left: 10px;
    }

    #container {
        min-height: 100%;
        height: auto !important;
        position: absolute;
        width: 100%;

        background-image: url("@/assets/background.svg");
        background-repeat: no-repeat;
        background-size: 100%;
        background-position-y: 110px;
    }

    #main {
        width: 368px;
        margin: 0 auto;
        padding-bottom: 60px; /*等于footer的高度*/
    }

    #footer {
        position: absolute;
        bottom: 0;
        width: 100%;
        height: 60px; /*脚部的高度*/
        background: rgba(255, 230, 135, 0);
        clear: both;
    }

    #footer p {
        text-align: center;
        line-height: 60px;
        color: #666;
    }

    /*表单等相关样式*/
    .login-form-button {
        width: 100%;
        height: 40px;
        font-size: 16px;
    }
</style>
