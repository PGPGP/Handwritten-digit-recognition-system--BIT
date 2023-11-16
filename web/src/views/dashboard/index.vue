<template>
  <div class="dashboard-container">
    <div class="dashboard-text">name: {{ user_name }}</div>

    <div :style="styleOfWel">欢迎使用手写数字识别管理系统</div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import users from '@/store/users/users.vue'

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  data() {
    return {
      user_id: users.user_id,
      user_name: users.user_name,
      styleOfWel: {
        color: 'silver',
        fontSize: '50px',
        textAlign: 'center',
        marginTop: '10%',
        opacity: '0.5'
      }
    }
  }
  // mounted() {
  //   if (sessionStorage.getItem('userInfo')) {
  //     // 将用户信息存储到sessionStorage中
  //     this.user = JSON.parse(sessionStorage.getItem('userInfo'))
  //   }
  // }

}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
