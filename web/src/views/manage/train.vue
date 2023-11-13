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
        <el-option
          v-for="item in options"
          :key="item.dataset_id"
          :label="item.dataset_name"
          :value="item.dataset_name">
        </el-option>
      </el-select>
    </div>
    <div class="train" :style="stylrOfTrain">
      <span><br><br>(训练集:测试集)%</span>
    </div>
    <div class="ratio" style="margin-left: 5%;margin-right: 5%;">
      <el-slider
        v-model="ratioValue"
        show-input>
      </el-slider>
    </div>
    <br>
    <br><br><br>
    <div class="h2" :style="styleOfH2">
      <h2>超参数</h2>
    </div>
    <div class="para" :style="styleOfPara">
      <span>epoch
        <el-input v-model="input_epoch" placeholder="请输入内容" style="width: 20%"></el-input>
      </span>
      <span><br>batch_size
        <el-input v-model="input_batch" placeholder="请输入内容" style="width: 20%"></el-input>
      </span>
      <span><br>learning_rate
        <el-input v-model="input_lr" placeholder="请输入内容" style="width: 20%"></el-input>
      </span>
    </div>
    <div class="start" :style="styleOfStart">
      <el-button v-if="trainFlag === false" type="primary" @click="train_start">开始训练<i class="el-icon-upload el-icon--right"></i></el-button>
      <el-button v-else type="primary" :loading="true">训练中</el-button>
    </div>
    <div class="h2" :style="styleOfH2">
      <h2><br>训练结果</h2>
    </div>
    <div class="block" style="margin-left: 40%">
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
import axios from 'axios'
import { socket } from './websocket'
export default {
  data() {
      return { 
        options: [{
          value: 'Minist',
          label: '默认数据集'
        }, {
          value: 'Testtext1',
          label: '测试文本1'
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
          marginLeft: '1%'
        },
        styleOfH2:{
          marginLeft: '1%'
        },
        styleOfChoose:{
          marginLeft: '5%'
        },
        stylrOfTrain:{
          marginLeft: '5%'
        },
        styleOfPara:{
          marginLeft: '5%'
        },
        styleOfStart:{
          marginLeft: '40%'
        },
        loading: true,
        websocketCount: -1,
        //查询条件
        queryCondition: {
          type: "message",
        },
        url: 'ws://localhost:8080/manage/train',
        para:{
          user_id: 1, //请求的用户id
          type: 'start', //websocket请求的类型。在这个用例中，为"start"
          tItem: {
            model_name: '111', //待训练的模型名称
            user_id: 2, //创建模型的用户id
            dataset_id: 3, //数据集id
            ratio: '', //训练集：测试集的比例
            epoch: '', //训练超参数
            batch_size: '', //训练超参数
            learning_rate: '' //训练超参数
          }
        },
      }
    },
    methods:{
      train_start: function(){
        this.para.tItem.ratio = this.ratioValue
        this.para.tItem.epoch = this.input_epoch
        this.para.tItem.batch_size = this.input_batch
        this.para.tItem.learning_rate = this.input_lr
        socket.sendMsg(JSON.stringify(this.para))
        this.trainFlag = !this.trainFlag
      },
      train_over: function(){
        
        this.trainFlag = !this.trainFlag
      },
      init() {
        socket.sendMsg(JSON.stringify(this.para))
      },
      websocketOnMessage(event) {
        //初始化界面时，主动向后台发送一次消息，获取数据
        //this.websocketCount += 1;
        // if (this.websocketCount === 0) {
        //   this.init();
        // }
        let info = JSON.parse(event.data)
        switch (info.type) {//有待修改
          case "heartbeat":
            socket.websocketState = true
            break
          case "message":
            this.loading = true;
            this.$nextTick(() => {
              this.consumeMessage(info)
            })
            break;
          case "error":
            this.loading = false;
            break
          default: 
            alert('666')
            console.log('未完成处理的消息')
            break
        }
      },
      consumeMessage(info) {
        //拿到最新数据重新渲染界面
      }
    },
    created:function(){
      console.log('训练被创建')
      axios.post("http://localhost:8080/request/dataset").then((response) => {
        console.log('请求表单数据成功'+response)
        console.log(response.data)
        this.options = response.data.datasets
      })
      //初始化websocket对象
      //window.location.host获取ip和端口，
      //process.env.VUE_APP_WEBSOCKET_BASE_API获取请求前缀
      socket.initWebSocket(
        this.url
      );// `ws:${window.location.host}${process.env.VUE_APP_WEBSOCKET_BASE_API}/notice/` +
        //   userId
      //绑定接收消息方法
      socket.websocket.onmessage = this.websocketOnMessage
    },
    mounted:function(){
      console.log('训练被挂载')
    }
}
</script>

<style>

</style>