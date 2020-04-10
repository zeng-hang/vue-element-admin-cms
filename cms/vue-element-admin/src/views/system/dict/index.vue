<template>
    <div class="app-container">
        <el-row :gutter="20">
            <el-col :span="6">
                <catalog ref="catalog" :list="catalog" @handle-click="handleCatalogClick">
                    <div style="overflow: hidden; clear: both;">
                        字典目录
                        <el-button-group style="float: right">
                            <el-tooltip effect="light" content="编辑" placement="top">
                                <el-button type="primary"
                                           icon="el-icon-edit"
                                           @click="handleCatalogEdit"
                                           v-if="$checkPermission(['sys:dict:update'])"/>
                            </el-tooltip>

                            <el-tooltip effect="light" content="新增" placement="top">
                                <el-button type="primary"
                                           icon="el-icon-plus"
                                           @click="handleCatalogAdd"
                                           v-if="$checkPermission(['sys:dict:save'])"/>
                            </el-tooltip>

                            <el-tooltip effect="light" content="删除" placement="top">
                                <el-button type="danger"
                                           icon="el-icon-delete"
                                           @click="handleCatalogDelete"
                                           v-if="$checkPermission(['sys:dict:delete'])"/>
                            </el-tooltip>
                        </el-button-group>
                    </div>
                </catalog>
            </el-col>
            <el-col :span="18">
                <el-button style="margin: 10px 0;"
                           type="primary"
                           icon="el-icon-plus"
                           @click="handleAdd"
                           v-if="$checkPermission(['sys:dict:save'])">
                    新增
                </el-button>
                <el-table v-loading="tabLoading"
                          :data="tabData"
                          element-loading-text="Loading..."
                          border
                          fit
                          highlight-current-row
                          row-key="id">
                    <el-table-column align="center" label="ID" width="95" prop="dictId"/>
                    <el-table-column label="字典值" prop="label"/>
                    <el-table-column label="字典码" prop="code"/>
                    <el-table-column label="排序" prop="sortNo"/>
                    <el-table-column label="操作" width="180" align="center">
                        <template slot-scope="{row}">
                            <el-button-group>
                                <el-button type="primary"
                                           icon="el-icon-edit"
                                           v-if="$checkPermission(['sys:dict:update'])"
                                           @click="handleEdit(row)">修改
                                </el-button>
                                <el-button type="danger"
                                           icon="el-icon-delete"
                                           v-if="$checkPermission(['sys:dict:delete'])"
                                           @click="handleDelete(row.dictId)">删除
                                </el-button>
                            </el-button-group>
                        </template>
                    </el-table-column>
                </el-table>
            </el-col>
        </el-row>

        <!--  新增、修改弹出框  -->
        <el-dialog :title="dialogTitle"
                   :visible.sync="editDialogVisible"
                   width="50%"
                   :close-on-click-modal="false"
                   @open="handleDialogOpen">
            <el-form ref="form" :model="currentData" label-width="180px" :rules="rules">
                <el-form-item label="字典目录" v-if="hasCatalog" prop="parentId">
                    <el-select v-model="currentData.parentId" placeholder="请选择">
                        <el-option
                            v-for="item in catalog"
                            :key="item.dictId"
                            :label="item.label"
                            :value="item.dictId"/>
                    </el-select>
                </el-form-item>
                <el-form-item label="字典值" prop="label">
                    <el-input v-model="currentData.label"/>
                </el-form-item>
                <el-form-item label="字典码" v-if="hasCatalog" prop="code">
                    <el-input v-model="currentData.code"/>
                </el-form-item>
                <el-form-item label="排序号">
                    <el-input-number v-model="currentData.sortNo"/>
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
    import Catalog from "./components/catalog";
    import {isEmptyObject} from "../../../utils/validate";

    export default {
        name: "Dict",
        components: {Catalog},
        data() {
            return {
                tabData: [],
                tabLoading: false,
                dialogTitle: '',
                editDialogVisible: false,
                currentData: {},
                activeData: [],
                catalog: [],
                rules: {
                    parentId: [
                        {required: true, message: '请选择字典目录', trigger: 'blur'}
                    ],
                    label: [
                        {required: true, message: '请输入字典值', trigger: 'blur'}
                    ],
                    code: [
                        {required: true, message: '请输入字典码', trigger: 'blur'}
                    ]
                },
                editButtonLoading: false,
            }
        },
        created() {
            this.getCatalog()
        },
        computed: {
            hasCatalog() {
                return !this.dialogTitle.includes('字典目录');
            }
        },
        methods: {
            getDict(parentId = 0) {
                return this.$request.get(`/dict/list/${parentId}`)
            },
            getCatalog() {
                this.getDict().then(res => {
                    this.catalog = res.data;
                    if (res.data.length === 0) return;

                    const currentData = this.$refs.catalog.getCurrentData();
                    const parentId = isEmptyObject(currentData) ? res.data[0].dictId : currentData.dictId
                    this.getTabData(parentId)
                })
            },
            getTabData(parentId) {
                this.tabLoading = true;
                this.getDict(parentId).then(res => {
                    this.tabData = res.data;
                    this.tabLoading = false;
                }).catch(err => {
                    this.tabLoading = false;
                })
            },
            handleCatalogEdit() {
                this.currentData = Object.assign({}, this.$refs.catalog.getCurrentData());
                if (isEmptyObject(this.currentData)) {
                    this.$message.warning('请选择一个字典目录进行修改');
                    return;
                }

                this.editDialogVisible = true;
                this.dialogTitle = '修改字典目录';
            },
            handleCatalogAdd() {
                this.editDialogVisible = true;
                this.dialogTitle = '新增字典目录';
                this.currentData = {};
            },
            handleCatalogDelete() {
                const currentData = this.$refs.catalog.getCurrentData();
                this.handleDelete(currentData.dictId)
            },
            handleCatalogClick(item) {
                this.getTabData(item.dictId);
            },
            handleDialogOpen() {
                this.$nextTick(() => {
                    this.$refs.form.clearValidate();
                })
            },
            handleAdd() {
                this.editDialogVisible = true;
                this.dialogTitle = '新增字典项';

                this.currentData = {};

                const currentData = this.$refs.catalog.getCurrentData();
                if (!isEmptyObject(currentData)) {
                    this.$set(this.currentData, 'parentId', currentData.dictId)
                }
            },
            handleEdit(row) {
                this.dialogTitle = '修改字典项';
                this.currentData = Object.assign({}, row);
                this.editDialogVisible = true;
            },
            handleDelete(id) {
                this.$confirm('确定删除选中的记录?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$request.delete(`/dict/delete/${id}`).then(res => {
                        this.$message.success('删除成功')
                        this.getCatalog();
                    })
                })
            },
            onSubmit() {
                this.$refs.form.validate((valid) => {
                    if (valid) {
                        this.editButtonLoading = true;

                        if (this.currentData.parentId == null) {
                            this.currentData.parentId = 0;
                        }

                        const url = `/dict/${this.dialogTitle.includes('新增') ? 'save' : 'update'}`;

                        this.$request.post(url, this.currentData).then(res => {
                            this.$message.success('保存成功')
                            this.getCatalog()
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
