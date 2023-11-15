<template>
  <div class="navbar">
    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb class="breadcrumb-container" />

    <div class="right-menu">
      <el-dropdown class="avatar-container" trigger="click">
        <!-- <div class="avatar-wrapper">
          <img :src="avatar+'?imageView2/1/w/80/h/80'" class="user-avatar">
          <img :style="{ backgroundColor: extractColorByName(user.user_name) }" class="user-avatar" :src="user.user_name+'?imageView2/1/w/80/h/80'">
          <i class="el-icon-caret-bottom" />
        </div> -->
        <div class="avatar-wrapper">
          <div :style="{ backgroundColor: extractColorByName(user.user_name) }" class="user-avatar">
            <!-- {{ getInitials(user.user_name) }} -->
            <span class="avatar-text">{{ getInitials(user.user_name) }}</span>
            <i class="el-icon-caret-bottom" />
          </div>
        </div>
        <el-dropdown-menu slot="dropdown" class="user-dropdown">
          <router-link to="/">
            <el-dropdown-item>
              首页
            </el-dropdown-item>
          </router-link>
          <a target="_blank">
            <el-dropdown-item>个人信息</el-dropdown-item>
          </a>
          <a target="_blank" href="https://github.com/PGPGP/Handwritten-digit-recognition-system--BIT">
            <el-dropdown-item>帮助</el-dropdown-item>
          </a>
          <el-dropdown-item divided @click.native="logout">
            <span style="display:block;">退出</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
// import user from 'mock/user'

export default {
  components: {
    Breadcrumb,
    Hamburger
  },
  data() {
    return {
      user: {
        user_name: ''
      }
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar'
    ])
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    // async logout() {
    //   await this.$store.dispatch('user/logout')
    //   this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    // }
    async logout() {
      // 移除本地用户登录信息
      sessionStorage.removeItem('userInfo')
      // 跳转页面到登录页
      this.$router.push('/login')
    },
    // 传入名字,根据名字生成颜色,这样颜色就固定下来了
    extractColorByName(user_name) {
      var temp = []
      temp.push('#')
      for (let index = 0; index < user_name.length; index++) {
        temp.push(parseInt(user_name[index].charCodeAt(0), 10).toString(16))
      }
      return temp.slice(0, 5).join('').slice(0, 4)
    },
    //  用户名首字母
    getInitials(username) {
      return username.charAt(0).toUpperCase()
    }
  },
  mounted() {
    if (sessionStorage.getItem('userInfo')) {
      // 将用户信息存储到sessionStorage中
      this.user = JSON.parse(sessionStorage.getItem('userInfo'))
    }
  }

}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;
        margin-bottom: 5px;

        .user-avatar {
          cursor: pointer;
          width: 50px;
          height: 50px;
          border-radius: 10px;
          display: flex;
          justify-content: center;
          align-items: center;
          position: relative;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
        .avatar-text {
          color: #ece6e6; /* 设置文字颜色 */
          font-size: 20px; /* 设置文字大小 */
          font-weight: bold; /* 设置文字粗细 */
          }
      }
    }
  }
}
</style>
