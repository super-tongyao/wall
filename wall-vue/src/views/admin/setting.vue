<template>
    <a-form :model="settingForm" ref="settingForm" name="settingForm"
            :rules="settingFormRules" v-bind="{labelCol: {span: 6,},wrapperCol: {pan: 14,},}" @finish="submitData">

        <a-form-item name="homeTitle" label="首页标题">
            <a-input v-model:value="settingForm.homeTitle" placeholder="首页标题"></a-input>
        </a-form-item>

        <a-form-item name="tagId" label="首页展示标签" extra="打开首页时，第一展示的标签内容。">
            <a-select placeholder="首页展示标签" allowClear="true"  v-model:value="settingForm.initTagId" >
                <template  v-for="(item, i) in tagList">
                    <a-select-option :value="item.tagId">{{item.tagName}}</a-select-option>
                </template>
            </a-select>
        </a-form-item>

        <a-form-item name="secret" label="令牌密钥" extra="如果跟其他站点共享Token令牌后，会有后台账号权限泄露风险，重启服务生效。">
            <a-input v-model:value="settingForm.secret" placeholder="令牌密钥"></a-input>
        </a-form-item>

        <a-form-item name="expireDate" label="令牌过期时间" extra="Jwt不可销毁原因，建议设定较短的有效时间，单位为分钟，重启服务生效。">
            <a-input-number style="width: 100%" v-model:value="settingForm.expireDate" placeholder="令牌过期时间"></a-input-number>
        </a-form-item>

        <a-form-item name="saveFilePath" label="文件保存位置" extra="图片、视频等其他文件保存的路径，文件没有会自动创建，谨慎修改。">
            <a-input v-model:value="settingForm.saveFilePath" placeholder="文件保存位置"></a-input>
        </a-form-item>

        <a-form-item name="beian" label="网站备案号" extra="通过信管局正规备案的备案号，没有可以不设置。">
            <a-input v-model:value="settingForm.beian" placeholder="网站正规备案号"></a-input>
        </a-form-item>

        <a-form-item style="margin: 0px auto;width:90px;">
            <a-button type="primary" html-type="submit"><form-outlined /> 保存设置</a-button>
        </a-form-item>
    </a-form>
</template>
<script>
    import {ref, defineComponent } from 'vue';
    import {get,post} from "@/utils/request";
    import {message} from "ant-design-vue";

    export default defineComponent({
        components: {},
        data() {
            return {
                settingForm: {
                    homeTitle:"",
                    saveFilePath:"",
                    beian: "",
                    secret:"",
                    expireDate:"",
                    initTagId:ref()
                },
                settingFormRules: {
                    homeTitle: [
                        {
                            required: true,
                            message: "首页标题不可为空！",
                            trigger: 'blur'
                        }
                    ],
                    saveFilePath: [
                        {
                            required: true,
                            message: "文件保存位置不可为空！",
                            trigger: 'blur'
                        }
                    ],
                    secret: [
                        {
                            required: true,
                            message: "令牌密钥不能为空！",
                            trigger: 'blur'
                        }
                    ],
                    expireDate: [
                        {
                            required: true,
                            message: "令牌过期时间不能为空！",
                            trigger: 'blur'
                        }
                    ],
                },
                tagList:[],
            };
        },
        created() {
            this.NProgress.start()
            get('/t-tag/query').then((res) => {
                this.tagList = res.data
            }).catch(() => {});

            get('/t-option/query').then((res) => {
                this.settingForm.beian = res.data.beian
                this.settingForm.expireDate = res.data.expireDate
                this.settingForm.homeTitle = res.data.homeTitle
                if (res.data.initTagId == null || res.data.initTagId == ""){
                    this.settingForm.initTagId = ref()
                }else{
                    this.settingForm.initTagId = res.data.initTagId
                }
                this.settingForm.saveFilePath = res.data.saveFilePath
                this.settingForm.secret = res.data.secret
                this.NProgress.done()
            }).catch(() => {});

        },
        methods:{
            submitData(){
                this.NProgress.start()
                var data = {};
                if (typeof this.settingForm.initTagId == "undefined"){
                    data.initTagId = "";
                }else{
                    data.initTagId = this.settingForm.initTagId;
                }
                data.beian = this.settingForm.beian;
                data.expireDate = this.settingForm.expireDate;
                data.homeTitle = this.settingForm.homeTitle;
                data.saveFilePath = this.settingForm.saveFilePath;
                data.secret = this.settingForm.secret;

                post('/t-option/save',data).then((res) => {
                    message.success('保存成功啦~');
                    this.NProgress.done()
                }).catch(() => {});
            },
        }
    })
</script>
