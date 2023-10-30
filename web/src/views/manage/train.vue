<template>
  <div>
    <div class="h1" :style="styleOfH1">
      <h1>训练模型</h1>
    </div>
    <div class="h2" :style="styleOfH2">
      <h2>数据集</h2>
    </div>
    <div class="choose" :style="styleOfChoose">
    <span>可选数据集</span>
      <el-select v-model="value" placeholder="请选择">
        <el-option-group
          v-for="group in options"
          :key="group.label"
          :label="group.label">
          <el-option
            v-for="item in group.options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-option-group>
      </el-select>
    </div>
    <div class="train" :style="stylrOfTrain">
      <span><br><br>训练集：测试集</span>
    </div>
    <el-slider
      v-model="ratioValue"
      show-input>
    </el-slider>
    <br>
    <br><br><br>
    <div class="h2" :style="styleOfH2">
      <h2>超参数</h2>
    </div>
    <span>epoch
    <el-input v-model="input_epoch" placeholder="请输入内容"></el-input></span>
    <span><br>batch_size
    <el-input v-model="input_batch" placeholder="请输入内容"></el-input></span>
    <span><br>learning_rate
    <el-input v-model="input_lr" placeholder="请输入内容"></el-input></span>
    <el-button v-if="trainFlag === false" type="primary" @click="train_start">开始训练<i class="el-icon-upload el-icon--right"></i></el-button>
    <el-button v-else type="primary" :loading="true">训练中</el-button>
    <span><br>训练结果</span>
    <div class="block">
      <span class="demonstration">图1</span>
      <el-image :src="src1">
        <div slot="placeholder" class="image-slot">
          素材缺失<span class="dot">...</span>
        </div>
      </el-image>
      <span class="demonstration">图2</span>
      <el-image :src="src2">
        <div slot="placeholder" class="image-slot">
          加载中<span class="dot">...</span>
        </div>
      </el-image>
    </div>
  </div>
</template>

<script>
export default {
  data() {
      return {
        options: [{
          label: '常用数据集',
          options: [{
            value: 'Minist',
            label: '默认数据集'
          }, {
            value: 'Testtext1',
            label: '测试文本1'
          }]
        }, {
          label: '数据集',
          options: [{
            value: 'Testtext2',
            label: '测试文本2'
          }, {
            value: 'Testtext3',
            label: '测试文本3'
          }, {
            value: 'Testtext4',
            label: '测试文本4'
          }, {
            value: 'Testtext5',
            label: '测试文本5'
          }]
        }],
        value: '',
        ratioValue: 0,
        input_epoch: '',
        input_batch: '',
        input_lr: '',
        trainFlag: false,
        src1:'',
        src2:'',
        styleOfH1: {
          color: 'blue',
          fontSize: '13px',
          marginLeft: '10px'
        },
        styleOfH2:{
          marginLeft: '10px'
        },
        styleOfChoose:{
          marginLeft: '50px'
        },
        stylrOfTrain:{
          marginLeft: '50px'
        }
      }
    },
    methods:{
      train_start: function(){
        
        this.trainFlag = !this.trainFlag
      },
      train_over: function(){
        
        this.trainFlag = !this.trainFlag
      }
    }
}
</script>

<style>

</style>