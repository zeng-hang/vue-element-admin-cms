<template>
    <el-upload
        class="avatar-uploader"
        ref="upload"
        action="/file/uploads"
        :http-request="handleUpload"
        :on-success="handleSuccess"
        :show-file-list="false">
        <img v-if="img" :src="img" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"/>
    </el-upload>
</template>

<script>
    export default {
        name: "Upload",
        props: {
            img: {
                type: Blob, String,
                default() {
                    return undefined
                }
            },
            used: {
                type: String,
                required: true
            }
        },
        methods: {
            handleUpload(data) {
                let formData = new FormData();
                formData.append("file", data.file, data.file.name);

                return new Promise((resolve, reject) => {
                    this.$request.post('/file/upload', formData).then(res => {
                        this.$message.success("上传成功");
                        resolve(res);
                    }).catch(err => {
                        this.$message.error("上传失败");
                        reject()
                    })
                })
            },
            handleSuccess(response, file, fileList) {
                this.$emit('success', response.data)
            }
        }
    }
</script>
