import axios, { AxiosRequestConfig } from 'axios'
import router from '../router/index'
import { useNotification, NotificationType } from 'naive-ui'
const notification = useNotification()

const notify = (content: string, type: NotificationType = 'warning') => {
  notification[type]({
    content: content,
    meta: 'warning',
    duration: 2500,
    keepAliveOnHover: true
  })
}

notify('warning')

function myAxios<T>(axiosConfig: AxiosRequestConfig): Promise<T> {
  const accessToken = window.localStorage.token

  const service = axios.create({
    baseURL: 'https://example.com/graphql', // 替换为 GraphQL API 的端点 URL
    timeout: 5000, //最多等待响应5秒
    headers: {
      Authorization: 'Bearer' + accessToken
    }
  })

  // 请求拦截器：可以在发请求之前可以处理一些业务
  service.interceptors.request.use((config) => {
    // console.log(locale.value)
    return config
  })

  // 响应拦截器：当服务器数据返回以后，可以处理一些事情
  service.interceptors.response.use(
    (res) => {
      //GraphQL API处理相关数据
      // const responseData = res.data; // 获取响应数据对象

      // if (responseData.errors) {
      //   // 处理错误信息
      //   const errorMessages = responseData.errors.map(error => error.message).join(', ');
      //   notify(errorMessages);
      //   return Promise.reject(responseData.errors);
      // }

      // const { data } = responseData; // 获取 GraphQL 查询的结果数据
      // return data;
      return res
    },
    (err) => {
      switch (err.response.result_status) {
        case 302:
          notify('接口重定向了！')
          break
        case 400:
          notify('参数不正确！')
          break
        case 401:
          // store.dispatch("outlogin")
          notify('登录过期,请重新登录')
          router.push('/login')
          // window.localStorage.removeItem("token");
          break

        case 403:
          notify('您没有权限操作！')
          break
        case 404:
          notify(`请求地址出错: ${err.response.config.url}`)
          break // 在正确域名下
        case 408:
          notify('请求超时！')
          break
        case 409:
          notify('系统已存在相同数据！')
          break
        case 500:
          notify('服务器内部错误！')
          break
        case 501:
          notify('服务未实现！')
          break
        case 502:
          notify('网关错误！')
          break
        case 503:
          notify('服务不可用！')
          break
        case 504:
          notify('服务暂时无法访问，请稍后再试！')
          break
        case 505:
          notify('HTTP版本不受支持！')
          break
        default:
          notify('异常问题，请联系管理员！')
          console.log()
          break
      }
      return Promise.reject(err)
    }
  )
  return service(axiosConfig)
}
export default myAxios
