<template>
  <div class="app-container">
    <div class="h1" :style="styleOfH1">
      <h1>
        系统使用数据统计
      </h1>
    </div>
    <br>
    <div class="choose" :style="styleOfChoose">
      系统使用模型
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
    <br><br>
    <div style="margin-left: 10%;">
      <el-radio-group v-model="radio">
        <el-radio :label="1">年度</el-radio>
        <el-radio :label="2">月度</el-radio>
        <el-radio :label="3">周次</el-radio>
        <el-radio :label="4">每日</el-radio>
      </el-radio-group>
      <span v-html="'\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0'"></span>
      <el-select v-model="valueMon" placeholder="请选择">
        <el-option
          v-for="item in optionsMon"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <br>
      <el-image>
        <div slot="error" class="image-slot">
          <i class="el-icon-picture-outline">图片缺失</i>
        </div>
      </el-image>
      <span v-html="'\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0'"></span>
      <el-image>
        <div slot="error" class="image-slot">
          <i class="el-icon-picture-outline">图片缺失</i>
        </div>
      </el-image>
    </div>
    <br><br><br><br><br><br><br><br><br><br><br><br><br>
    <div>
      <el-table
        :data="tableData"
        border
        style="width: 100%">
        <el-table-column
          fixed
          prop="cata"
          label=""
          width="180">
        </el-table-column>
        <el-table-column
          prop="jau"
          label="1月"
          width="180">
        </el-table-column>
        <el-table-column
          prop="feb"
          label="2月"
          width="180">
        </el-table-column>
        <el-table-column
          prop="mar"
          label="3月"
          width="180">
        </el-table-column>
        <el-table-column
          prop="apr"
          label="4月"
          width="180">
        </el-table-column>
        <el-table-column
          prop="may"
          label="5月"
          width="180">
        </el-table-column>
        <el-table-column
          prop="jun"
          label="6月"
          width="180">
        </el-table-column>
        <el-table-column
          prop="jul"
          label="7月"
          width="180">
        </el-table-column>
        <el-table-column
          prop="aug"
          label="8月"
          width="180">
        </el-table-column>
        <el-table-column
          prop="sep"
          label="9月"
          width="180">
        </el-table-column>
        <el-table-column
          prop="oct"
          label="10月"
          width="180">
        </el-table-column>
        <el-table-column
          prop="nov"
          label="11月"
          width="180">
        </el-table-column>
        <el-table-column
          prop="dec"
          label="12月"
          width="180">
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
export default {
  data(){
    return{
      styleOfH1:{
        color:'blue',
        marginLeft:'2%'
      },
      styleOfChoose:{
        marginLeft:'2%'
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
        }],
        radio: 2,
        optionsMon: [{
          value: '选项1',
          label: '1月'
        }, {
          value: '选项2',
          label: '2月'
        }, {
          value: '选项3',
          label: '3月'
        }, {
          value: '选项4',
          label: '4月'
        }, {
          value: '选项5',
          label: '5月'
        },{
          value: '选项6',
          label: '6月'
        },{
          value: '选项7',
          label: '7月'
        },{
          value: '选项8',
          label: '8月'
        },{
          value: '选项9',
          label: '9月'
        },{
          value: '选项10',
          label: '10月'
        },{
          value: '选项11',
          label: '11月'
        },{
          value: '选项12',
          label: '12月'
        }],
        valueMon: '',
        tableData: [{
          cata: '用户使用次数',
          jau:'',
          feb:'',
          mar:'',
          apr:'',
          may:'',
          jun:'',
          jul:'',
          aug:'',
          sep:'',
          oct:'',
          nov:'',
          dec:''
        }, {
          cata: '反馈错误次数',
          jau:'',
          feb:'',
          mar:'',
          apr:'',
          may:'',
          jun:'',
          jul:'',
          aug:'',
          sep:'',
          oct:'',
          nov:'',
          dec:''
        }, {
          cata: '错误率',
          jau:'',
          feb:'',
          mar:'',
          apr:'',
          may:'',
          jun:'',
          jul:'',
          aug:'',
          sep:'',
          oct:'',
          nov:'',
          dec:''
        }]
    }
  }
}
</script>

<style>

</style>