<template>
    <a-layout>
        <a-layout-header class="header">
            <div class="header-title">
                <span @click="tests">{{option.homeTitle}}</span>
            </div>
            <div class="github-link">
                <a-tooltip title="前往 Github" color="#1890ff" placement="bottom">
                    <a href="https://github.com/super-tongyao/wall" target="_blank"><github-outlined /> GitHub</a>
                </a-tooltip>
            </div>
            <a-menu theme="light" mode="horizontal" style="line-height: 50px;border: 0px" v-model:selectedKeys="selectedTag">
                <a-menu-item key="" @click="load('')">
                    全部
                </a-menu-item>
                <a-menu-item :key="item.tagId" v-for="(item, i) in tagList" @click="load(item.tagId)">
                    {{item.tagName}}
                </a-menu-item>
            </a-menu>

        </a-layout-header>


        <a-layout-content :style="this.waterfallStyle" id="galleryID" style="min-height: 300px;">
            <a-spin size="large" :spinning="show.loading" style="height: 300px">
                <div v-if="show.empty">
                    <a-empty :image="simpleImage" description="亲，么的数据 ~" style="width:300px;margin:0px auto;margin-top: 50px"  />
                </div>
                <div v-else>
                    <div id="pubuliu" class="pubuliu" v-masonry destroy-delay="3" transition-duration="0.3s" item-selector=".item" style="background-color: rgb(240 242 245 / 0%);margin-top: 20px;margin-bottom: 50px" >
                        <a-image-preview-group>
                            <div v-masonry-tile class="item" v-for="(item, i) in dataList" style="margin: 6px;"
                                 @mouseenter="moveIn(i)" @mouseleave="moveOut(i)" @click="visible = true">

                                <div v-if="item.resourceType == 'mp4' || item.resourceType == 'mov'"  @click="palyVideo(item.resourcePath,item.title)">
                                    <img :src="'api/static/'+item.coverPath" class="thumbnail" :width="this.waterfallWidth" />
                                    <div class="img-text" ref="imgText" :title="item.title">
                                        {{item.title}}
                                    </div>
                                    <!--<div style="position: absolute;top: 35px;right: 50px;color: #fff;">
                                        <play-circle-filled />
                                    </div>-->

    <!--                                <div style="position: absolute;top: 5px;right: 0px;color: #fff;opacity: 0.5">-->
    <!--                                    <a-tag color="blue" style="font-size: 2px;display: inline-block">视频</a-tag>-->
    <!--                                </div>-->

                                    <!--<div style="position: absolute;top: 5px;right: 0px;color: #fff;">
                                        <a-tag color="green" style="font-size: 2px;display: inline-block">VR全景</a-tag>
                                    </div>-->

                                </div>
                                <div v-else>


                                    <a-image
                                            :width="this.waterfallWidth"
                                            :src="'api/static/'+item.coverPath"
                                            :preview="{
                                              src: 'api/static/'+item.resourcePath,
                                            }"
                                            :previewMask="false"
                                            class="thumbnail" :alt="item.title"
                                    />
                                    <div class="img-text" ref="imgText" :title="item.title">
                                        {{item.title}}
                                    </div>




                                </div>
                            </div>
                        </a-image-preview-group>
                    </div>
                </div>
            </a-spin>





        </a-layout-content>
        <footerIndex />
    </a-layout>

    <div class="video-model" v-if="video.show">
        <div class="video-model-close" @click="videoClose"><close-outlined /></div>
        <video ishivideo="true" autoplay="true" isrotate="true" autoHide="true" controls="controls" :style="this.videoWidthHeighut" class="video-model-video">
            <source :src="video.url" type="video/mp4" >
            <source :src="video.url" type="video/mov" >
            您的浏览器不支持视频播放
        </video>
        <div class="video-model-text">
            <p :title="video.title">{{video.title}}</p>
        </div>
    </div>



</template>

