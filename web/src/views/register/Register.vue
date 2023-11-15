<template>
  <div class="register-container">
    <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form" auto-complete="on" label-position="left">

      <div class="title-container">
        <h3 class="title">注册</h3>
      </div>

      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="username"
          v-model="registerForm.username"
          placeholder="用户名"
          name="username"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          v-model="registerForm.password"
          :type="passwordType"
          placeholder="密码"
          name="password"
          tabindex="2"
          auto-complete="off"
          @keyup.enter.native="handleRegister"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>

      <el-form-item prop="password2">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="registerForm.password2"
          :type="passwordType"
          placeholder="确认密码"
          name="password2"
          tabindex="3"
          auto-complete="off"
          @keyup.enter.native="handleRegister"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>

      <div class="btnGroup">
        <el-button type="primary" style="margin-left:70px;width:120px;margin-bottom:30px;" @click.prevent="handleRegister">提交</el-button>
        <el-button style="margin-left:50px;width:120px;margin-bottom:30px;" @click="goBack">返回</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import { validUsername } from '@/utils/validate'

export default {
  name: 'Register',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('用户名不能为空'))
      } else {
        if (!validUsername(value)) {
          callback(new Error('请输入正确的用户名'))
        } else {
          callback()
        }
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (value.length < 6) {
          callback(new Error('密码不能少于6位'))
        } else {
          if (this.registerForm.checkPass !== '') {
          // 刚开始是validatePassword显示没有函数，改成filed就可以了
            this.$refs.registerForm.validateField('checkPass')
          }
          callback()
        }
      }
    }
    const validatePassword2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else {
        if (value !== this.registerForm.password) {
          callback(new Error('两次密码不一致'))
        } else {
          callback()
        }
      }
    }

    return {
      registerForm: {
        username: '',
        password: '',
        password2: ''
      },
      registerRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }],
        password2: [{ required: true, trigger: 'blur', validator: validatePassword2 }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined
    }
  },

  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        // 点击登陆后，让登录按钮转圈圈
        this.loading = true
        // 经过校验，账号密码都不为空，发送请求到后端登录接口
        if (valid) {
          const _this = this
          // 使用axios将登录信息发送到后端
          this.axios({
            url: 'http://localhost:8080/user/register', // 请求地址
            method: 'post', //  请求方法
            headers: { //  请求头
              'Content-Type': 'application/json'
            },
            params: {
              user_name: _this.registerForm.username,
              user_password: _this.registerForm.password
            }
          }).then((res) => { // 收到后端响应执行该括号内的代码，res为响应信号
            if (res.data.code === '20000') {
              //  将用户信息存储到sessionStorage中
              // sessionStorage.setItem('userInfo', JSON.stringify(res.data.data))
              // this.$router.push('/login')
              this.$message({
                message: res.data.msg,
                type: 'sucess'
              })
            } else { //  x响应编码不为20000，登录失败
              this.$message({
                message: res.data.msg,
                type: 'warning'
              })
            }
            _this.loading = false
            console.log(res)
          })
        } else { // 账号或密码有一个没填，不发送请求
          console.log('error submit!!')
          this.loading = false
          return false
        }
      })
    },
    goBack() {
      this.$router.push('/login')
    }
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg:#283443;
$light_gray:#fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .register-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.register-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.register-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;
  background-image:url('../../assets/background_images/background12.jpg') ;
  background-size: 100%;
  display: flex;//平铺
  align-items:center ;

  .register-form {
    position: relative;
    width: 500px;
    max-width: 100%;
    padding: 30px 30px 10px;
    margin: 0 auto;
    overflow: hidden;
    background-color: rgb(19, 39, 63);
    border-radius:10px;
    opacity: 0.9;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
