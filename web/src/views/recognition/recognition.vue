<template>
  <div class="app-container">
    <div>
      <h1 class="h1" :style="styleOfH1">
        识别手写数字
      </h1>
      <div class="image-preview-container">
        <div class="preview-wrapper" :class="{ 'empty-preview': !selectedImage }">
          <img v-if="selectedImage" :src="getImageUrl(selectedImage)" alt="待识别图片" class="preview-image">
          <div v-else class="empty-preview-text">待识别图片</div>
        </div>
      </div>
      <br>
      <div class="button-container">
        <el-upload
          class="upload-demo"
          :before-upload="handleBeforeUpload"
          :show-file-list="false"
        >
          <div class="button-container">
            <el-button size="middle" type="primary">选择文件</el-button>
          </div>
        </el-upload>
      </div>
      <br>
      <div class="button-container">
        <el-button @click="recognize" size="middle" type="primary">识别</el-button>
      </div>
    </div>

    <div>
      <h1 class="h1" :style="styleOfH1">
        识别结果
      </h1>
      <div class="result-container">
        <div class="image-preview-container">
          <div class="preview-wrapper" :class="{ 'empty-preview': !this.recognizedImage }">
            <img v-if="this.recognizedImage" :src="base64ToUrl()" alt="识别结果图片" class="preview-image">
            <div v-else class="empty-preview-text">识别结果图片</div>
          </div>
        </div>
        <table> </table>
        <table> </table>
        <table> </table>
        <div class="text-preview-container">
          <div>
            识别结果文本：
          </div>
          <div class="text-preview">
            {{ recognizedText }}
          </div>
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
    handleBeforeUpload(file) {
      if (!file.type.startsWith('image/')) {
        this.$message.error('请选择图片文件！')
        return false
      }
      const reader = new FileReader()
      reader.onload = (e) => {
        this.selectedImageUrl = e.target.result
        this.selectedImagePostfix = '.' + file.name.split('.').pop()
      }
      reader.readAsDataURL(file)
      this.selectedImage = file
      return true
    },
    recognize() {
      if (!this.selectedImage) {
        this.$message('请先选择图片!')
        return
      }

      const formData = new FormData()
      formData.append('file', this.selectedImage)
      formData.append('postfix', this.selectedImagePostfix)
      axios.post('http://localhost:8080/use/image', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }).then(response => {
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
        return ''
      }
    },
    base64ToUrl() {
      return `data:image/jpg;base64,${this.recognizedImage}`
    }
  }
}
</script>

<style>
  .result-container {
    display: flex;
    align-items: center; /* 使内容垂直居中 */
    justify-content: center; /* 使内容水平居中 */
  }

  .text-preview-container {
    height: 320px;
    width: 500px;
    border: 2px solid black; /* 为 recognizedText 添加黑色边框 */
    padding: 10px;
    margin-right: 10px; /* 可调整 recognizedText 和图片之间的间距 */
  }

  .text-preview {
    overflow: auto;
    font-size: 16px;
    color: black;
  }
  .button-container {
    text-align: center;
  }
  .image-preview-container {
    text-align: center;
  }
  .preview-wrapper {
    max-width: 100%;
    max-height: 320px;
    height: 320px;
    width: 500px;
    margin: 10px 0;
    text-align: center;
    border: 2px solid black; /* 设置黑边框 */
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    margin: auto; /* 水平居中 */
  }
  .empty-preview-text {
    opacity: 0.5;
    font-size: 16px;
    color: black;
  }
  .preview-image {
    max-width: 100%;
    max-height: 300px;
    margin: 20px 0;
    border: 1px solid #ccc;
  }
</style>
