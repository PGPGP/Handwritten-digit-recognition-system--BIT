<template>
  <div>
    <div calss="h1" :style="styleOfH1">
      <h1>
        应用模型
      </h1>
    </div>
    <div class="info" :style="styleOfInfo">
      <div class="model" :style="styleOfModel">
        系统当前使用模型为：
        <el-input v-model="input_model" placeholder="暂无使用模型" style="width:20%" :disabled="true"></el-input>
      </div>
      <div class="time" :style="styleOfTime">
        <br>
        启用时间：
        <el-input v-model="input_time" placeholder="暂无使用记录" style="width:20%" :disabled="true"></el-input>
      </div>
      <div class="span" :style="styleOfSpan">
        <br>
        使用时间：
        <el-input v-model="input_span" placeholder="暂无使用记录" style="width:20%" :disabled="true"></el-input>
        天
      </div>
    </div>
    <br><br><br><br><br>
    <div class="choose" :style="styleOfChoose">
      可供选择的模型：
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
      <el-button type="primary" icon="fa fa-upload">应用</el-button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return{
      styleOfH1:{
        color:'blue',
        marginLeft:'2%'
      },
      styleOfInfo:{
        border:'1px solid blue',
        marginLeft:'7%',
        marginRight:'7%'
      },
      styleOfModel:{
        marginTop:'0.5%',
        marginLeft:'1%'
      },
      input_model:'',
      styleOfTime:{
        marginLeft:'1%'
      },
      input_time:'',
      styleOfSpan:{
        marginLeft:'1%',
        marginBottom:'0.5%'
      },
      input_span:'',
      styleOfChoose:{
        marginLeft:'7%'
      },
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
        }]
    }
  }

}
</script>

<style>

</style>