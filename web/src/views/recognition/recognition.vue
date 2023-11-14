<template>
  <div class="app-container">
    <div>
      <h1 class="h1" :style="styleOfH1">
        识别手写数字
      </h1>
    <div class="image-preview-container">
      <div >
          <img :src="getImageUrl(this.selectedImage)" alt="待识别图片" class="preview-image">
        </div>
    </div>
      <div class="file-input-container">
        <el-upload
          class="upload-demo"
          :before-upload="handleBeforeUpload"
          :show-file-list="false"
        >
          <el-button size="small" type="primary" >选择文件</el-button>
        </el-upload>
      </div>
      <el-button @click="recognize" size="small" type="primary" >识别</el-button>
    </div>

    <div>
      <h1  class="h1" :style="styleOfH1">
        识别结果
      </h1>
      <div class="image-preview-container">
      <div >
          <img :src="base64ToUrl()" alt="识别结果图片" class="preview-image">
        </div>
    </div>
      <div class="result-container">
        <div class="text-preview">
          {{ this.recognizedText }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      styleOfH1: {
        color: 'blue',
        marginLeft: '2%'
      },
      selectedImage: null,
      selectedImagePostfix: '',
      selectedImageUrl: '',
      recognizedImage: '',
      recognizedText: ''

    }
  },
  methods: {
    // 在上传之前的钩子，用于保存文件
    handleBeforeUpload(file) {
      const reader = new FileReader()
      reader.onload = (e) => {
        this.selectedImageUrl = e.target.result
        this.selectedImagePostfix = '.' + file.name.split('.').pop()
      }
      reader.readAsDataURL(file)
      this.selectedImage = file
      return true // 允许上传
    },
    // 上传成功的处理方法
    // handleSuccess(response) {
    //   this.recognizedImage = response.imageUrl;
    //   this.recognizedText = response.text;
    // },
    // 点击识别按钮时触发上传
    recognize() {
      if (!this.selectedImage) {
        // 用户未选择图片，可以在这里添加逻辑以通知用户或执行其他操作
        this.$message('请先选择图片!')
        return
      }

      const formData = new FormData()
      formData.append('file', this.selectedImage)
      formData.append('postfix', this.selectedImagePostfix)
      // 发送 POST 请求
      axios.post('http://localhost:8080/use/image', formData, {
        headers: {
          'Content-Type': 'multipart/form-data' // 设置请求头
        }
      }).then(response => {
        console.log('收到！')
        const { file, text } = response.data
        this.recognizedImage = file
        this.recognizedText = text
      }).catch(error => {
        console.error(error)
      })
    },

    getImageUrl(img) {
      if (img && img instanceof Blob) {
        return URL.createObjectURL(img)
      } else {
        // 如果 image 不是 Blob 对象，可能需要根据实际情况返回其他 URL 或空字符串
        return ''
      }
    },

    base64ToUrl() {
      // 将Base64字符串拼接成图片的Data URL
      return `data:image/jpg;base64,${this.recognizedImage}`
    }
  }
}
</script>

<style>
  .result-container {
    display: flex;
  }

  .image-preview-container {
      text-align: center;
  }
  .preview-image {
    max-width: 100%;
    max-height: 300px;
    margin: 20px 0;
    border: 1px solid #ccc;
  }

  .text-preview {
    flex: 1;
    padding: 0 10px;
    overflow: auto;
  }

  .file-input-container {
    position: relative;
    display: inline-block;
  }
</style>
