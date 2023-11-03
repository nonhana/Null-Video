import axios, { AxiosRequestConfig } from 'axios'
import { useRouter } from 'vue-router'
import { useNotification, NotificationType } from 'naive-ui'

const router = useRouter()
const notification = useNotification()

const notify = (content: string, type: NotificationType = 'warning') => {
  notification[type]({
    content: content,
    meta: 'warning',
    duration: 2500,
    keepAliveOnHover: true
  })
}

// 定义res.data的类型
interface Data {
  code: number
  data: any
  message: string
}

function myAxios(axiosConfig: AxiosRequestConfig): Promise<Data> {
  const service = axios.create({
    baseURL: 'http://124.222.255.122:8080',
    timeout: 30000
  })

  // 请求拦截器：可以在发请求之前可以处理一些业务
  service.interceptors.request.use((config) => {
    const token = localStorage.getItem('token')
    config.headers.Authorization = token ?? ''
    return config
  })

  // 响应拦截器：当服务器数据返回以后，可以处理一些事情
  service.interceptors.response.use(
    (res) => {
      if (res.data.code === 0) {
        return res.data
      } else {
        notify('发生错误' + res.data.code + '：' + res.data.message)
        return Promise.reject(res.data.message)
      }
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
          notify('登录过期,请重新登录')
          localStorage.clear()
          setTimeout(() => {
            router.push('/login')
          }, 2000)
          break
        case 403:
          notify('您没有权限操作！')
          break
        case 404:
          notify(`请求地址出错: ${err.response.config.url}`)
          break
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
          break
      }
      return Promise.reject(err)
    }
  )
  return service(axiosConfig)
}

export default myAxios
