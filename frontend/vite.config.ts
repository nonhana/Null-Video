import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { NaiveUiResolver } from 'unplugin-vue-components/resolvers'
import postCssPxToRem from 'postcss-pxtorem' // px转rem;

// https://vitejs.dev/config/
export default defineConfig({
  base: '/',
  plugins: [
    vue(),
    AutoImport({
      imports: [
        'vue',
        {
          'naive-ui': [
            'useDialog',
            'useMessage',
            'useNotification',
            'useLoadingBar'
          ]
        }
      ]
    }),
    Components({
      resolvers: [NaiveUiResolver()]
    })
  ],
  resolve: {
    // 配置路径别名@
    alias: {
      '@': path.resolve(__dirname, './src'),
      '@nullVideo': path.resolve(__dirname, './src/components')
    }
  },
  server: {
    host: '0.0.0.0', // ip地址
    port: 2000, // 端口号
    open: false // 是否自动打开浏览器
  },
  css: {
    // 此代码为适配移动端px2rem
    postcss: {
      plugins: [
        postCssPxToRem({
          rootValue: 16, // 1rem的大小（控制1rem的大小  点位：px）
          propList: ['*'] // 需要转换的属性，这里选择全部都进行转换
        })
      ]
    },
    // 注册全局 less 样式
    preprocessorOptions: {
      less: {
        charset: false,
        additionalData: `@import '@/variables.less';`
      }
    }
  }
})
