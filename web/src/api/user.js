import request from '@/utils/request'

export function login(token) {
  return request({
    url: '/user/login',
    method: 'post',
    params: { token }
    // data
  })
}

export function register(token) {
  return request({
    url: '/user/register',
    method: 'post',
    params: { token }
  })
}

export function getInfo(token) {
  return request({
    url: '/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}
