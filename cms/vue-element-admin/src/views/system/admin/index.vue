<template>
    <div class="app-container">
        <div class="filter-container">
            <el-input v-model="params.userName" placeholder="请输入用户名" style="width: 200px;" class="filter-item"/>
            <el-input v-model="params.nickname" placeholder="请输入昵称" style="width: 200px;" class="filter-item"/>
            <el-input v-model="params.phone" placeholder="请输入手机号" style="width: 200px;" class="filter-item"/>
            <el-input v-model="params.mail" placeholder="请输入邮箱" style="width: 200px;" class="filter-item"/>
            <el-button class="filter-item" type="primary" icon="el-icon-search" @click="getTabData">查询</el-button>
            <el-button class="filter-item"
                       style="margin-left: 10px;"
                       type="primary"
                       icon="el-icon-plus"
                       v-if="$checkPermission(['sys:user:save'])"
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
            <el-table-column label="用户ID" width="100" align="center" prop="userId"/>
            <el-table-column label="用户名" align="center" prop="userName"/>
            <el-table-column label="昵称" align="center" prop="nickname"/>
            <el-table-column label="邮箱" align="center" prop="mail"/>
            <el-table-column label="手机号" align="center" prop="phone"/>
            <el-table-column label="状态" width="110" align="center">
                <template slot-scope="{row}">
                    <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                        {{ row.status === 1 ? '启用' : '停用' }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column align="center" prop="createTime" label="创建时间"/>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <el-button-group>
                        <el-button type="primary"
                                   icon="el-icon-edit"
                                   v-if="$checkPermission(['sys:user:update'])"
                                   @click="handleEdit(scope.row)">修改
                        </el-button>
                        <el-button type="danger"
                                   icon="el-icon-delete"
                                   v-if="$checkPermission(['sys:user:delete'])"
                                   @click="handleDelete(scope.row.userId)">删除
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
        <el-dialog :title="dialogTitle" :visible.sync="editDialogVisible" width="40%" :close-on-click-modal="false">
            <el-form ref="form" :model="currentUser" label-width="120px" :rules="rules">
                <img-cropper :img-src="currentUser.avatar" @upload-img="upLoadImg"/>
                <el-form-item label="用户名" prop="userName">
                    <el-input v-model="currentUser.userName" placeholder="请输入用户名"/>
                </el-form-item>
                <el-form-item label="昵称" prop="nickname">
                    <el-input v-model="currentUser.nickname" placeholder="请输入昵称"/>
                </el-form-item>
                <el-form-item label="密码" prop="password" v-if="currentUser.userId == null">
                    <el-input v-model="currentUser.password" placeholder="请输入密码"/>
                </el-form-item>
                <el-form-item label="性别" prop="gender">
                    <el-radio v-model="currentUser.gender" :label="1">男</el-radio>
                    <el-radio v-model="currentUser.gender" :label="0">女</el-radio>
                </el-form-item>
                <el-form-item label="住址" prop="address">
                    <el-input v-model="currentUser.address" placeholder="请输入住址"/>
                </el-form-item>
                <el-form-item label="生日" prop="birthday">
                    <el-date-picker
                        v-model="currentUser.birthday"
                        type="date"
                        format="yyyy-MM-dd"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        placeholder="请选择生日"/>
                </el-form-item>
                <el-form-item label="邮箱" prop="mail">
                    <el-input v-model="currentUser.mail" placeholder="请输入邮箱"/>
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                    <el-input v-model="currentUser.phone" placeholder="请输入手机号"/>
                </el-form-item>
                <el-form-item label="角色">
                    <el-checkbox-group v-model="currentUser.rolesList">
                        <el-checkbox v-for="r in role" :label="r.roleId" :key="r.roleId">{{r.name}}</el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
                <el-form-item label="状态">
                    <el-radio-group v-model="currentUser.status">
                        <el-radio :label="0">禁用</el-radio>
                        <el-radio :label="1">正常</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="editDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="onSubmit" :loading="editButtonLoading">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import ImgCropper from "../../../components/ImgCropper/ImgCropper";

    export default {
        name: "Admin",
        components: {ImgCropper},
        data() {
            return {
                tabData: [],
                tabLoading: false,
                params: {
                    userName: '',
                    nickname: '',
                    phone: '',
                    mail: '',
                    page: 1,
                    size: 10
                },
                total: 0,
                editDialogVisible: false,
                dialogTitle: '',
                editButtonLoading: false,
                currentUser: {},
                currentDept: [],
                dept: [],
                role: [],
                rules: {
                    userName: [
                        {required: true, message: '请输入用户名', trigger: 'blur'}
                    ],
                    nickname: [
                        {required: true, message: '请输入昵称', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '请输入密码', trigger: 'blur'}
                    ],
                    mail: [
                        {type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur'}
                    ],
                    phone: [
                        {required: true, message: '请填写手机号', trigger: 'blur'}
                    ]
                }
            }
        },
        created() {
            this.getTabData();
            this.getRoleData();
        },
        methods: {
            getTabData() {
                if (arguments.length > 0) {
                    this.params.page = 1;
                    this.params.size = 10;
                }

                this.tabLoading = true;
                this.$request.get('/user/list', {params: this.params}).then(({data}) => {
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
                if (row.rolesList == null) {
                    this.$request.get(`/user/role/list/${row.userId}`).then(({data}) => {
                        row.rolesList = []
                        this.currentUser = Object.assign({}, row);
                    })
                }
                this.currentUser = Object.assign({}, row);

                this.currentUser.rolesList = [];
                this.editDialogVisible = true;
            },
            handleAdd() {
                this.dialogTitle = '新增';
                this.currentUser = {
                    rolesList: []
                };
                this.currentDept = [];
                this.editDialogVisible = true;
            },
            handleDelete(userId) {
                this.$confirm('确定删除选中的记录?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$request.delete(`/user/delete/${userId}`).then(res => {
                        this.$message.success('删除成功')
                        this.getTabData();
                    })
                })
            },
            getRoleData() {
                this.$request.get('/role/list', {params: {page: 1, size: 100}}).then(({data}) => {
                    this.role = Object.freeze(data.records);
                });
            },
            onSubmit() {
                this.$refs.form.validate((valid) => {
                    if (valid) {
                        this.editButtonLoading = true;
                        let url;
                        if (this.dialogTitle === '新增')
                            url = '/user/save'
                        else
                            url = '/user/update'

                        this.$request.post(url, this.currentUser).then(res => {
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
            },
            upLoadImg(data) {
                const params = new FormData();
                params.append('file', data);
                params.append("used", "avatar")

                this.$request.post("/file/upload", params).then(res => {
                    this.currentUser.avatar = res.data;
                })
            }
        }
    }
</script>

<style scoped>

</style>
