import router from './router'
// import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
// import { getToken } from '@/utils/auth' // get token from cookie
// import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

// const whiteList = ['/login'] // no redirect whitelist

// router.beforeEach(async(to, from, next) => {
//   // start progress bar
//   NProgress.start()

//   // set page title
//   document.title = getPageTitle(to.meta.title)

//   // determine whether the user has logged in
//   const hasToken = getToken()
//   // const hasToken = !!sessionStorage.getItem('userInfo')
//   if (hasToken) {
//     if (to.path === '/login') {
//       // if is logged in, redirect to the home page
//       next({ path: '/' })
//       // next()
//       NProgress.done()
//     } else {
//       const hasGetUserInfo = store.getters.name
//       if (hasGetUserInfo) {
//         next()
//       } else {
//         try {
//           // get user info
//           await store.dispatch('user/getInfo')

//           next()
//         } catch (error) {
//           // remove token and go to login page to re-login
//           await store.dispatch('user/resetToken')
//           Message.error(error || 'Has Error')
//           next(`/login?redirect=${to.path}`)
//           NProgress.done()
//         }
//       }
//     }
//   } else {
//     /* has no token*/
//     if (to.path === '/register') {
//       next()
//     } else {
//       if (whiteList.indexOf(to.path) !== -1) {
//         // in the free login whitelist, go directly
//         next()
//       } else {
//         // other pages that do not have permission to access are redirected to the login page.
//         next(`/login?redirect=${to.path}`)
//         NProgress.done()
//       }
//     }
//   }
// })

router.beforeEach((to, from, next) => {
  const isAuthenticated = !!sessionStorage.getItem('userInfo')
  // 如果路由要跳转到除了登录和注册的界面的话就判断是否已经登录，如果没有登录就强制跳到登录界面
  if (to.path !== '/login' && to.path !== '/register' && !isAuthenticated) {
    next({ path: '/login' })
    Message({
      message: '请先登录！',
      type: 'warning'
    })
  } else next()
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
