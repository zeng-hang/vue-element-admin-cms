<template>
    <el-upload
        class="upload-demo"
        ref="upload"
        action="/file/uploads"
        :http-request="handleUpload"
        :on-success="handleSuccess"
        :file-list="fileList"
        :auto-upload="false"
        :limit="10"
        :on-exceed="handleExceed"
        multiple>
        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
        <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
    </el-upload>
</template>

<script>
    export default {
        name: "Upload",
        data() {
            return {
                fileList: [],
                formData: new FormData(),
                uploadFlag: null,
                uploadList: [],
                uploadTimer: []
            };
        },
        methods: {
            submitUpload() {
                this.$refs.upload.submit();
            },
            handleUpload(data) {
                this.formData.append("files", data.file, data.file.name);

                return new Promise((resolve, reject) => {
                    setTimeout(() => {
                        resolve();
                    }, 0)
                });
            },
            handleExceed() {
                this.$message.warning("最多一次只能上传10个文件");
            },
            handleSuccess(response, file, fileList) {
                //在这里设置上传中状态
                file.status = 'uploading';
                file.percentage = 0;

                let timer = setInterval(() => {
                    //可以在这里访问后台查看上传状态
                    if (file.percentage === 99) {
                        return;
                    }
                    file.percentage += 1;
                }, 300);
                this.uploadTimer.push({timer, file});

                clearInterval(this.uploadFlag);
                this.uploadFlag = setTimeout(() => {
                    this.$request.post('/file/uploads', this.formData).then(res => {
                        this.$message.success(res.msg);

                        let failList = res.failList || [];

                        this.uploadTimer.forEach(timer => {
                            //清除定时器
                            clearInterval(timer.timer);
                            timer.file.percentage = 100;

                            //延时可以看到加载到100的动画效果
                            setTimeout(() => {
                                if (failList.includes(timer.file.name)) {
                                    timer.file.status = 'fail'
                                } else {
                                    timer.file.status = 'success'
                                }

                            }, 300)
                        })

                        this.$emit("on-success")
                    }).catch(err => {
                        this.uploadTimer.forEach(timer => {
                            clearInterval(timer);
                            timer.file.status = 'fail'
                        })

                        this.$emit("on-error")
                    })
                }, 300)
            },
            clearFiles() {
                this.$refs.upload.clearFiles()
            }
        }
    }
</script>

<style scoped>

</style>
