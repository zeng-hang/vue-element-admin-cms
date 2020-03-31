<template>
    <div>
        <el-input v-model="filterText" placeholder="请输入图标名称以供查询"/>
        <div style="border: 1px #DCDFE6 solid; border-top: none;">
            <ul class="icon-ul">
                <li v-for="icon in filterList" :key="icon" class="icon-li" @click="handleClick(icon)" :title="icon">
                    <svg-icon :iconClass="icon" :className="icon"/>
                    <br/>
                    {{ icon }}
                </li>
                <li v-show="!filterText" class="icon-li" @click="handleClick('')"
                    style="padding:0; line-height: 56px; vert-align: middle; font-size: 20px; font-weight: 600;">
                    无
                </li>
            </ul>
        </div>
    </div>
</template>

<script>
    export default {
        name: "IconList",
        data() {
            return {
                iconList: [],
                filterText: ''
            }
        },
        computed: {
            filterList() {
                if (this.filterText)
                    return this.iconList.filter(icon => icon.toLocaleLowerCase().includes(this.filterText.toLocaleLowerCase()))
                else
                    return this.iconList
            }
        },
        created() {
            this.iconList = require.context('@/icons/svg', false, /\.svg$/).keys().map(icon => {
                return icon.replace(/\.svg$/, '').replace(/\.\//, '')
            })
        },
        methods: {
            handleClick(icon) {
                this.$emit('icon-click', icon)
            }
        }
    }
</script>

<style lang="scss" scoped>
    @import "~@/styles/variables.scss";

    .icon-ul {
        clear: both;
        overflow: hidden;
        text-align: center;
        margin-top: 0;
        padding-top: 14px;
    }

    .icon-li {
        list-style: none;
        float: left;
        padding: 5px;
        margin: 5px;
        border-radius: 5px;
        border: 1px $menuText solid;
        cursor: pointer;
        width: 64px;
        height: 56px;
        text-overflow: ellipsis;
        -webkit-box-orient: vertical;
        white-space: nowrap;
        overflow: hidden;
    }

    .icon-li:hover, .icon-li:active {
        background-color: $tiffany;
    }

    .svg-icon {
        width: 24px;
        height: 24px;
    }
</style>