<script>
    import {defineComponent, ref} from 'vue';
    import {get, post,remove} from "@/utils/request";

    import footerIndex from '@/components/footer-index.vue';


    import PhotoSwipeLightbox from 'photoswipe/lightbox';
    import 'photoswipe/style.css';


    export default defineComponent({
        components: {footerIndex},
        data() {
            return {
                selectedTag:null,
                tagList:[],
                dataList:[],
                video:[{
                    url:"",
                    show:false,
                    title:""
                }],
                option:[],
                show:{
                    loading:false,
                    empty:false
                }
            };
        },
        unmounted() {
            if (this.lightbox) {
                this.lightbox.destroy();
                this.lightbox = null;
            }
        },
        mounted() {
            this.NProgress.start()
            get("/t-option/target").then((res)=>{
                this.option = res.data;
                // 回显首页导航标签
                if(typeof this.$route.params.tagId == "undefined"){
                    this.selectedTag = ref([this.option.initTagId])
                    this.load(this.option.initTagId)
                }else{
                    this.selectedTag = ref([this.$route.params.tagId])
                    this.load(this.$route.params.tagId)
                }
                this.NProgress.done();
            })

            get("/t-tag/query").then((res)=>{
                this.tagList = res.data
            })

            var that = this;
            setInterval(function () {
                that.$redrawVueMasonry()
            },1000)

        },
        methods: {
            loaded(instance ) {
                console.log(instance);
            },
            // 移入移除事件
            moveIn(i) {
                this.$refs.imgText[i].style='opacity:1'
            },
            moveOut(i) {
                this.$refs.imgText[i].style='display:0'
            },
            palyVideo(url,title){
                this.video.url = 'api/static/'+url;
                this.video.show = true
                this.video.title = title;
            },
            load(tagId){
                var that = this;
                this.show.empty = false;
                this.show.loading = true;
                this.NProgress.start()
                get("/t-resource/query?tagId="+tagId).then((res)=>{
                    this.dataList = res.data;
                    this.show.loading = false;
                    if (this.dataList == null || this.dataList.length == 0){
                        this.show.empty = true;
                    }

                    this.NProgress.done()
                })
            },
            videoClose(){
                this.video.show = false;
            },
        }
    })
</script>

<style>

    .header {
        background-color: #ffffff;
        /*height: 40px;*/
        height: 50px;
        padding-left: 10px;
        padding-right: 10px;
    }
    .header-title{
        height: 50px;
        padding-left:10px;
        padding-right:10px;
        line-height: 50px;
        float: left;
        font-weight: bold;
        font-size: 16px;
    }
    .img-text{
        position:absolute;
        bottom:0px;
        height:50px;
        line-height:55px;
        background: linear-gradient(180deg,transparent 0,rgba(0,0,0,.5) 91%);
        color: #fff;
        width: 100%;
        opacity:0;
        transition-duration: 0.2s;
        border-bottom-left-radius: 5px;
        border-bottom-right-radius: 5px;
        font-weight: 700;
        letter-spacing:1.5px;
        font-size: 1rem;

        padding-left: 15px;
        padding-right: 15px;
        overflow:hidden;
        white-space:nowrap;
        text-overflow:ellipsis;
    }
    .thumbnail{
        border-radius: 5px;
        overflow: hidden;
        transition-duration: 0.2s;
    }

    ::-webkit-scrollbar {
        /*隐藏滚轮*/
        display: none;
    }

    body{
        background: rgb(240,242,245);
        cursor: url('@/assets/pointer.cur'),auto!important;
    }

    .github-link{
        float: right;
        height: 50px;
        line-height: 50px;
    }

    .github-link a:hover{
        color: #1890ff;
    }

    .github-link a{
        margin: 0px 5px 0px 0px;
        color: #000
    }


    .video-model{
        position: fixed;
        background-color: rgba(0, 0, 0, 0.8);
        top: 0px;
        bottom: 0px;
        left: 0px;
        right: 0px;
        overflow: hidden;
    }

    .video-model-video{
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        margin: auto;
        z-index: 99999;
        background: #000; /* 设置背景为灰色 */

    }

    .video-model-close{
        position: absolute;
        right: 20px;
        font-size: 18px;
        top: 15px;
        color: #fff;
        font-weight: 900;
        cursor: pointer;
    }

    .video-model-text{
        position: absolute;
        bottom: 50px;
        color: #fff;
        font-size: 16px;
        width: 100%;
        height: 50px;
        padding-left: 40px;
        padding-right: 40px;
    }

    .video-model-text p{
        line-height: 50px;
        /*文字超过变...*/
        overflow:hidden;
        white-space:nowrap;
        text-overflow:ellipsis;
        cursor: pointer;
    }
</style>
