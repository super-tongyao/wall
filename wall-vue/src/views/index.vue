<template>
    <a-layout>
        <a-layout-header class="header">
            <div class="header-title">
                <span @click="tests">{{option.homeTitle}}</span>
            </div>
            <div style="float: right;height: 50px;line-height: 50px;">
                <a style="margin: 0px 5px 0px 0px;color:#000" href="https://github.com/super-tongyao/wall" target="_blank"><github-outlined /> GitHub</a>
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
                        <div v-masonry-tile class="item" v-for="(item, i) in dataList" style="margin: 6px;"
                             @mouseenter="moveIn(i)" @mouseleave="moveOut(i)" @click="visible = true">

                            <div v-if="item.resourceType == 'mp4' || item.resourceType == 'mov'"  @click="palyVideo(item.resourcePath,item.title)">
                                <img :src="'api/static/'+item.coverPath" class="thumbnail" :width="this.waterfallWidth" />
                                <div class="img-text" ref="imgText">
                                    {{item.title}}
                                </div>
                            </div>
                            <div v-else>
                                <a :key="item.resourceId"
                                   :data-pswp-src="'api/static/'+item.resourcePath"
                                   :data-pswp-width="item.resourceWidth"
                                   :data-pswp-height="item.resourceHeight"
                                   target="_blank" rel="noreferrer" >

                                    <!--<img :src="'api/static/'+item.coverPath" class="thumbnail" :width="this.waterfallWidth" :alt="item.title" ref="img" @load="test(i)"/>-->
                                    <img :src="'api/static/'+item.coverPath" class="thumbnail" :width="this.waterfallWidth" :alt="item.title"/>
                                    <div class="img-text" ref="imgText">
                                        {{item.title}}
                                    </div>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </a-spin>
        </a-layout-content>
        <footerIndex />
    </a-layout>
    <a-modal v-model:visible="video.show" :title="video.title" :footer="null" style="padding: 0px;" :width="this.videoWidth" destroyOnClose="true">
        <video ishivideo="true" autoplay="true" isrotate="true" autoHide="true" controls="controls" style="width: 100%;margin: 0px">
            <source :src="video.url" type="video/mp4" >
            <source :src="video.url" type="video/mov" >
            您的浏览器不支持视频播放
        </video>
    </a-modal>
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
            if (!this.lightbox) {
                this.lightbox = new PhotoSwipeLightbox({
                    gallery: '#galleryID',
                    children: 'a',
                    pswpModule: () => import('photoswipe'),
                });
                that.lightbox.on('uiRegister', function() {
                    that.lightbox.pswp.ui.registerElement({
                        name: 'custom-caption',
                        order: 9,
                        isButton: false,
                        appendTo: 'root',
                        onInit: (el, pswp) => {
                            that.lightbox.pswp.on('change', () => {
                                const currSlideElement = that.lightbox.pswp.currSlide.data.element;
                                let captionHTML = '';
                                if (currSlideElement) {
                                    const hiddenCaption = currSlideElement.querySelector('.hidden-caption-content');
                                    if (hiddenCaption) {
                                        // get caption from element with class hidden-caption-content
                                        captionHTML = hiddenCaption.innerHTML;
                                    } else {
                                        // get caption from alt attribute
                                        captionHTML = currSlideElement.querySelector('img').getAttribute('alt');
                                    }
                                }
                                el.innerHTML = captionHTML || '';
                            });
                        }
                    });
                });
                this.lightbox.init();
            }
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
           /* test(i){
                this.$refs.img[i].style="background-color:red;height:"+this.$refs.img[i].height+"px";
            }*/
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
        line-height:60px;
        padding-left:15px;
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
    }
    .thumbnail{
        border-radius: 5px;
        overflow: hidden;
        transition-duration: 0.2s;
    }

    .ant-modal-header {
        height: 30px;
        line-height: 30px;
        margin: 0px;
        padding: 5px 0px 5px 10px;
    }
    .ant-modal-close-x{
        height: 30px;
        line-height: 30px;
        width: 40px;
    }

    .ant-modal-title{
    }

    .ant-modal-body{
        padding: 0px;
    }

    ::-webkit-scrollbar {
        /*隐藏滚轮*/
        display: none;
    }

    body{
        background: rgb(240,242,245);
        cursor: url('@/assets/pointer.cur'),auto!important;
    }

    .pswp__custom-caption {
        /*background: rgba(0, 0, 0, 0.75);*/
        font-size: 16px;
        color: #fff;
        position: absolute;
        left: 0px;
        right: 0px;
        bottom: 0px;
        margin: 0px 4% 3% 4%;
    }
</style>
