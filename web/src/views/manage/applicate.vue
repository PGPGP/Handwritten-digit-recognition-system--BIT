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
          prop="model"
          label="模型名称"
          width="180"
          fixed>
        </el-table-column>
        <el-table-column
          prop="dataset"
          label="数据集"
          width="180"
          fixed>
        </el-table-column>
        <el-table-column
          prop="name"
          label="创建人"
          width="180"
          fixed>
        </el-table-column>
        <el-table-column
          prop="date"
          label="创建日期"
          sortable
          width="180"
          column-key="date"
          :filters="[{text: '2016-05-01', value: '2016-05-01'}, {text: '2016-05-02', value: '2016-05-02'}, {text: '2016-05-03', value: '2016-05-03'}, {text: '2016-05-04', value: '2016-05-04'}]"
          :filter-method="filterHandler"
          fixed
        >
        </el-table-column>
        <el-table-column
          prop="tag"
          label="状态"
          width="100"
          :filters="[{ text: '使用中', value: '使用中' }, { text: '禁用', value: '禁用' }]"
          :filter-method="filterTag"
          filter-placement="bottom-end"
          fixed>
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.tag === '使用中' ? 'success' : 'danger'"
              disable-transitions>{{scope.row.tag}}</el-tag>
          </template>
        </el-table-column>
      </el-table>
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
        }],
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
        }
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
        return row.tag === value;
      },
      filterHandler(value, row, column) {
        const property = column['property'];
        return row[property] === value;
      }
    }
}
</script>

<style>

</style>