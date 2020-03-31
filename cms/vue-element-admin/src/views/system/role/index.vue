<template>
    <div class="app-container">
        <div class="filter-container">
            <el-input v-model="params.name" placeholder="角色名" style="width: 200px;" class="filter-item"
                      @keyup.enter.native="getTabData"/>
            <el-button class="filter-item"
                       type="primary"
                       icon="el-icon-search"
                       @click="getTabData">查询
            </el-button>
            <el-button class="filter-item"
                       style="margin-left: 10px;"
                       type="primary"
                       icon="el-icon-plus"
                       v-if="$checkPermission(['sys:role:save'])"
                       @click="handleAdd">
                新增
            </el-button>
        </div>
        <el-table v-loading="tabLoading"
                  :data="tabData"
                  element-loading-text="Loading..."
                  border
                  fit
                  highlight-current-row>
            <el-table-column align="center" label="角色ID" width="95" prop="roleId"/>
            <el-table-column label="角色名称" prop="name"/>
            <el-table-column label="角色标识" prop="role"/>
            <el-table-column label="备注" align="center" prop="remark"/>
            <el-table-column label="创建时间" width="180" align="center" prop="createTime"/>
            <el-table-column label="操作" width="180" align="center">
                <template slot-scope="scope">
                    <el-button-group>
                        <el-button type="primary" icon="el-icon-edit"
                                   v-if="$checkPermission(['sys:role:update'])"
                                   @click="handleEdit(scope.row)">修改
                        </el-button>
                        <el-button type="danger" icon="el-icon-delete"
                                   v-if="$checkPermission(['sys:role:delete'])"
                                   @click="handleDelete(scope.row.roleId)">删除
                        </el-button>
                    </el-button-group>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="params.page"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="10"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>

        <!-- 新增、修改弹出框 -->
        <el-dialog :title="dialogTitle"
                   :visible.sync="editDialogVisible"
                   width="50%"
                   :close-on-click-modal="false"
                   top="30px">
            <el-card class="box-card">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <h3 style="text-align: center;">基础信息</h3>
                        <el-form ref="form" :model="currentRole" label-width="80px" :rules="rules">
                            <el-form-item label="角色名称" prop="name">
                                <el-input v-model="currentRole.name"/>
                            </el-form-item>
                            <el-form-item label="角色名称" prop="role">
                                <el-input v-model="currentRole.role" :maxlength="20"/>
                            </el-form-item>
                            <el-form-item label="备注" prop="remark">
                                <el-input v-model="currentRole.remark"/>
                            </el-form-item>
                        </el-form>
                    </el-col>
                    <el-col :span="12" style="border-left: solid 1px #dcdfe6;">
                        <h3 style="text-align: center;">权限列表</h3>
                        <el-scrollbar wrap-class="scrollbar-wrapper" style="height: 500px;">
                            <el-tree
                                :data="menu"
                                show-checkbox
                                default-expand-all
                                node-key="id"
                                ref="tree"
                                :props="{label: 'name', children: 'children'}"
                                highlight-current/>
                        </el-scrollbar>
                    </el-col>
                </el-row>
            </el-card>
            <div slot="footer" class="dialog-footer">
                <el-button @click="editDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="onSubmit" :loading="editButtonLoading">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import {deleteNullNode, findIdPath} from "@/utils/filter-tree";

    export default {
        name: "Role",
        data() {
            return {
                tabData: [],
                tabLoading: false,
                params: {
                    name: '',
                    page: 1,
                    size: 10,
                    order: 'asc'
                },
                total: 0,
                editDialogVisible: false,
                dialogTitle: '',
                editButtonLoading: false,
                currentRole: {},
                rules: {
                    name: [
                        {required: true, message: '请输入角色名', trigger: 'blur'}
                    ],
                    role: [
                        {required: true, message: '请输入角色标识', trigger: 'blur'}
                    ]
                },
                menu: [],
            }
        },
        created() {
            this.getTabData();
            this.getMenuData();
        },
        methods: {
            getTabData() {
                if (arguments.length > 0) {
                    this.params.page = 1;
                    this.params.size = 10;
                }

                this.tabLoading = true;
                this.$request.get('/role/list', {params: this.params}).then(({data}) => {
                    this.tabLoading = false;
                    this.tabData = data.records;
                    this.total = data.total;
                }).catch(err => {
                    this.tabLoading = false;
                })

            },
            handleSizeChange(size) {
                this.params.size = size;
                this.getTabData();
            },
            handleCurrentChange(page) {
                this.params.page = page;
                this.getTabData();
            },
            handleEdit(row) {
                this.dialogTitle = '修改';
                this.editDialogVisible = true;
                this.$request.get(`/role/info/${row.roleId}`).then(({data}) => {
                    this.currentRole = data;
                    this.$refs.tree.setCheckedKeys(data.menuIdList);
                })
            },
            handleAdd() {
                this.dialogTitle = '新增';
                this.currentRole = {};
                this.editDialogVisible = true;

                this.$nextTick(() => {
                    this.$refs.tree.setCheckedKeys([]);
                })
            },
            handleDelete(roleId) {
                this.$confirm('确定删除选中的记录?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$request.delete(`/role/delete/${roleId}`).then(res => {

                        this.$message.success('删除成功')
                        this.getTabData();
                    })
                }).catch(() => {
                });
            },
            getMenuData() {
                this.$request({
                    url: '/menu/tree',
                    method: 'get'
                }).then(({data}) => {
                    deleteNullNode(data);
                    this.menu = data;
                });
            },
            onSubmit() {
                this.$refs.form.validate((valid) => {
                    if (valid) {
                        this.editButtonLoading = true;

                        //获取选中的权限
                        this.currentRole.menuIdList = this.$refs.tree.getCheckedKeys()

                        let url = '';
                        if (this.dialogTitle === '新增')
                            url = '/role/save'
                        else
                            url = '/role/update'

                        this.$request.post(url, this.currentRole).then(res => {
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
