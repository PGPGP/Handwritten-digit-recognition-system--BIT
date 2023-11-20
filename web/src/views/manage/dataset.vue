
<template>

  <div class="app-container">

    <p>现有数据集</p>
      <el-table 
        v-loading="listLoading"
        :data="list"
        element-loading-text="Loading"
        border
        fit
        highlight-current-row
      >

        <el-table-column align="center" label="序号" width="100" >
          <template v-slot="scope">
            {{ scope.row.dataset_id }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="数据集名称" >
          <template v-slot="scope">
            {{ scope.row.dataset_name }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="创建人ID" width="100">
          <template v-slot="scope">
            {{ scope.row.user_id }}
          </template>
        </el-table-column>
        <el-table-column label="数据量" w align="center" width="100">
          <template v-slot="scope">
            <span>{{ scope.row.image_num }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建日期"  align="center" >
          <template v-slot="scope">
            {{ formatDate(scope.row.dataset_create_date )}}
          </template>
        </el-table-column>
        <el-table-column align="center" prop="created_at" label="操作" width="100">
          <template v-slot="scope"> 
            <!-- <i class="el-icon-time" />
            <span>{{ scope.row.display_time }}</span> -->
            <el-button 
              v-if="scope.row.dataset_name !== 'mnist'"
              style="font-size: 10px;padding: 5px 10px;" type="dashed" @click="deletedataset(scope.row.dataset_id)"><p style="font-size: 10px;color: rgb(15, 15, 15);">删除</p></el-button>
          </template>
        </el-table-column>
      </el-table>
    <!-- </div>   -->
    
    <p>添加数据集</p>
    <div class="app-container">
      <el-form ref="form" :model="form" label-width="120px">

        
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



          <div v-if="uploadingImages.length > 0 && currentImageIndex < uploadingImages.length">
            <div class="image-preview-container">
              <img :src="getImageUrl()" alt="已上传的图片" class="preview-image" />
            </div>
            <div class="number-options">
              <div v-if="labeling">
                  <div
                    v-for="num in 10"
                    :key="num"
                    class="number-option"
                    @click="selectNumber(num-1)"
                    :class="{ selected: selectedNumber === num-1 }"
                  >
                    {{ num-1 }}
                  </div>
                  <div class="buttons">
                  <el-button @click="previousImage">上一张</el-button>
                  <el-button @click="deleteImage">删除</el-button>
                  <el-button @click="confirmSelection" :disabled="selectedNumber === null">确定</el-button>
                  <el-button @click="nextImage">下一张</el-button>
                  </div>
              </div>
            </div>
          </div>
              

        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="startLabeling">打标签</el-button>
        </el-form-item>



        <el-form-item label="数据集命名">
          <el-input v-model="form.name" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSubmit">提交数据集</el-button>
          <el-button @click="onCancel"><a style="color: rgb(15, 15, 15);">取消</a></el-button>
        </el-form-item>


      </el-form>
    </div>
      

  </div> 

</template>


<script>
import axios from 'axios'
import users from '@/store/users/users.vue'


export default {

  watch: {
        'form.instFilePics': {
        handler(newVal) {
            // 当 instFilePics 变化时，更新 uploadingImages
            this.uploadingImages = newVal.map(file => ({ image: file.raw, label: null }));
        },
        deep: true,
        },
    },

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
      list: [],
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
      user_id: users.user_id,
      labellist:[],
      imagelist:[]
    }
  },
  created() {
    this.fetchData()
  },

  mounted() {
	this.$refs.uploadFile.$children[0].$refs.input.webkitdirectory = true;
  console.log('hhhhh');
  },

  methods: {

    formatDate(date) {
      const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
      return new Date(date).toLocaleDateString('zh-CN', options);
    },

    previousImage() {
      if (this.currentImageIndex > 0) {
        this.currentImageIndex--;
        this.updateSelectedNumber();
      }
    },

    nextImage() {
      if (this.currentImageIndex < this.uploadingImages.length - 1) {
        this.currentImageIndex++;
        this.updateSelectedNumber();
      }
    },

    updateSelectedNumber() {
      const existingLabel = this.uploadingImages[this.currentImageIndex].label;
      if (existingLabel !== null) {
        this.selectedNumber = existingLabel;
      } else {
        this.selectedNumber = null;
      }
      this.$nextTick(() => {
        this.labeling = true;
      });
    },


    //此处应为数据集列表数据
    fetchData() {
      this.listLoading = true;
      axios.post('http://192.168.43.254:8080/request/dataset')
        .then(response => {
          this.list = response.data.datasets;
        })
        .catch(error => {
          console.error('Error fetching datasets:', error);
        })
        .finally(() => {
          this.listLoading = false;
        });
    },

    startLabeling() {
      this.labeling = true;
    },

    selectLabel(label) {
      this.uploadingImages[this.currentImageIndex].label = label;
    },


    confirmSelection() {
      if (this.uploadingImages[this.currentImageIndex] !=null && this.selectedNumber != null) {
        this.currentImageIndex++; // 显示下一张图片
        const existingLabel = this.uploadingImages[this.currentImageIndex].label;
        if (existingLabel !== null) {
          this.selectedNumber = existingLabel;
        }
        else{
          this.selectedNumber = null;
        }

        if (this.currentImageIndex < this.uploadingImages.length) {
          // 如果还有图片需要处理，将标签选择状态设置为 true
          this.labeling = true;
        } else {
          this.labeling = false; // 否则退出标签选择状态
        }
      }
    },
    
    onSubmit() {
        const formData = new FormData();

        // 遍历 uploadingImages 数组，将每张图片和对应标签添加到 formData 中
        for (let i = 0; i < this.uploadingImages.length; i++) {
            const image = this.uploadingImages[i].image;
            const label = this.uploadingImages[i].label;

            formData.append('images', image);
            formData.append('labels', label);
        }

        // 添加其他表单数据
        formData.append('user_id', this.user_id);
        formData.append('dataset_name', this.form.name);

        this.$message('数据集已提交!');

        // 发送 POST 请求
        axios.post("http://192.168.43.254:8080/manage/upload_dataset", formData, {
            headers: {
            'Content-Type': 'multipart/form-data', // 设置请求头
            }
        }).then(response => {
            var resultWebItem = response.data;
            var result = resultWebItem.result;
            this.$message(result);
            this.fetchData();
            console.log('返回结果：'+result)

            // 清空原始上传的数据
            location.reload();

            // 处理响应数据
        }).catch(error => {
            console.error(error);
        });
        },

    deletedataset(dataset_id){
      console.log('dataset_id:', dataset_id);
      var dataset_idstring = String(dataset_id);
      const formData = new FormData();
      formData.append('user_id', this.user_id);
      formData.append('dataset_id', dataset_idstring);
      
      this.$message('删除请求已提交!')

      axios.post("http://192.168.43.254:8080/manage/delete_dataset",formData,{
        headers: {
            'Content-Type': 'multipart/form-data', // 设置请求头
        }
      }).then(response=>{
        var resultWebItem = response.data;
        var result = resultWebItem.result;
        this.$message(result);
        this.fetchData();
        console.log('返回结果：'+result)
        // console.log('请求表单数据成功'+response)
        
      })
      .catch(error=>{
        console.error(error);
      });
      
    },


    onCancel(){
      location.reload();
    },


    fileChang(file, fileList, name) {
        // 更新 instFilePics
        this.form.instFilePics = fileList;

        // 处理上传的文件夹中的图片
        const images = [];
        const allowedTypes = ['image/jpeg', 'image/png']; // 可接受的图片类型

        for (const file of fileList) {
            if (file.raw && allowedTypes.includes(file.raw.type)) {
            images.push({ image: file.raw, label: null });
            } else {
            // 处理非法文件类型，比如给用户提示
            console.warn(`Invalid file type: ${file.raw.type}`);
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

    deleteImage() {
      if (this.form.instFilePics[this.currentImageIndex] !== null) {
        this.form.instFilePics.splice(this.currentImageIndex, 1);

        this.$nextTick(() => {
          if (this.currentImageIndex > 0) {
              this.currentImageIndex--;
              const existingLabel = this.uploadingImages[this.currentImageIndex].label;
              if (existingLabel !== null) {
                this.selectedNumber = existingLabel;
              }
              else{
                this.selectedNumber = null;
              }
            this.labeling = true;
          } else if (this.uploadingImages.length > 0) {
            this.labeling = true;
          } else {
            this.labeling = false;
          }

          // console.log('Deleted Image Index:', this.currentImageIndex);
        });
      } else {
        this.labeling = false;
      }
    },
   

      getImageUrl() {
      const currentImage = this.uploadingImages[this.currentImageIndex];
      if (currentImage && currentImage.image instanceof Blob) {
        // 如果 image 是 Blob 对象，使用 URL.createObjectURL 创建临时 URL
        return URL.createObjectURL(currentImage.image);
      } else {
        // 如果 image 不是 Blob 对象，可能需要根据实际情况返回其他 URL 或空字符串
        return '';
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


</style>
