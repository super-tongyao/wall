<template>
    <a-layout-footer class="footer">
        <div style="min-height: 25px;overflow:hidden;line-height:25px;text-align:center;font-size:10px;">
            当前使用 <a href="https://github.com/super-tongyao/wall" target="_blank">Wall</a> 搭建（<a href="https://ityao.cn" target="_blank">Tongyao</a> 提供技术支持）
            <div v-if="beian!=''" style="display: inline-block">
                <a href="https://beian.miit.gov.cn/" target="_blank">{{beian}}</a>
            </div>
        </div>
    </a-layout-footer>
</template>

<script>
    import {get} from "@/utils/request";

    export default {
        name: "footer",
        data(){
            return {
                beian:""
            }
        },
        created() {
            get("/t-option/target").then((res)=>{
                this.beian = res.data.beian;
            })
        }
    }
</script>

<style scoped>
    .footer{
        padding: 0px;
        background-color: #fff;
        margin-top: 50px;
        position: fixed;
        bottom: 0px;
        width: 100%;
        z-index: 999;
    }
</style>
