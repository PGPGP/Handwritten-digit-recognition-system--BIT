
<script>

const user = JSON.parse(sessionStorage.getItem('userInfo'))
// 拆分数据为name和uid
const user_id = user.user_id
const user_name = user.user_name
const user_verify = user.user_verify
// 导出拆分后的数据
export default { user_id, user_name, user_verify }

// 导入时使用语句 import users from '@/store/users/users.vue'--其中users为自定义的变量名
// 在data（）值的return中加入以下语句
// user_id: users.user_id,
// user_name: users.user_name,

</script>
