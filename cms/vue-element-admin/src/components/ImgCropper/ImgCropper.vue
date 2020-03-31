<template>
    <div class="container">
        <div class="crop-demo">
            <div style="min-height: 102px;">
                <label class="crop-demo-btn" for="image">
                    <img v-if="imgSrc" :src="imgSrc" ref="img" class="pre-img" alt="">
                    <img v-else src="@/assets/other_images/avatar.jpg" class="pre-img" alt="">
                </label>
            </div>
            <input class="crop-input" type="file" id="image" name="image" accept="image/*" @change="setImage"/>
        </div>

        <el-dialog title="裁剪图片"
                   :visible.sync="dialogVisible"
                   width="30%"
                   append-to-body>
            <vue-cropper ref='cropper'
                         :img="cropImg"
                         centerBox
                         :fixed="false"
                         autoCrop
                         style="width:100%;height:300px;"/>
            <div slot="footer" class="dialog-footer">
                <el-button @click="cancelCrop">取 消</el-button>
                <el-button type="primary" @click="uploadImg">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import {VueCropper} from 'vue-cropper';

    export default {
        name: "ImgCropper",
        components: {
            VueCropper
        },
        props: {
            imgSrc: {
                type: [String, Blob],
                default() {
                    return require('@/assets/other_images/avatar.jpg');
                }
            },
            imgType: {
                type: String,
                default() {
                    return "blob"
                }
            }
        },
        data() {
            return {
                dialogVisible: false,
                cropImg: ''
            }
        },
        methods: {
            setImage(e) {
                const file = e.target.files[0];
                if (!file.type.includes('image/')) {
                    this.$message.warning("只能选择图片文件");
                    return;
                }
                const reader = new FileReader();
                reader.onload = (event) => {
                    this.dialogVisible = true;
                    this.cropImg = event.target.result;

                    this.$refs.cropper && this.$refs.cropper.replace(event.target.result);
                };
                reader.readAsDataURL(file);
            },
            cancelCrop() {
                this.dialogVisible = false;
            },
            uploadImg() {
                if (this.imgType === "blob") {
                    // 获取截图的blob数据
                    this.$refs.cropper.getCropBlob((data) => {
                        this.$emit('upload-img', data);
                    });
                } else {
                    // 获取截图的base64 数据
                    this.$refs.cropper.getCropData((data) => {
                        this.$emit('upload-img', data);
                    }, "image/jpeg");
                }

                this.dialogVisible = false;
            }
        }
    }
</script>

<style scoped>
    .container {
        margin: 5px;
    }

    .pre-img {
        width: 100px;
        height: 100px;
        background: #f8f8f8;
        border: 1px solid #eee;
        border-radius: 5px;
    }

    .crop-demo {
        text-align: center;
    }

    .crop-demo-btn {
        cursor: pointer;
    }


    .crop-input {
        display: none;
    }
</style>
