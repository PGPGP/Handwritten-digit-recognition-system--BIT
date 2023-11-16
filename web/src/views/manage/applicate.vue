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
        <el-input ref="input_model" v-model="input_model" placeholder="暂无使用模型" style="width:20%" :disabled="true">111</el-input>
      </div>
      <div class="time" :style="styleOfTime">
        <br>
        启用时间：
        <el-input ref="input_time" v-model="input_time" placeholder="暂无使用记录" style="width:30%" :disabled="true"></el-input>
      </div>
      <div class="span" :style="styleOfSpan">
        <br>
        使用时间：
        <el-input ref="input_span" v-model="input_span" placeholder="暂无使用记录" style="width:20%" :disabled="true"></el-input>
        天
      </div>
    </div>
    <br><br><br><br><br>
    <div class="choose" :style="styleOfChoose">
      可供选择的模型：
      <el-select v-model="value" placeholder="请选择">
        <el-option
          v-for="item in options"
          :key="item.model_id"
          :label="item.model_name"
          :value="item.model_id">
        </el-option>
      </el-select>
      <el-button type="primary" icon="fa fa-upload" @click="changeStatus">应用</el-button>
    </div>
    <div class="table_button" style="margin-left: 40%">
      <el-button @click="resetDateFilter">清除日期过滤器</el-button>
      <el-button @click="clearFilter">清除所有过滤器</el-button>
    </div>
    <div class="table" :style="styleOfTable">
      <el-table
        ref="filterTable"
        :data="tableData"
        style="width: 100%"
        max-height="250">
        <el-table-column
          prop="model_name"
          label="模型名称"
          width="180"
          fixed>
        </el-table-column>
        <el-table-column
          prop="dataset_id"
          label="数据集"
          width="180"
          fixed>
        </el-table-column>
        <el-table-column
          prop="user_id"
          label="创建人"
          width="180"
          fixed>
        </el-table-column>
        <el-table-column
          prop="model_create_date"
          label="创建日期"
          sortable
          width="180"
          column-key="date"
          fixed
        >
        </el-table-column>
        <el-table-column
          prop="is_active"
          label="状态"
          width="100"
          :filters="[{ text: '使用中', value: 1 }, { text: '禁用', value: 0 }]"
          :filter-method="filterTag"
          filter-placement="bottom-end"
          fixed>
          <template slot-scope="scope">
            <el-tag v-if="scope.row.is_active === 1"
              :type="'success'"
              disable-transitions>使用中</el-tag>
            <el-tag v-else
            :type="'danger'"
            disable-transitions>禁用</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
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
          model_id: 'Minist',
          model_name: '默认模型'
        }],
        value: '',
        tableData: [{
          model: '默认模型',
          dataset: 'Minist',
          date: '2016-05-02',
          name: '测试文本',
          tag: '使用中'
        }, {
          model: '测试文本',
          dataset: '测试文本',
          date: '2016-05-04',
          name: '测试文本',
          tag: '禁用'
        }, {
          model: '测试文本',
          dataset: '测试文本',
          date: '2016-05-01',
          name: '测试文本',
          tag: '禁用'
        }, {
          model: '测试文本',
          dataset: '测试文本',
          date: '2016-05-03',
          name: '测试文本',
          tag: '禁用'
        }],
        styleOfTable:{
          marginLeft:'20%'
        },
        input_model: '',
        input_time: '',
        input_span: ''
    }
  },
  methods: {
      resetDateFilter() {
        this.$refs.filterTable.clearFilter('date');
      },
      clearFilter() {
        this.$refs.filterTable.clearFilter();
      },
      formatter(row, column) {
        return row.address;
      },
      filterTag(value, row) {
        return row.is_active === value;
      },
      changeStatus() {
        // for(const i in this.options){
        //   if(this.options[i].model_name == this.value){
        //     id = this.
        //   }
        // }
        const formData = new FormData()
        formData.append('model_id', this.value)
        console.log(this.value)
        axios.post("http://localhost:8080/manage/switch_model",formData,{headers:{'Content-Type':'multipart/form-data'}}).then((response) => {
          if(response.data.result == "successful"){
            for(const i in this.tableData){
              this.tableData[i].is_active = 0
            }
            for(const i in this.tableData){
              if(this.tableData[i].model_id == this.value){
                this.tableData[i].is_active = 1
                this.input_model = this.tableData[i].model_name
                this.input_time = this.tableData[i].model_activate_date
                break
              }
            }
          }
          else{
            alert('更换失败')
          }
        })
      },
      fresh(){
        for(const i in this.tableData){
          if(this.tableData[i].is_active == 1){
            console.log('this.tableData[i]')//尽管成功匹配了，值并没有得到修改，需要等待讨论
            this.input_model = this.tableData[i].model_name
            this.input_time = this.tableData[i].model_activate_date
            let year = new Date().getFullYear(); //获取当前时间的年份
            let month = new Date().getMonth() + 1; //获取当前时间的月份
            let day = new Date().getDate(); //获取当前时间的天数
            let date = this.input_time.substr(0,10)
            let nowDate = year + '-' + month + '-' + day
            console.log(nowDate)
            console.log(date)
            let dateSpan, iDays
	          let sDate1 = Date.parse(date)
	          let sDate2 = Date.parse(nowDate)
            dateSpan = sDate2 - sDate1
	          dateSpan = Math.abs(dateSpan)
	          iDays = Math.floor(dateSpan / (24 * 3600 * 1000))
            console.log(iDays)
            this.input_span = iDays
            break
          }
        }
      }
    },
    created:function(){
      console.log('应用被创建')
      axios.post("http://localhost:8080/request/model").then((response) => {
        console.log('请求表单数据成功'+response)
        //this.options = response.options
        if(response != null){
          this.options = response.data.models
          this.tableData = response.data.models
          console.log(response)
        }
        
        this.fresh()
      })
    },
    mounted:function(){
      console.log('应用被挂载')
    }
}
</script>

<style>

</style>