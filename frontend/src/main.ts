import { createApp } from 'vue'
import App from './App.vue'
import 'amfe-flexible'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import router from './router'
import VueCookies from 'vue-cookies'

const baseSize = 16

// 设置 rem 函数
function setRem() {
  // 当前页面宽度缩放比例，可根据自己需要修改
  const scale = document.documentElement.clientWidth / 1920
  // 设置页面根节点字体大小
  document.documentElement.style.fontSize = baseSize * Math.min(scale, 2) + 'px'
}
// 初始化 rem
setRem()
// 改变窗口大小时重新设置 rem
window.onresize = function () {
  setRem()
}

const store = createPinia()
store.use(piniaPluginPersistedstate) // 引入pinia持久化插件

const app = createApp(App)
app.use(VueCookies)
app.use(store)
app.use(router)
app.mount('#app')
