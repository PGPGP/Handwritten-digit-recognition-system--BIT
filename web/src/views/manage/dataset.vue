<template>

  <div class="app-container">

    <p>现有数据集</p>
    <!-- <div class="centered-table"> -->
      <el-table
        v-loading="listLoading"
        :data="list"
        element-loading-text="Loading"
        border
        fit
        highlight-current-row
      >

        <el-table-column align="center" label="序号" width="100" >
          <template slot-scope="scope">
            {{ scope.$index }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="数据集名称" >
          <template slot-scope="scope">
            {{ scope.row.title }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="创建人ID" width="100">
          <template slot-scope="scope">
            <!-- {{ scope.row.title }} -->
          </template>
        </el-table-column>
        <el-table-column label="数据量" w align="center" width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.author }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建日期"  align="center" >
          <template slot-scope="scope">
            {{ scope.row.pageviews }}
          </template>
        </el-table-column>
        <el-table-column align="center" prop="created_at" label="操作" width="100">
          <template>
            <!-- <i class="el-icon-time" />
            <span>{{ scope.row.display_time }}</span> -->
            <el-button style="font-size: 10px;padding: 5px 10px;" type="dashed" @click="onSubmit"><p style="font-size: 10px;color: rgb(15, 15, 15);">删除</p></el-button>
          </template>
        </el-table-column>
      </el-table>
    <!-- </div>   -->
    
    <p>添加数据集</p>
    <div class="app-container">
      <el-form ref="form" :model="form" label-width="120px">

        <!-- <el-form-item label="选择数字图片">
          <span>（规定图片必须为单个数字）</span>
          <el-upload
            action="D:\image"
            :headers="{'Authorization': 'Bearer ' + authToken}" 
            :show-file-list="false" 
            :on-success="handleSuccess"
          >
            <el-button type="primary">上传图片</el-button>
          </el-upload>
          <img v-if="imageUrl" :src="imageUrl" alt="Uploaded Image" />
         
            <el-button type="primary" @click="uploadAndNavigate" >标注标签</el-button>
        </el-form-item> -->
        
        <el-form-item>
          <el-upload
              class="upload-demo"
              action="#"
              :on-remove="fileRemove"
              :on-change="fileChang"
              accept=""
              :auto-upload="false"
              :multiple="true"
              :file-list="form.instFilePics"
              ref="uploadFile">
              <el-button size="small" type="primary" >
                <i class="el-icon-folder-opened"></i>
              </el-button>
          </el-upload>


          <!-- <div v-if="uploadingImages.length > 0 && currentImageIndex < uploadingImages.length">
            <el-row type="flex" justify="center">
              <el-col :span="6">
                <div class="image-preview-container">
                  <img :src="uploadingImages[currentImageIndex].image" alt="已上传的图片" class="preview-image" />
                </div>
              </el-col>
              <el-col :span="6">
                <div v-if="labeling">
                  <el-button v-for="num in 10" :key="num" @click="selectLabel(num)">{{ num }}</el-button>
                </div>
              </el-col>
            </el-row>
          </div> -->

          <div v-if="uploadingImages.length > 0 && currentImageIndex < uploadingImages.length">
            <div class="image-preview-container">
              <img :src="uploadingImages[currentImageIndex].image" alt="已上传的图片" class="preview-image" />
            </div>
            <div class="number-options">
              <div v-if="labeling">
                <!-- <el-button v-for="num in 10" :key="num" @click="selectLabel(num)">{{ num }}</el-button> -->
                  <div
                    v-for="num in 10"
                    :key="num"
                    class="number-option"
                    @click="selectNumber(num)"
                    :class="{ selected: selectedNumber === num }"
                  >
                    {{ num }}
                  </div>
                  <div class="buttons">
                  <button @click="deleteImage">删除</button>
                  <button @click="confirmSelection" :disabled="selectedNumber === null">确定</button>
                  </div>
              </div>
            </div>
          </div>
              

        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="startLabeling">打标签</el-button>
        </el-form-item>

        <!--批量上传图片-->
        <!-- <el-form-item label="图片" :required="form.postsType !== '2'">
              <el-upload
                action=""
                list-type="picture-card"
                :auto-upload="false"
                :limit="9"
                :before-upload="beforeUpload"
                :on-change="handleChange"
                :on-preview="handlePictureCardPreview"
                :disabled="disabled"
                :on-remove="handleRemove">
                <i class="el-icon-plus"></i>
              </el-upload>
              <div style="font-size: 12px;color: #666;">
                只能上传jpg/png文件,且不超过 2MB,最多上传 9 张图片
              </div>
              <el-dialog :visible.sync="dialogVisible">
                <img width="100%" :src="dialogImageUrl" alt="">
              </el-dialog>
          </el-form-item> -->

        <el-form-item label="数据集命名">
          <el-input v-model="form.name" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSubmit">提交数据集</el-button>
          <el-button @click="onCancel"><a style="color: rgb(15, 15, 15);">取消</a></el-button>
        </el-form-item>

        <!-- <el-form-item>
          <template>
              <div class="image-preview-container">
        
                <div class="number-options">
                  <div
                    v-for="num in 10"
                    :key="num"
                    class="number-option"
                    @click="selectNumber(num)"
                    :class="{ selected: selectedNumber === num }"
                  >
                    {{ num }}
                  </div>
                </div>
            
                
                <div class="buttons">
                  <button @click="deleteImage" :disabled="!selectedImage">删除</button>
                  <button @click="confirmSelection" :disabled="selectedNumber === null">确定</button>
                </div>
              </div>
          </template>
        </el-form-item> -->

      </el-form>
    </div>
      

  </div> 

</template>

<style>
.centered-table {
  display: flex;
  justify-content: center;
  align-items: center;
  /* width: 40cm; */
};

</style>

<script>
import { getList } from '@/api/table'

export default {

  // filters: {
  //   statusFilter(status) {
  //     const statusMap = {
  //       published: 'success',
  //       draft: 'gray',
  //       deleted: 'danger'
  //     }
  //     return statusMap[status]
  //   }
  // },
  data() {
    return {
      form: {
        name: '',
        region: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        desc: '',
        instFilePics:[],
			  instFile:[]
      },
      list: null,
      listLoading: true,
      authToken: 'your-auth-token', 
      imageUrl: null,
      dialogImageUrl: '',//窗口预览图片路径
      dialogVisible: false,//预览窗口是否打开
      disabled: false,//是否禁用上传操作
      selectedImage: null, // 选中的图片
      selectedNumber: null, // 选中的数字

      uploadingImages: [], // 保存上传的图片
      currentImageIndex: 0, // 当前处理的图片索引
      labeling: false, // 是否处于标签选择状态
      selectedLabels: [] // 保存标签信息
      

    }
  },
  created() {
    this.fetchData()
  },
  mounted() {
	//以下代码，有时候可能写法不同，可在控制台打印一层一层的找input，再给加webkitdirectory 属性
	this.$refs.uploadFile.$children[0].$refs.input.webkitdirectory = true;
  },

  methods: {
    handleSuccess(response, file) {
    this.uploadingImages.push({ image: URL.createObjectURL(file.raw), label: null });
    },

    startLabeling() {
      this.labeling = true;
    },

    selectLabel(label) {
      this.uploadingImages[this.currentImageIndex].label = label;
      // this.currentImageIndex++;
      // if (this.currentImageIndex < this.uploadingImages.length) {
      //   this.labeling = true;
      // } else {
      //   this.labeling = false;
      // }
    },


    confirmSelection() {
      if (this.uploadingImages[this.currentImageIndex] !=null && this.selectedNumber != null) {
        // this.uploadingImages[this.currentImageIndex].label = this.selectedNumber;
        this.currentImageIndex++; // 显示下一张图片
        

        // this.selectedImage = null;
        this.selectedNumber = null;

        

        if (this.currentImageIndex < this.uploadingImages.length) {
          // 如果还有图片需要处理，将标签选择状态设置为 true
          this.labeling = true;
        } else {
          this.labeling = false; // 否则退出标签选择状态
        }
      }
    },
    




    fetchData() {
      this.listLoading = true
      getList().then(response => {
        this.list = response.data.items
        this.listLoading = false
      })
    },
    onSubmit() {
      this.$message('submit!')
    },
    // handleSuccess(response, file) {
    //   // 处理上传成功后的回调
    //   this.imageUrl = URL.createObjectURL(file.raw);
    // },
    uploadAndNavigate() {
      // 假设已上传图片信息保存在 uploadedImage 变量中
      this.$router.push({ path: '/target-page', params: { uploadedImage: this.imageUrl } });
    },


    fileChang(file, fileList, name) {
      // 更新 instFilePics
      this.form.instFilePics = fileList;
      
      // 处理上传的文件夹中的图片
      const images = [];
      for (const file of fileList) {
        if (file.raw) {
          images.push({ image: URL.createObjectURL(file.raw), label: null });
        }
      }
      this.uploadingImages = images;
    },



    fileRemove(file, fileList, name) {
      this.form.instFilePics = fileList
    },
    selectNumber(num) {
        this.selectedNumber = num;
        this.selectLabel(num);
      },
    // 删除图片
    // deleteImage() {
    //   this.selectedImage = null;
    // },
    deleteImage() {
      if (this.uploadingImages[this.currentImageIndex] !== null) {
        this.uploadingImages.splice(this.currentImageIndex, 1);

        if (this.currentImageIndex > 0) {
          this.currentImageIndex--; // 返回到上一张图片
          this.labeling = true; // 重新进入标签选择状态
        } else {
          this.labeling = false; // 如果已经是第一张图片，退出标签选择状态
        }
      }else{
        this.labeling = false;
      }
    },


  }
}
</script>


<style scoped>
  .image-preview-container {
    text-align: center;
  }
  
  .preview-image {
    max-width: 100%;
    max-height: 300px;
    margin: 20px 0;
    border: 1px solid #ccc;
  }

  
  .number-options {
    display: flex;
    flex-wrap: nowrap;
    justify-content: center;
  }

  
  /* .number-option {
    cursor: pointer;
    margin: 10px;
    padding: 10px 20px;
    border: 1px solid #ccc;
    border-radius: 5px;
  } */

  .number-option {
  display: inline-block; /* 让按钮以行内块元素显示 */
  cursor: pointer;
  margin: 10px 9px;
  padding: 10px 15px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

  
  .number-option.selected {
    background-color: #51a0f6;
    color: #fff;
  }
  
  .buttons {
    margin-top: 20px;
  }
  
  .buttons button {
    margin: 0 10px;
  }




  /* .preview-image {
  max-width: 100%;
  max-height: 300px;
  border: 1px solid #ccc;
} */

/* .el-row {
  margin-bottom: 10px;
}

.el-col {
  text-align: center;
}

.el-button {
  margin: 5px;
} */

</style>
