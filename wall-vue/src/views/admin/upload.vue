<template>
    <a-spin :spinning="uploadLoding">
        <template #tip>
            <p style="margin-top: 10px">正在上传... <br> 请勿关闭或刷新页面</p>
        </template>
        <a-form :model="resourceForm" ref="resourceForm" name="resourceForm" :rules="resourceFormRules"
                :label-col="{sm: {span: 5}}" :wrapper-col="{sm: { span: 15}}"
                @finish="submitData">

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

            <a-form-item name="sourceType" label="资源来源">
                <a-radio-group v-model:value="resourceForm.sourceType">
                    <a-radio value="1" @click="uploadSource(true)">本地上传</a-radio>
                    <a-radio value="2" @click="uploadSource(false)">图床URL</a-radio>
                </a-radio-group>
            </a-form-item>

            <a-form-item name="fileType" label="上传格式" v-if="android">
                <a-radio-group v-model:value="accept">
                    <a-radio value="image/*" @click="accept == 'image/*'">图片</a-radio>
                    <a-radio value="video/*" @click="accept == 'video/*'">视频</a-radio>
                </a-radio-group>
            </a-form-item>


            <a-form-item name="resource" label="上传文件" v-if="sourceToggle" >

                <a-upload v-model:file-list="resourceForm.resource"
                          listType="picture"
                          :before-upload="beforeUpload"
                          :accept="accept"
                          id="resourceFile"
                          multiple="true"
                          style="width: 100%;border: 1px red solid"
                >

                    <a-button type="dashed" style="width: 100%;" >
                        <upload-outlined /> {{android == true ? accept == 'image/*' ? '选择图片' : '选择视频' : uploadName}}
                    </a-button>

                    <template #itemRender="{ file, actions }">
                        <div style="width: 100%;border: 1px red solid;margin-top: 5px;height: 66px;border: 1px #eee solid;border-radius: 5px">

                            <a-row type="flex" :wrap="false">
                                <a-col flex="66px">
                                    <img width="50" height="50" :src="file.thumbUrl"
                                         style=";margin-top: 7px;margin-left: 7px;border-radius: 3px"/>
                                </a-col>
                                <a-col flex="auto">
                                    <a-input v-model:value="file.title" placeholder="描述一下发生的趣事吧~" style="margin-top: 17px;border: 0px" />
                                </a-col>
                                <a-col flex="45px">
                                    <delete-outlined style="line-height: 66px;margin-left: 13px;cursor: pointer" @click="actions.remove"/>
                                </a-col>
                            </a-row>

                        </div>
                    </template>
                </a-upload>

            </a-form-item>
            <a-form-item name="bedUrl" label="图床URL" v-else>
                <a-button type="dashed" style="width: 100%;" @click="addBadUrl" >
                    <plus-circle-outlined /> 添加一个图床URL
                </a-button>

                <div v-for="(item,i) in resourceForm.bedUrl">
                    <div style="width: 100%;border: 1px red solid;margin-top: 5px;height: 66px;border: 1px #eee solid;border-radius: 5px">
                        <a-row type="flex" :wrap="false">
                            <a-col flex="66px">
                                <a-image
                                        :width="50" :height="50"
                                        :src="item.img"
                                        :preview="false"
                                        :fallback="require('../../assets/img.png')"
                                        style=";margin-top: 7px;margin-left: 7px;border-radius: 3px"
                                />
                            </a-col>
                            <a-col flex="auto">
                                <a-input v-model:value="item.title" size="small" :bordered="false" placeholder="描述一下发生的趣事吧~" style="outline:none;margin-top: 7px" />
                                <a-input v-model:value="item.url" size="small" :bordered="false" placeholder="https://" style="margin-top: 7px;outline:none;"  @blur="echoImg(i)" />
                            </a-col>
                            <a-col flex="45px">
                                <delete-outlined style="line-height: 66px;margin-left: 13px;cursor: pointer" @click="delBadUrl(i)" />
                            </a-col>
                        </a-row>
                    </div>
                </div>
            </a-form-item>

            <a-form-item name="resourceType" label="资源格式" extra="VR全景需要全景图片格式，可使用具备360度全景相机拍摄，如：影石 Insta 360 全景相机等。">
                <a-radio-group v-model:value="resourceForm.resourceType">
                    <a-radio value="1">普通资源</a-radio>
                    <a-radio value="2">VR全景</a-radio>
                </a-radio-group>
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
                    tagId:ref([]),
                    sourceType: "1",
                    resource: ref([]),
                    bedUrl:[
                        {
                            img: require('@/assets/img.png'),
                            title:'',
                            url:''
                        }
                    ],
                    resourceType:'1'
                },
                resourceFormRules: {
                    bedUrl: [
                        {
                            required: true,
                            validator: (rule, value, callback)=>{
                                var temp = false;
                                for (var i = 0; i < value.length; i++) {
                                    if(value[i].url == ""){
                                        temp = true;
                                        break;
                                    }else{
                                        temp = false;
                                    }
                                }
                                if(temp){
                                    return Promise.reject("请补齐图床URL");
                                }else{
                                    return Promise.resolve();
                                }
                            },
                            trigger: 'change'
                        }
                    ],
                },
                uploadLoding:false,
                tagList:[],
                accept:"",
                android:false,
                uploadName:"选择图片 / 视频",
                sourceToggle:true
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
                this.accept = ".jpg,.png,.gif,.mp4,.mov,.wmv";
            }else{
                this.android = true;
                this.accept = "image/*";
                this.uploadName = "选择图片";
            }
        },
        methods:{
            submitData () {
                if(this.resourceForm.sourceType == "1" && this.resourceForm.resource.length == 0){
                    message.warning('你还没有选择要上传的图片或视频呢');
                    return false;
                }
                this.uploadLoding = true;
                this.NProgress.start()
                const formData = new FormData();
                if (this.resourceForm.tagId != ""){
                    formData.append('tagId', this.resourceForm.tagId+",");
                }else{
                    formData.append('tagId', "");
                }
                formData.append('sourceType', this.resourceForm.sourceType);
                formData.append('resourceFormat', this.resourceForm.resourceType);

                if (this.resourceForm.sourceType == "1"){
                    for (var i = 0; i < this.resourceForm.resource.length; i++) {
                        formData.append('resource', this.resourceForm.resource[i].originFileObj);
                        formData.append('title', this.resourceForm.resource[i].title);
                    }
                }else{
                    var tempJson = this.resourceForm.bedUrl;
                    for (var i = 0; i < this.resourceForm.bedUrl.length; i++) {
                        delete tempJson[i].img;
                    }
                    formData.append('bedUrl', JSON.stringify(tempJson));
                }


                console.log(this.resourceForm.resource)

                post('/t-resource/upload', formData).then(() => {
                    this.resourceForm.bedUrl = [];
                    this.resourceForm.resource = ref([]);
                    this.resourceForm.title = ""
                    this.uploadLoding = false;
                    message.success('上传成功啦~');
                    this.NProgress.done()
                }).catch(() => {
                    this.uploadLoding = false;
                });

            },
            resetForm(){
                this.resourceForm.tagId = ref([])
                this.resourceForm.resource = ref([]);
                this.resourceForm.cover = ref([]);
                if (this.coverUpload){
                    document.getElementById("coverFile").removeAttribute('capture')
                }
                document.getElementById("resourceFile").removeAttribute('capture')
                message.success('表单已重置！');
            },
            beforeUpload(){
                var that = this;
                setTimeout(function () {
                    document.getElementById("resourceFile").removeAttribute('capture')
                })
                return false;
            },
            addBadUrl(){
                this.resourceForm.bedUrl.push({
                    img: require('@/assets/img.png'),
                    title:'',
                    url:''
                })
            },
            delBadUrl(index){
                if (this.resourceForm.bedUrl.length > 1){
                    this.resourceForm.bedUrl.splice(index,1)
                }else{
                    message.warn('这个删了还上传什么呀~');
                }
            },
            echoImg(index){
                this.resourceForm.bedUrl[index].img = this.resourceForm.bedUrl[index].url
            },
            uploadSource(flag){
                this.sourceToggle = flag;
                if (flag){
                    var deviceName = navigator.userAgent.toLowerCase();
                    this.android = (deviceName.indexOf('iphone') == -1 && deviceName.indexOf('windows') == -1)
                }else{
                    this.android = flag
                }
            }

        }
    })
</script>
<style>

    .ant-upload.ant-upload-select{
        display: inline;
    }
</style>
