<template>
    <a-spin :spinning="uploadLoding">
        <template #tip>
            <p style="margin-top: 10px">正在上传... <br> 请勿关闭或刷新页面</p>
        </template>
        <a-form :model="resourceForm" ref="resourceForm" name="resourceForm" :rules="resourceFormRules"
                v-bind="{labelCol: {span: 6,},wrapperCol: {pan: 14,},}" @finish="submitData">
            <a-form-item name="title" label="资源标题">
                <a-input v-model:value="resourceForm.title" placeholder="资源标题"></a-input>
            </a-form-item>

            <a-form-item name="coverType" label="封面选项" >
                <a-radio-group v-model:value="resourceForm.coverType">
                    <a-radio value="1" @click="coverFun(false)">自动识别封面</a-radio>
                    <a-radio value="2" @click="coverFun(true)">上传封面</a-radio>
                </a-radio-group>
            </a-form-item>

            <div v-if="coverUpload">
                <a-form-item name="cover" label="上传封面" >
                    <a-upload v-model:file-list="resourceForm.cover"
                              listType="picture"
                              :max-count="1"
                              :before-upload="beforeUpload"
                              accept="image/png, image/jpeg,image/gif"
                              id="coverFile"
                    >
                        <a-button>
                            <upload-outlined /> 选择封面图片
                        </a-button>
                    </a-upload>
                </a-form-item>
            </div>

            <a-form-item name="tagId" label="标签归类" extra="为空时，默认归类全部类型标签。">
                <a-select placeholder="标签归类"
                          allowClear="true" :max-tag-count="this.maxTagCount"
                          v-model:value="resourceForm.tagId" mode="multiple"
                >
                    <template  v-for="(item, i) in tagList">
                        <a-select-option :value="item.tagId" >{{item.tagName}}</a-select-option>
                    </template>
                </a-select>

            </a-form-item>

            <a-form-item name="fileType" label="上传的文件格式" v-if="android">
                <a-radio-group v-model:value="accept">
                    <a-radio value="image/*" @click="accept == 'image/*'">图片</a-radio>
                    <a-radio value="video/*" @click="accept == 'video/*'">视频</a-radio>
                </a-radio-group>
            </a-form-item>
            <a-form-item name="resource" label="上传文件" >
                <a-upload v-model:file-list="resourceForm.resource"
                          listType="picture"
                          :max-count="1"
                          :before-upload="beforeUpload"
                          :accept="accept"
                          id="resourceFile"
                >
                    <a-button>
                        <upload-outlined /> {{android == true ? accept == 'image/*' ? '选择图片' : '选择视频' : uploadName}}
                    </a-button>
                </a-upload>
            </a-form-item>

            <a-form-item style="margin: 0px auto;width:202px;">
                <a-space>
                    <a-button type="primary" html-type="submit"><to-top-outlined /> 提交上传</a-button>
                    <a-button  @click="resetForm"><reload-outlined /> 重置</a-button>
                </a-space>
            </a-form-item>
        </a-form>
    </a-spin>

</template>
<script>
    import { defineComponent, ref } from 'vue';
    import {get, post} from "@/utils/request";
    import {message,Modal} from "ant-design-vue";
    import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

    export default defineComponent({
        components: {ExclamationCircleOutlined},
        data() {
            return {
                coverUpload:false,
                resourceForm: {
                    title: "",
                    coverType: "1",
                    tagId:ref([]),
                    resource: ref([]),
                    cover: ref([])
                },
                resourceFormRules: {
                    title: [
                        {
                            required: true,
                            validator: (rule, value, callback)=>{
                                if(value == ""){
                                    return Promise.reject("资源标题还没输入呢");
                                }else if(value.length > 300){
                                    return Promise.reject("标题长度不能超过300个字符");
                                }else {
                                    return Promise.resolve();
                                }
                            },
                            trigger: 'blur'
                        }
                    ]
                },
                uploadLoding:false,
                tagList:[],
                accept:"",
                android:false,
                uploadName:"选择图片/视频"
            };
        },
        created() {
            get('/t-tag/query').then((res) => {
                this.tagList = res.data
            }).catch(() => {});


        },
        mounted() {
            document.getElementById("resourceFile").removeAttribute('capture')

            document.getElementById("resourceForm_tagId").remove();

            var deviceName = navigator.userAgent.toLowerCase();
            if(deviceName.indexOf("iphone") != -1) {
                // 苹果
                this.accept = "image/*,video/*";
            }else if(deviceName.indexOf("windows") != -1){
                this.accept = ".jpg,.png,.gif,.mp4,.mov";
            }else{
                this.android = true;
                this.accept = "image/*";
                this.uploadName = "选择图片";
            }


        },
        methods:{
            submitData () {
                if(this.resourceForm.resource.length == 0){
                    message.warning('你还没有选择要上传的图片或视频呢');
                    return false;
                }
                if(this.resourceForm.coverType == "2" &&  this.resourceForm.cover.length == 0){
                    message.warning('请选择要上传的图片封面');
                    return false;
                }
                this.uploadLoding = true;

                this.NProgress.start()
                const formData = new FormData();
                formData.append('resource', this.resourceForm.resource[0].originFileObj);
                if (this.resourceForm.coverType == "2"){
                    formData.append('cover', this.resourceForm.cover[0].originFileObj);
                }
                formData.append('title', this.resourceForm.title);
                formData.append('coverType', this.resourceForm.coverType);
                if (this.resourceForm.tagId != ""){
                    formData.append('tagId', this.resourceForm.tagId+",");
                }else{
                    formData.append('tagId', "");
                }

                post('/t-resource/upload', formData).then(() => {
                    this.resourceForm.resource = ref([]);
                    this.resourceForm.cover = ref([]);
                    this.resourceForm.title = ""
                    this.uploadLoding = false;
                    message.success('上传成功啦~');
                    this.NProgress.done()
                }).catch(() => {
                    this.uploadLoding = false;
                });

            },
            resetForm(){
                this.resourceForm.coverType = "1";
                this.coverUpload = false;
                this.resourceForm.tagId = ref([])
                this.resourceForm.resource = ref([]);
                this.resourceForm.cover = ref([]);
                this.resourceForm.title = ""
                if (this.coverUpload){
                    document.getElementById("coverFile").removeAttribute('capture')
                }
                document.getElementById("resourceFile").removeAttribute('capture')
                message.success('表单已重置！');
            },
            coverFun(e){
                this.coverUpload = e;
                if (e){
                    setTimeout(function () {
                        document.getElementById("coverFile").removeAttribute('capture')
                    })
                }
            },
            beforeUpload(){
                var that = this;
                setTimeout(function () {
                    if (that.coverUpload){
                        document.getElementById("coverFile").removeAttribute('capture')
                    }
                    document.getElementById("resourceFile").removeAttribute('capture')
                })
                return false;
            }


        }
    })
</script>
