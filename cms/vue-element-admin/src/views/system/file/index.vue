<template>
    <div class="app-container">
        <div class="filter-container">
            <el-input v-model="params.fileName" placeholder="源文件名" style="width: 200px;" class="filter-item"
                      @keyup.enter.native="getTabData"/>
            <el-input v-model="params.fileType" placeholder="文件类型" style="width: 200px;" class="filter-item"
                      @keyup.enter.native="getTabData"/>
            <el-input v-model="params.used" placeholder="用途" style="width: 200px;" class="filter-item"
                      @keyup.enter.native="getTabData"/>
            <el-button class="filter-item"
                       type="primary"
                       icon="el-icon-search"
                       @click="getTabData">查询
            </el-button>
            <el-button class="filter-item"
                       type="primary"
                       icon="el-icon-upload2"
                       @click="uploadDialogVisible = true">上传
            </el-button>
            <el-button class="filter-item"
                       type="danger"
                       icon="el-icon-delete"
                       :disabled="multipleSelection.length === 0"
                       v-if="$checkPermission(['sys:role:delete'])"
                       @click="handleBatchDelete">批量删除
            </el-button>
        </div>
        <el-table v-loading="tabLoading"
                  :data="tabData"
                  element-loading-text="Loading..."
                  border
                  fit
                  highlight-current-row
                  @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"/>
            <el-table-column align="center" label="ID" width="95" prop="fileId"/>
            <el-table-column label="文件名" prop="storageName" width="270"/>
            <el-table-column label="源文件名" prop="fileName" width="200"/>
            <el-table-column label="文件类型" width="95" align="center" prop="fileType"/>
            <el-table-column label="文件大小" width="95" align="center" prop="size"/>
            <el-table-column label="用途" width="95" align="center" prop="used"/>
            <el-table-column label="下载地址" align="center" prop="downloadUrl"/>
            <el-table-column label="创建时间" width="180" align="center" prop="createTime"/>
            <el-table-column label="操作" width="180" align="center">
                <template slot-scope="{row}">
                    <el-button-group>
                        <el-button type="primary" icon="el-icon-download"
                                   @click="handleDownload(row)">下载
                        </el-button>
                        <el-button type="danger" icon="el-icon-delete"
                                   v-if="$checkPermission(['sys:role:delete'])"
                                   @click="handleDelete(row.fileId)">删除
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
            :total="total"/>

        <!-- 文件上传弹出框 -->
        <el-dialog title="文件上传" :visible.sync="uploadDialogVisible" width="40%" :close-on-click-modal="false">
            <upload ref="custom-upload"
                    @on-success="handleUploadAfter"
                    @on-error="handleUploadAfter"/>
        </el-dialog>
    </div>
</template>

<script>

    import Upload from "../../../components/Upload/index";

    export default {
        name: "Role",
        components: {Upload},
        data() {
            return {
                tabData: [],
                tabLoading: false,
                params: {
                    fileName: '',
                    fileType: '',
                    used: '',
                    page: 1,
                    size: 10
                },
                total: 0,
                uploadDialogVisible: false,
                multipleSelection: []
            }
        },
        created() {
            this.getTabData();
        },
        methods: {
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            getTabData() {
                if (arguments.length > 0) {
                    this.params.page = 1;
                    this.params.size = 10;
                }

                this.tabLoading = true;
                this.$request.get('/file/list', {params: this.params}).then(({data}) => {
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
            handleDownload(file) {
                window.open(`${process.env.VUE_APP_SERVICE_URL}/file/download/${file.fileId}`);
                // this.$request({
                //     url: `/file/download/${file.fileId}`,
                //     methods: 'get',
                //     responseType: 'arraybuffer'
                // }).then(res => {
                //     let blob = new Blob([res], {type: fileAccept(file.type)});
                //     window.location.href = URL.createObjectURL(blob);
                // })
            },
            handleDelete(fileId) {
                this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$request.delete(`/file/delete/${fileId}`).then(res => {
                        this.$message.success(res.msg);
                        this.getTabData();
                    })
                })
            },
            handleBatchDelete() {
                if (this.multipleSelection.length === 0) {
                    this.$message.warning("请选择需要删除的数据");
                    return;
                }
                this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$request.post(`/file/batch/delete`, this.multipleSelection.map(item => item.fileId)).then(res => {
                        this.$message.success(res.msg);
                        this.getTabData();
                        this.multipleSelection = [];
                    })
                })
            },
            handleUploadAfter() {
                this.$refs['custom-upload'].clearFiles()
                this.getTabData()
                this.uploadDialogVisible = false
            }
        }
    }
</script>

<style scoped>

</style>
