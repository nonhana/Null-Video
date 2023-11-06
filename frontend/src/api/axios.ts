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
        console.log('发生错误' + res.data.code + '：' + res.data.message)
        switch (res.data.code) {
          case 40000:
            // 请求参数错误
            console.log('请求参数错误')
            break
          case 40100:
            // 处理未登录
            console.log('未登录')
            break
          case 40101:
            // 处理无权限
            console.log('无权限')
            break
          case 40400:
            // 处理未找到
            console.log('请求资源未找到')
            break
          case 40300:
            // 处理禁止访问
            console.log('禁止访问')
            break
          case 40500:
            // 用户登陆状态异常
            console.log('用户登陆状态异常')
            break
          case 50000:
            // 处理系统错误
            console.log('系统内部错误')
            break
          case 50001:
            // 处理操作错误
            console.log('操作失败')
            break
          case 1:
            // 处理文件上传错误
            console.log('上传文件失败')
            break
          default:
            // 处理其他错误
            console.log('发生未知错误')
            break
        }
        return Promise.reject(res.data.message)
      }
    },
    (err) => {
      switch (err.response.result_status) {
        case 302:
          console.log('接口重定向了！')
          break
        case 400:
          console.log('参数不正确！')
          break
        case 401:
          console.log('登录过期,请重新登录')
          setTimeout(() => {
            router.push('/login')
          }, 2000)
          break
        case 403:
          console.log('您没有权限操作！')
          break
        case 404:
          console.log(`请求地址出错: ${err.response.config.url}`)
          break
        case 408:
          console.log('请求超时！')
          break
        case 409:
          console.log('系统已存在相同数据！')
          break
        case 500:
          console.log('服务器内部错误！')
          break
        case 501:
          console.log('服务未实现！')
          break
        case 502:
          console.log('网关错误！')
          break
        case 503:
          console.log('服务不可用！')
          break
        case 504:
          console.log('服务暂时无法访问，请稍后再试！')
          break
        case 505:
          console.log('HTTP版本不受支持！')
          break
        default:
          console.log('异常问题，请联系管理员！')
          break
      }
      return Promise.reject(err)
    }
  )
  return service(axiosConfig)
}

export default myAxios
