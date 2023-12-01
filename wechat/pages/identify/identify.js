// pages/identify/identify.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    image:'',
    text:'',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  /*onLoad(options) {

  },*/
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    this.setData({
      image:options.image,
      text:options.text
    })
       var users=[];
       var that = this;
       var arrayBuffer=wx.base64ToArrayBuffer(that.data.image);//将Base64对象转成ArrayBuffer对象
       var base64=wx.arrayBufferToBase64(arrayBuffer);
       //console.log(base64);
       that.setData({ identifyImage: 'data:image/jpg;base64,' + base64});
        //拼接成data URL
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  },
  feedback(){
    wx.showModal({
      title: '',
      content: '该识别结果是否正确？',
      cancelText:'错误',
      confirmText:'正确',
      complete: (res) => {
        if (res.cancel) {
          console.log('wrong')
          wx.request({
            url: 'http://192.168.43.254:8080/use/feedback',
            method:'post',
            data: {
              feedback: 'wrong'
            },
            header: {
              'content-type': 'application/x-www-form-urlencoded' // 默认值
            },
            success (res) {
              console.log(res.data)
            }
          })

        }
    
        if (res.confirm) {
          console.log('right')
          wx.request({
            url: 'http://192.168.43.254:8080/use/feedback',
            method:'post',
            data: {
              feedback: 'right'
            },
            header: {
              'content-type': 'application/x-www-form-urlencoded' // 默认值
            },
            success (res) {
              console.log(res.data)
            }
          })
          
        }
      }
    })

  },
  next_calculator(){
    // wx.navigateTo({
    //   url: '/pages/index/index',
    // })
    wx.navigateBack()
  }
})