<template>
    <el-card class="box-card">
        <slot slot="header"/>
        <div id="catalog">
            <div v-for="(l,index) in list"
                 :key="index"
                 class="text item"
                 v-text="l.label"
                 @click="handleClick(l, $event)"/>
        </div>
    </el-card>
</template>

<script>
    export default {
        name: "Catalog",
        props: {
            list: {
                required: true,
                type: Array
            }
        },
        data() {
            return {
                currentData: {}
            }
        },
        methods: {
            handleClick(item, evn) {
                let doms = document.getElementById('catalog').getElementsByClassName('active');
                for (let dom of doms) dom.classList.remove('active')
                evn.target.classList.add('active');

                this.currentData = item;
                this.$emit('handle-click', item, evn);
            },
            getCurrentData() {
                return this.currentData;
            }
        }
    }
</script>

<style scoped>
    .text {
        font-size: 14px;
    }

    .item {
        height: 40px;
        line-height: 40px;
        text-align: center;
        border: 1px solid #dfe6ec;
        border-radius: 5px;
        margin: 10px 0;
        cursor: pointer;
        transition: background-color .5s;
    }

    .item:hover {
        background-color: #f5f7fa;
    }

    .active {
        background-color: #f5f7fa;
    }
</style>
