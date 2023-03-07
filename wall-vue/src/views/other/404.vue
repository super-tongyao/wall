<template>
    <a-result status="404" title="404" sub-title="抱歉，没有找到页面。">
        <template #extra>
            <router-link to="/">
                <a-button type="primary">返回主页</a-button>
            </router-link>
        </template>
    </a-result>
</template>

<script>
    export default {
        name: "404"
    }
</script>

<style scoped>

</style>