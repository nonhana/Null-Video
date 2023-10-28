import axios, { AxiosRequestConfig } from 'axios'
import router from '../router/index'
// import Vue from 'vue';
import { ElMessage } from 'element-plus'
import {
  startLoading,
  endLoading
} from '../components/publicComponents/loading.vue'
import i18n from '../i18n'
const { locale } = i18n.global

function myAxios<T = any>(axiosConfig: AxiosRequestConfig<any>) {
  const accessToken = window.localStorage.token

  const service = axios.create({
    baseURL: 'https://example.com/graphql', // 替换为 GraphQL API 的端点 URL
    timeout: 5000, //最多等待响应5秒
    headers: {
      Authorization: 'Bearer' + accessToken,
      'Accept-Language': locale.value // 这行代码设置了全局字段，表示当前用户选择的语言
    }
  })

  // 请求拦截器：可以在发请求之前可以处理一些业务
  service.interceptors.request.use((config) => {
    startLoading()
    // console.log(locale.value)
    return config
  })

  // 响应拦截器：当服务器数据返回以后，可以处理一些事情
  service.interceptors.response.use(
    (res) => {
      endLoading()
      //GraphQL API处理相关数据
      // const responseData = res.data; // 获取响应数据对象

      // if (responseData.errors) {
      //   // 处理错误信息
      //   const errorMessages = responseData.errors.map(error => error.message).join(', ');
      //   ElMessage(errorMessages);
      //   return Promise.reject(responseData.errors);
      // }

      // const { data } = responseData; // 获取 GraphQL 查询的结果数据
      // return data;
      return res
    },
    (err) => {
      endLoading()
      switch (err.response.result_status) {
        case 302:
          ElMessage('接口重定向了！')
          break
        case 400:
          ElMessage('参数不正确！')
          break
        case 401:
          // store.dispatch("outlogin")
          ElMessage('登录过期,请重新登录')
          router.push('/login')
          // window.localStorage.removeItem("token");
          break

        case 403:
          ElMessage('您没有权限操作！')
          break
        case 404:
          ElMessage(`请求地址出错: ${err.response.config.url}`)
          break // 在正确域名下
        case 408:
          ElMessage('请求超时！')
          break
        case 409:
          ElMessage('系统已存在相同数据！')
          break
        case 500:
          ElMessage('服务器内部错误！')
          break
        case 501:
          ElMessage('服务未实现！')
          break
        case 502:
          ElMessage('网关错误！')
          break
        case 503:
          ElMessage('服务不可用！')
          break
        case 504:
          ElMessage('服务暂时无法访问，请稍后再试！')
          break
        case 505:
          ElMessage('HTTP版本不受支持！')
          break
        default:
          ElMessage('异常问题，请联系管理员！')
          console.log()
          break
      }
      return Promise.reject(err)
    }
  )
  return service(axiosConfig)
}
export default myAxios
