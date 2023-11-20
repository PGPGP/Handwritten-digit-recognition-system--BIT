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
      <span><br><br>训练集:测试集</span>
    </div>
    <div class="ratio" style="margin-left: 5%;margin-right: 5%;">
      <el-slider
        v-model="ratioValue"
        show-input
        :step=0.1
        :max=1>
      </el-slider>
    </div>
    <br>
    <br><br><br>
    <div class="h2" :style="styleOfH2">
      <h2>超参数</h2>
    </div>
    <div class="para" :style="styleOfPara">
      <span>模型名称
        <el-input v-model="input_name" placeholder="请输入内容" style="width: 20%"></el-input>
      </span>
      <br>
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
    <!-- <div class="h2" :style="styleOfH2">
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
    </div> -->
    <el-dialog
      title="训练情况"
      :visible.sync="centerDialogVisible"
      width="50%"
      :close-on-click-modal='false'
      :close-on-press-escape='false'
      :show-close="false"
      center>
      <div id="main" style="width: 100%; height: 500px; background:#fff"></div>
      <div id="main2" style="width: 100%; height: 500px; background:#fff"></div>
      <span>训练过程中不能操作其他界面，除非您希望强制中止训练</span>
      <span slot="footer" class="dialog-footer">
        <el-button v-if="diagFlag == false" type="primary" @click="train_stop">中止训练</el-button>
        <el-button v-else type="primary" @click="train_over">完成</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'
