<template>
    <div class="app-container">
        <div class="filter-container">
            <el-button class="filter-item"
                       style="margin-left: 10px;"
                       type="primary"
                       icon="el-icon-plus"
                       @click="handleAdd">
                新增
            </el-button>
        </div>
        <el-table v-loading="tabLoading"
                  :data="tabData"
                  element-loading-text="Loading..."
                  border
                  fit
                  highlight-current-row
                  row-key="id">
            <el-table-column align="center" label="菜单ID" width="95" prop="id"/>
            <el-table-column label="菜单标题" prop="name"/>
            <el-table-column label="菜单name" width="110" align="center" prop="parentName">
                <template slot-scope="{row}">
                    <span>
                        {{ row.data.routeName }}
                    </span>
                </template>
            </el-table-column>
            <el-table-column label="图标" prop="name">
                <template slot-scope="{row}">
                    <svg-icon :iconClass="row.data.icon ? row.data.icon : ''"/>
                </template>
            </el-table-column>
            <el-table-column label="菜单类型" prop="type">
                <template slot-scope="{row}">
                    <el-tag :type="row.data.type | getMenuType">
                        {{ row.data.type === 0 ? '目录' :
                        row.data.type === 1 ? '菜单' : '按钮' }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column label="跳转地址" prop="url">
                <template slot-scope="{row}">
                    <span>
                        {{ row.data.url }}
                    </span>
                </template>
            </el-table-column>
            <el-table-column label="是否显示" prop="isHidden">
                <template slot-scope="{row}">
                    <el-tag :type="row.data.isHidden ? 'success' : 'danger'">
                        {{ row.data.isHidden ? '是' : '否' }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column label="是否一直显示根路由" prop="waysShow">
                <template slot-scope="{row}">
                    <el-tag :type="row.data.waysShow ? 'success' : 'danger'">
                        {{ row.data.waysShow ? '是' : '否' }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column label="是否显示在顶部导航" prop="isCache">
                <template slot-scope="{row}">
                    <el-tag :type="row.data.isCache ? 'success' : 'danger'">
                        {{ row.data.isCache ? '是' : '否' }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column label="排序号" width="110" align="center" prop="orderNum">
                <template slot-scope="{row}">
                    <span>
                        {{ row.data.orderNum }}
                    </span>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center">
                <template slot-scope="{row}">
                    <el-button-group>
                        <el-button type="primary"
                                   icon="el-icon-edit"
                                   v-if="$checkPermission(['sys:menu:update'])"
                                   @click="handleEdit(row)">修改
                        </el-button>
                        <el-button type="danger"
                                   icon="el-icon-delete"
                                   v-if="$checkPermission(['sys:menu:delete'])"
                                   @click="handleDelete(row.id)">删除
                        </el-button>
                    </el-button-group>
                </template>
            </el-table-column>
        </el-table>

        <!--  新增、修改弹出框  -->
        <el-dialog :title="dialogTitle" :visible.sync="editDialogVisible" width="50%" :close-on-click-modal="false">
            <el-form ref="form" :model="currentMenu" label-width="180px" :rules="rules">
                <el-form-item label="菜单类型" prop="type">
                    <!--          <el-form-item label="菜单类型" prop="type">-->
                    <el-radio-group v-model="currentMenu.type">
                        <el-radio :label="0">目录</el-radio>
                        <el-radio :label="1">菜单</el-radio>
                        <el-radio :label="2">按钮</el-radio>
                    </el-radio-group>
                    <!--          </el-form-item>-->
                </el-form-item>
                <el-form-item label="关联主菜单">
                    <el-cascader
                        clearable
                        v-model="activeMenu"
                        :options="tabData"
                        :change-on-select="true"
                        :props="{value: 'parentKey', label: 'name', children: 'children'}"/>
                </el-form-item>
                <el-form-item label="菜单标题">
                    <el-input v-model="currentMenu.title" placeholder="请填写菜单标题，主要用来展示"/>
                </el-form-item>
                <el-form-item label="菜单名称" v-if="currentMenu.type !== 2">
                    <el-input v-model="currentMenu.routeName" placeholder="请填写菜单名称，请使用英文并且不能有相同的"/>
                </el-form-item>
                <el-form-item label="权限标识">
                    <el-input v-model="currentMenu.perms" placeholder="请填写权限标识，多个请用英文逗号分割"/>
                </el-form-item>
                <el-form-item label="图标" v-if="currentMenu.type !== 2">
                    <svg-icon :iconClass="currentMenu.icon"
                              style="width: 24px; height: 24px; margin-right: 20px;"/>
                    <el-button type="primary" @click="iconDialogVisible = true">选择图标</el-button>
                </el-form-item>
                <el-form-item label="跳转地址" v-if="currentMenu.type !== 2">
                    <el-input v-model="currentMenu.url" placeholder="请为该菜单填写一个跳转地址，建议和跳转模板保持一致"/>
                </el-form-item>
                <el-form-item label="重定向地址" v-if="currentMenu.type === 0">
                    <el-input v-model="currentMenu.redirect" placeholder="重定向地址为子菜单的跳转地址"/>
                </el-form-item>
                <el-form-item label="跳转模板" v-if="currentMenu.type !== 2">
                    <el-input v-model="currentMenu.component" v-if="currentMenu.type === 1" placeholder="跳转模板为目录地址"/>
                    <el-select v-model="currentMenu.component" placeholder="请选择跳转模板" v-else-if="currentMenu.type === 0">
                        <el-option
                            v-for="item in menuDefault.component"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="是否显示" v-if="currentMenu.type !== 2">
                    <el-switch
                        v-model="currentMenu.isHidden"
                        active-text="是"
                        inactive-text="否">
                    </el-switch>
                </el-form-item>
                <el-form-item label="是否一直显示根路由" v-if="currentMenu.type !== 2">
                    <el-switch
                        v-model="currentMenu.waysShow"
                        active-text="是"
                        inactive-text="否">
                    </el-switch>
                </el-form-item>
                <el-form-item label="是否显示在顶部导航" v-if="currentMenu.type !== 2">
                    <el-switch
                        v-model="currentMenu.isCache"
                        active-text="是"
                        inactive-text="否">
                    </el-switch>
                </el-form-item>
                <el-form-item label="排序号">
                    <el-input-number v-model="currentMenu.orderNum"/>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="editDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="onSubmit" :loading="editButtonLoading">确 定</el-button>
            </div>
        </el-dialog>
        <!--  选择图标弹出框  -->
        <el-dialog title="图标库" :visible.sync="iconDialogVisible" width="50%">
            <icon-list @icon-click="onSelectIcon"/>
        </el-dialog>
    </div>
</template>

<script>
    import {deleteNullNode, findIdPath} from "@/utils/filter-tree";

    export default {
        name: "Menu",
        data() {
            return {
                tabData: [],
                tabLoading: false,
                dialogTitle: '',
                editDialogVisible: false,
                currentMenu: {},
                activeMenu: [],
                rules: {
                    name: [
                        {required: true, message: '请输入部门名称', trigger: 'blur'}
                    ]
                },
                editButtonLoading: false,
                menuTree: [],
                iconDialogVisible: false,

                menuDefault: {
                    component: [{
                        label: '基础模板',
                        value: 'Layout'
                    }, {
                        label: '空白模板',
                        value: 'AppMain'
                    }],
                    redirect: []
                }
            }
        },
        created() {
            this.getTabData();
        },
        filters: {
            getMenuType(type) {
                switch (type.toString()) {
                    case '0':
                        return 'success';
                    case '1':
                        return 'info';
                    case '2':
                        return 'warning';
                    default:
                        return '';
                }
            }
        },
        methods: {
            getTabData() {
                this.tabLoading = true;
                this.$request.get('/menu/tree').then(res => {
                    deleteNullNode(res.data);
                    this.tabData = res.data;
                    this.tabLoading = false;
                }).catch(err => {
                    this.tabLoading = false;
                })
            },
            handleAdd() {
                this.dialogTitle = '新增';
                this.currentMenu = {
                    isHidden: true,
                    waysShow: false,
                    isCache: true,
                    type: 0
                };
                this.activeMenu = [];
                this.editDialogVisible = true;
            },
            handleEdit(row) {
                this.dialogTitle = '修改';
                this.currentMenu = Object.assign({}, row.data);
                this.menuDefault.redirect = row.children;
                this.activeMenu = findIdPath(this.currentMenu.parentId, this.tabData).split(',').slice(1).reverse();
                this.editDialogVisible = true;
            },
            handleDelete(id) {
                this.$confirm('确定删除选中的记录?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$request.delete(`/menu/delete/${id}`).then(res => {
                        this.$message.success('删除成功')
                        this.getTabData();
                    })
                })
            },
            onSelectIcon(icon) {
                this.iconDialogVisible = false;
                this.currentMenu.icon = icon;
            },
            onSubmit() {
                this.$refs.form.validate((valid) => {
                    if (valid) {
                        this.editButtonLoading = true;

                        if (this.activeMenu.length === 0) {
                            this.currentMenu.parentId = 0;
                        } else {
                            //选择最后一个
                            for (let i = this.activeMenu.length - 1; i >= 0; i--) {
                                if (this.activeMenu[i]) {
                                    this.currentMenu.parentId = this.activeMenu[i];
                                    break
                                }
                            }
                        }

                        let url = '';
                        if (this.dialogTitle === '新增')
                            url = '/menu/save'
                        else
                            url = '/menu/update'

                        this.$request.post(url, this.currentMenu).then(res => {
                            this.$message.success('保存成功')
                            this.getTabData()
                            this.editButtonLoading = false;
                            this.editDialogVisible = false;
                        }).catch(err => {
                            this.editButtonLoading = false;
                            this.editDialogVisible = false;
                        })
                    }
                });
            }
        }
    }
</script>

<style scoped>

</style>
