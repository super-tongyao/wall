<template>

    <a-space direction="vertical" style="width: 100%">
        <a-form :model="searchForm" ref="searchForm" name="searchForm" layout="inline" style="float: left">
            <a-form-item name="title" label="标签名称" style="margin-bottom: 10px" :style="this.width">
                <a-input v-model:value="searchForm.tagName" placeholder="标签名称"></a-input>
            </a-form-item>

            <a-space style="margin-bottom: 10px">
                <a-button type="primary" @click="searchTag(this.pagination.current,this.pagination.pageSize)"><search-outlined /> 查询</a-button>
                <a-button type="primary" @click="addTag"><tags-outlined /> 添加</a-button>
                <a-button type="primary" danger @click="deletes"><delete-outlined /> 批量删除</a-button>
            </a-space>
        </a-form>


        <a-table :row-selection="{onChange: (selectedRowKeys, selectedRows, event) => {this.recordRowKey(selectedRowKeys,selectedRows,event)},}"
                 :rowKey="record=>record.tagId"
                 :data-source="dataSource"
                 :columns="columns"
                 :pagination="pagination"
                 :loading="false"
                 :locale="{emptyText:'暂无数据，赶快添加吧！'}"
                 :scroll="{ x: 650 }"
        >

            <template #bodyCell="{ text, record, index, column }">
                <template v-if="column.key === 'number'">
                    {{index+1}}
                </template>

                <template v-if="column.key === 'tagName'">
                    {{text}}&nbsp;
                    <a-tooltip title="分享此标签页" color="#1890ff" placement="bottom" @click="share(record)">
                        <link-outlined />
                    </a-tooltip>
                </template>



                <template v-if="column.key === 'operation'">
                    <a-space>
                        <a href="javascript:" @click="editTag(record)">编辑</a>
                    </a-space>
                </template>
            </template>
        </a-table>
    </a-space>


    <a-modal v-model:visible="saveModel" :title="tag+'操作'" :footer="null">
        <a-form :model="tagForm" ref="tagForm" name="tagForm" :rules="tagFormRules" :label-col="{style: {width: '80px',},}"  @finish="submitData">
            <a-form-item name="tagName" label="标签名称">
                <a-input v-model:value="tagForm.tagName" placeholder="标签名称（支持系统自带emoji😜）"></a-input>
            </a-form-item>

            <a-form-item name="sort" label="排序">
                <a-input-number v-model:value="tagForm.sort" placeholder="请输入顺序（用于首页导航排序）" style="width: 100%"></a-input-number>
            </a-form-item>

            <a-form-item style="margin-bottom: 0px">
                <div style="margin: 0px auto;width: 90px;">
                    <a-button type="primary" html-type="submit" class="login-form-button">
                        <form-outlined /> 保存标签
                    </a-button>
                </div>
            </a-form-item>
        </a-form>

    </a-modal>


</template>
<script>
    import { computed, defineComponent } from 'vue';
    import {get, post,remove} from "@/utils/request";
    import {message,Modal} from "ant-design-vue";
    import NProgress from "nprogress";

    export default defineComponent({
        components: {},
        data(){
            return {
                searchForm: {
                    tagName: "",
                },
                tagForm:{
                    tagId:'',
                    tagName:"",
                    sort:1
                },
                tagFormRules: {
                    tagName: [
                        {
                            required: true,
                            message: "你还没输入标签名称",
                            trigger: 'blur'
                        }
                    ],
                    sort: [
                        {
                            required: true,
                            message: "这个也还没输入",
                            trigger: 'blur'
                        }
                    ]
                },
                columns: [
                    {
                        title: '序号',
                        key: 'number',
                        width:60,
                        align:'center'
                    },
                    {
                        title: '标签名称',
                        dataIndex: 'tagName',
                        key: 'tagName',
                    },
                    {
                        title: '顺序',
                        dataIndex: 'sort',
                        key: 'sort',
                        width:60,
                        align:'center'
                    },
                    {
                        title: '创建时间',
                        dataIndex: 'createTime',
                        key: 'createTime',
                        width:190,
                        align:'center'
                    },
                    {
                        title: '操作',
                        key: 'operation',
                        width:100,
                        align:'center',
                        fixed: 'right',
                    },

                ],
                dataSource: [],
                pagination:{
                    total: 0,
                    current: 1,
                    pageSize: 10,
                    onChange: (current, pageSize) => {
                        this.searchTag(current,pageSize);
                    }
                },
                saveModel:false,
                tag:"",
                deleteTagId:""
            }
        },
        created() {
            this.searchTag();
        },
        methods:{
            addTag(){
                this.saveModel = true;
                this.tag = "添加";
                this.tagForm = {
                    tagId:"",
                    tagName:"",
                    sort:1
                }
            },
            editTag(e){
                this.saveModel = true;
                this.tag = "编辑";
                this.tagForm = {
                    tagId:e.tagId,
                    tagName:e.tagName,
                    sort:e.sort
                }
            },
            searchTag(pageNo,pageSize){
                if(typeof pageNo == "undefined" || pageNo == "" ){
                    pageNo = this.pagination.current
                }
                if(typeof pageSize == "undefined" || pageSize == "" ){
                    pageSize = this.pagination.pageSize
                }
                this.NProgress.start()
                get("/t-tag/list?tagName="+this.searchForm.tagName+"&pageNo="+pageNo+"&pageSize="+pageSize).then((res)=>{
                    this.dataSource = res.data.records
                    this.pagination.total = res.data.total
                    this.pagination.current = pageNo;
                    this.NProgress.done()
                })
            },
            submitData(){
                this.NProgress.start()
                post("/t-tag/saveOrUpdate",this.tagForm).then((res)=>{
                    this.searchTag();
                    this.saveModel = false
                    message.success("标签已保存");
                    this.NProgress.done()
                },(err) => {

                })
            },
            recordRowKey(e){
                this.deleteTagId = e;
            },
            deletes(){
                var that = this;
                if(this.deleteTagId.length == 0){
                    message.warning('你还没选择要删除的标签呀');
                    return false;
                }
                Modal.confirm({
                    title: '你确定要删除这些标签吗？相关资源标签也会被清除掉，不可恢复哦！',
                    okText:'嗯，确定',
                    cancelText:'我点错啦',
                    onOk() {
                        that.NProgress.start()
                        remove("/t-tag/delete?tagIds="+that.deleteTagId).then((res)=>{
                            that.searchTag();
                            that.deleteTagId=[]
                            message.success("标签删除完成");
                            that.NProgress.done()
                        },(err) => {

                        })
                    },
                });


            },
            share(e){
                this.$pub.copyText(window.location.origin+"/"+e.tagId,"分享链接："+window.location.origin+"/"+e.tagId+" 已复制~");
            }
        },


    });
</script>
<style >

</style>