import { socket } from './websocket'
import * as echarts from 'echarts'
import users from '@/store/users/users.vue'
import { options } from 'runjs'
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
        ratioValue: 0.8,
        input_epoch: 10,
        input_batch: 32,
        input_lr: 0.001,
        input_name: '',
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
        url: 'ws://192.168.43.254:8080/manage/train',
        para:{
          user_id: 1, //请求的用户id
          type: 'start', //websocket请求的类型。在这个用例中，为"start"
          object: {
            model_name: '111', //待训练的模型名称
            user_id: 2, //创建模型的用户id
            dataset_id: 3, //数据集id
            ratio: '', //训练集：测试集的比例
            epoch: '', //训练超参数
            batch_size: '', //训练超参数
            learning_rate: '' //训练超参数
          }
        },
        stop:{
          user_id: 1,
          type: 'stop',
          object: null
        },
        charts: '',
        charts2: '',
        trainLossData: [],
        trainAccData: [],
        testLossData: [],
        testAccData: [],
        xData: [],
        user: {
          user_name: '',
          user_id: null
        },
        centerDialogVisible: false,
        diagFlag: false,
        drawnum: 1
      }
    },
    methods:{
      train_start: function(){
        if(this.value == ''){
          alert('数据集不能为空')
          return
        }
        if(this.input_name == ''){
          alert('模型名称不能为空')
          return
        }
        for(let i=1; i <= this.input_epoch; i++){
          this.xData.push(i)
        }
        // console.log('开始尝试重连')
        // socket.reconnect()
        // console.log('尝试重连结束')
        this.para.user_id = this.user.user_id
        for(const i in this.options){
          if(this.options[i].dataset_name == this.value){
            console.log('检测'+this.options[i])
            this.para.object.dataset_id = this.options[i].dataset_id
            this.para.object.model_name = this.input_name
            this.para.object.user_id = this.options[i].user_id
            break
          }
        }
        this.para.object.ratio = this.ratioValue
        this.para.object.epoch = this.input_epoch
        this.para.object.batch_size = this.input_batch
        this.para.object.learning_rate = this.input_lr
        socket.sendMsg(JSON.stringify(this.para))
        this.trainFlag = !this.trainFlag
        //const h = this.$createElement;
        this.centerDialogVisible = true
        // this.$msgbox({
        //   showClose:false,
        //   closeOnClickModal:false,
        //   closeOnPressEscape:false,
        //   showCancelButton:false,
        //   title: '训练中',
        //   message: h('p', null, [
        //     h('span', null, '在训练结束前您不能对系统做任何其他的操作，除非您希望执行 '),
        //     h('i', { style: 'color: teal' }, '强制中止'),
        //     h('span', {
        //       style:{

        //       }
        //     },
        //     [h('el-progress', {
        //         style: {
        //           cursor: 'pointer',
        //         },
        //         attrs: {
        //           // 添加属性
        //           text_inside: "true",
        //           stroke_width: "22",
        //           percentage: 80,
        //           id: "pro",
        //           status:"success",
        //         },
        //         on: {
        //           change: () => {
        //             _this.statu = '1'
        //           }
        //         }
        //       }, []), h('span', {
        //         class: 'el-progress',
        //       }, )]),
        //       h('div',{style: 'width: 100%; height: 300px; background:#fff', id: 'main'},),
        //   ]),
          
        //   confirmButtonText: '强制中止',
        //   cancelButtonText: '显示训练详情',
        //   beforeClose: (action, instance, done) => {
        //     if (action === 'confirm') {
        //       instance.confirmButtonLoading = true;
        //       instance.confirmButtonText = '执行中止操作中...'
        //       socket.sendMsg(JSON.stringify(this.stop))
        //       setTimeout(() => {
        //         done();
        //         setTimeout(() => {
        //           instance.confirmButtonLoading = false;
        //         }, 300);
        //       }, 3000);
        //     } else {
        //         this.drawLine('main')
        //       //done();
        //     }
        //   }
        // }).then(action => {
        //   this.$message({
        //     type: 'info',
        //     message: '训练中断'//'action: ' + action
        //   })
        //   this.trainFlag = !this.trainFlag
        // })
      },
      train_over: function(){
        this.diagFlag = false
        this.trainFlag = !this.trainFlag
        this.centerDialogVisible = false
        this.charts.dispose()
        this.charts2.dispose()
      },
      train_stop(){
        this.stop.user_id = this.user.user_id
        socket.sendMsg(JSON.stringify(this.stop))
        this.centerDialogVisible = false
        this.diagFlag = false
        this.trainFlag = !this.trainFlag
        this.xData = []
        this.trainLossData = []
        this.trainAccData = []
        this.testLossData = []
        this.testAccData = []
        this.charts.dispose()
        this.charts2.dispose()
        alert('训练中止')
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
            this.loading = false
            break
          case "info":
            //这里写改变chartsData的赋值
            this.trainLossData.push(info.object.train_loss)
            this.trainAccData.push(info.object.train_acc)
            this.testLossData.push(info.object.test_loss)
            this.testAccData.push(info.object.test_acc)
            // if(this.drawnum % 20 == 0){
            //   this.drawLine('main')
            //   this.drawLine2('main2')
            //   this.drawnum = 1
            // }              
            // else
            //   this.drawnum++
            this.drawLine('main')
            this.drawLine2('main2')
            break
          case "finish":
            //this.$msgbox.close()
            //this.centerDialogVisible = false
            this.drawLine('main')
            this.drawLine2('main2')
            this.xData = []
            this.trainLossData = []
            this.trainAccData = []
            this.testLossData = []
            this.testAccData = []
            alert('训练完成')
            this.diagFlag = true
            break
          default: 
            alert('666')
            console.log('未完成处理的消息')
            break
        }
      },
      consumeMessage(info) {
        //拿到最新数据重新渲染界面
      },
      drawLine2(id) {
        if(this.charts2 != '')
          this.charts2.dispose()
        this.charts2 = echarts.init(document.getElementById(id))
        this.charts2.clear()
        this.charts2.setOption({
          title:{
            left: '3%',
            top: '5%',
            text:'训练情况_acc'
          },
          tooltip:{
            trigger: 'axis'
          },
          legend: {
            align: 'right',
            left:'3%',
            top: '15%',
            data: ['train_acc', 'test_acc']
          },
          grid:{
            top:'30%',
            left:'5%',
            right:'5%',
            bottom:'5%',
            containLabel: true
          },
          toolbox: {
            feature:{
              saveAsImage: {}//保存为图片？？？这要填什么，地址吗？
            }
          },
          xAxis:{
            type: 'category',
            boundaryGap:true,
            axisTick:{
              alignWithLabel:true//刻度线和标签对齐
            },
            data:this.xData
          },
          yAxis:{
            type: 'value',
            //boundaryGap:true,
            //splitNumber:26, //有几个纵坐标
            //interval:5 //坐标间隔
          },
          series:[{
            name:'train_acc',
            type:'line',
            // stack:'?',//不知道是干什么的
            // areaStyle:{
            //   color:{
            //     type:'linear',
            //     x:0,
            //     y:0,
            //     x2:0,
            //     y2:0,
            //     colorStops:[{
            //       offset:0,color:'rgb(212,199,255)'//0%处的颜色
            //     },{
            //       offset:1,color:'#ffffff'//100%处的颜色
            //     }],
            //     global:false
            //   }
            // },
            // itemStyle:{
            //   color: 'rgb(233,199,255)',//改变折线点的颜色
            //   lineStyle:{
            //     color:'rgb(233,199,255)'//改变折线颜色
            //   }
            // },
            data: this.trainAccData
          },{
            name:'test_acc',
            type:'line',
            // stack:'?',//不知道是干什么的
            // areaStyle:{
            //   color:{
            //     type:'linear',
            //     x:0,
            //     y:0,
            //     x2:0,
            //     y2:0,
            //     colorStops:[{
            //       offset:0,color:'rgb(255,212,199)'//0%处的颜色
            //     },{
            //       offset:1,color:'#ffffff'//100%处的颜色
            //     }],
            //     global:false
            //   }
            // },
            // itemStyle:{
            //   color: 'rgb(255,199,199)',//改变折线点的颜色
            //   lineStyle:{
            //     color:'rgb(255,199,199)'//改变折线颜色
            //   }
            // },
            data: this.testAccData
          }]
        })
      },
      drawLine(id) {
        if(this.charts != '')
          this.charts.dispose()
        this.charts = echarts.init(document.getElementById(id))
        this.charts.clear()
        this.charts.setOption({
          title:{
            left: '3%',
            top: '5%',
            text:'训练情况_loss'
          },
          tooltip:{
            trigger: 'axis'
          },
          legend: {
            align: 'right',
            left:'3%',
            top: '15%',
            data: ['train_loss', 'test_loss']
          },
          grid:{
            top:'30%',
            left:'5%',
            right:'5%',
            bottom:'5%',
            containLabel: true
          },
          toolbox: {
            feature:{
              saveAsImage: {}//保存为图片？？？这要填什么，地址吗？
            }
          },
          xAxis:{
            type: 'category',
            boundaryGap:true,
            axisTick:{
              alignWithLabel:true//刻度线和标签对齐
            },
            data:this.xData
          },
          yAxis:{
            type: 'value',
            //boundaryGap:true,
            //splitNumber:26, //有几个纵坐标
            //interval:0.5 //坐标间隔
          },
          series:[{
            name:'train_loss',
            type:'line',
            // stack:'?',//不知道是干什么的
            // areaStyle:{
            //   color:{
            //     type:'linear',
            //     x:0,
            //     y:0,
            //     x2:0,
            //     y2:0,
            //     colorStops:[{
            //       offset:0,color:'rgb(255,200,213)'//0%处的颜色
            //     },{
            //       offset:1,color:'#ffffff'//100%处的颜色
            //     }],
            //     global:false
            //   }
            // },
            // itemStyle:{
            //   color: 'rgb(255,96,64)',//改变折线点的颜色
            //   lineStyle:{
            //     color:'rgb(255,96,64)'//改变折线颜色
            //   }
            // },
            data: this.trainLossData
          },{
            name:'test_loss',
            type:'line',
            // stack:'?',//不知道是干什么的
            // areaStyle:{
            //   color:{
            //     type:'linear',
            //     x:0,
            //     y:0,
            //     x2:0,
            //     y2:0,
            //     colorStops:[{
            //       offset:0,color:'rgb(199,255,220)'//0%处的颜色
            //     },{
            //       offset:1,color:'#ffffff'//100%处的颜色
            //     }],
            //     global:false
            //   }
            // },
            // itemStyle:{
            //   color: 'rgb(199,255,241)',//改变折线点的颜色
            //   lineStyle:{
            //     color:'rgb(199,255,241)'//改变折线颜色
            //   }
            // },
            data: this.testLossData
          }]
        })
      }
    },
    created:function(){
      console.log('训练被创建')
      axios.post("http://192.168.43.254:8080/request/dataset").then((response) => {
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
      this.user = users
      console.log(this.user.user_id)
      console.log(this.user.user_name)
    }
}
</script>

<style>

</style>