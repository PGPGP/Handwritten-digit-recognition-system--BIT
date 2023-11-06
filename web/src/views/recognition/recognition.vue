<template>
  <div class="app-container">
    <div class="h1" :style="styleOfH1">
      <h1>
        识别手写数字
      </h1>
    </div>
    <div class="image" :style="styleOfImage">
      <div style="margin-left: 40%;margin-top: 25%;">
        <el-image>
          <div slot="error" class="image-slot">
            <i class="el-icon-picture-outline">图片预览区</i>
          </div>
        </el-image>
      </div>
    </div>
    <br>
    <div class="upload" :style="styleOfUpload">
      <el-upload
        class="upload-demo"
        drag
        action="https://jsonplaceholder.typicode.com/posts/"
        multiple>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
      </el-upload>
      <div class="button" style="margin-left: 13%">
        <el-button type="info">画图</el-button>
        <el-button type="info">识别</el-button>
      </div> 
    </div>
    <div class="h1" :style="styleOfH1">
      <h1>
        识别结果
      </h1>
    </div>
    <div>
      <div class="image2" :style="styleOfImage2">
        <div style="margin-left: 40%;margin-top: 25%;">
          <el-image>
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline">图片预览区</i>
            </div>
          </el-image>
        </div>
      </div>
      <div class="output" :style="styleOfOutput">
        <div style="margin-left: 2%;margin-top: 2%;">
          识别出的数字为：
        </div>
      </div>
    </div>
    <br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
  </div>
</template>
<!--最后并排的框目前有bug，包括整体缩放都会有bug，目前只是看起来没毛病了-->
<script>
export default {
  data(){
    return{
      styleOfH1:{
        color:'blue',
        marginLeft:'2%'
      },
      styleOfImage:{
        border:'1px solid silver',
        marginLeft:'30%',
        marginRight:'30%',
        height:'300px'
      },
      styleOfUpload:{
        marginLeft:'36%'
      },
      styleOfImage2:{
        border:'1px solid silver',
        marginLeft:'5%',
        marginRight:'55%',
        height:'300px',
        position:'absolute',
        width:'40%'
      },
      styleOfOutput:{
        border:'1px solid silver',
        marginLeft:'55%',
        marginRight:'5%',
        height:'300px',
        position:'absolute',
        width:'40%'
      }
    }
  }
}
</script>

<style>
  
</style>