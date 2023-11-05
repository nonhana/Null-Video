import axios, { AxiosRequestConfig } from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

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
        console.log('error', res)
        window.alert('发生错误' + res.data.code + '：' + res.data.message)
        switch (res.data.code) {
          case 40000:
            // 请求参数错误
            window.alert('请求参数错误')
            break
          case 40100:
            // 处理未登录
            window.alert('未登录')
            break
          case 40101:
            // 处理无权限
            window.alert('无权限')
            break
          case 40400:
            // 处理未找到
            window.alert('请求资源未找到')
            break
          case 40300:
            // 处理禁止访问
            window.alert('禁止访问')
            break
          case 40500:
            // 用户登陆状态异常
            window.alert('用户登陆状态异常')
            break
          case 50000:
            // 处理系统错误
            window.alert('系统内部错误')
            break
          case 50001:
            // 处理操作错误
            window.alert('操作失败')
            break
          case 1:
            // 处理文件上传错误
            window.alert('上传文件失败')
            break
          default:
            // 处理其他错误
            window.alert('发生未知错误')
            break
        }
        return Promise.reject(res.data.message)
      }
    },
    (err) => {
      switch (err.response.result_status) {
        case 302:
          window.alert('接口重定向了！')
          break
        case 400:
          window.alert('参数不正确！')
          break
        case 401:
          window.alert('登录过期,请重新登录')
          setTimeout(() => {
            router.push('/login')
          }, 2000)
          break
        case 403:
          window.alert('您没有权限操作！')
          break
        case 404:
          window.alert(`请求地址出错: ${err.response.config.url}`)
          break
        case 408:
          window.alert('请求超时！')
          break
        case 409:
          window.alert('系统已存在相同数据！')
          break
        case 500:
          window.alert('服务器内部错误！')
          break
        case 501:
          window.alert('服务未实现！')
          break
        case 502:
          window.alert('网关错误！')
          break
        case 503:
          window.alert('服务不可用！')
          break
        case 504:
          window.alert('服务暂时无法访问，请稍后再试！')
          break
        case 505:
          window.alert('HTTP版本不受支持！')
          break
        default:
          window.alert('异常问题，请联系管理员！')
          break
      }
      return Promise.reject(err)
    }
  )
  return service(axiosConfig)
}

export default myAxios
