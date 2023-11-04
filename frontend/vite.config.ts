// vite.config.ts
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { NaiveUiResolver } from 'unplugin-vue-components/resolvers'
import postCssPxToRem from 'postcss-pxtorem'
import viteCompression from 'vite-plugin-compression' // 用于gzip压缩
import imageminPlugin from 'vite-plugin-imagemin' // 用于图片压缩
import externalGlobals from 'rollup-plugin-external-globals' // 用于从CDN引入外部库
import svgLoader from 'vite-svg-loader'


export default defineConfig(() => {
  return {
    base: '/',
    plugins: [
      vue(), // 支持Vue单文件组件
      AutoImport({
        // 自动导入Vue等常用函数
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
        // 自动导入组件
        resolvers: [NaiveUiResolver()]
      }),
      viteCompression(), // 启用gzip压缩
      imageminPlugin({
        // 图片压缩配置
        // 压缩jpeg格式图片
        mozjpeg: {
          quality: 75 // 压缩质量从0到100
        },
        // 压缩png格式图片
        pngquant: {
          quality: [0.65, 0.9], // 压缩质量，该值是一个区间
          speed: 4 // 压缩速度，从1（最慢）到10（最快）
        },
        // 压缩svg格式图片
        svgo: {
          plugins: [
            {
              removeViewBox: false
            },
            {
              cleanupIDs: true
            },
          ],
        },
        // GIF图片的压缩
        gifsicle: {
          optimizationLevel: 1 // 1-3压缩等级，3是最慢的
        },
        // Webp格式
        webp: {
          quality: 75
        },
      }),
      svgLoader() // svg 引入组件
    ],
    resolve: {
      alias: {
        '@': path.resolve(__dirname, './src'), // 设置`@`指向`src`目录
        '@nullVideo': path.resolve(__dirname, './src/components'), // 通用组件
        '@nullSvg': path.resolve(__dirname, './src/assets/svgs')
      }
    },
    server: {
      port: 2000, // 开发服务器端口
      open: false // 是否自动打开浏览器
    },
    css: {
      postcss: {
        plugins: [
          postCssPxToRem({
            // px转rem插件配置
            rootValue: 16,
            propList: ['*']
          })
        ]
      },
      preprocessorOptions: {
        less: {
          charset: false,
          additionalData: `@import '@/variables.less';` // 全局引入变量文件
        }
      }
    },
    build: {
      rollupOptions: {
        output: {
          manualChunks(id) {
            // 代码分割逻辑
            // 如果模块是node_modules目录下的，可以返回它的包名
            if (id.includes('node_modules')) {
              return id
                .toString()
                .split('node_modules/')[1]
                .split('/')[0]
                .toString()
            }
          },
          plugins: [
            externalGlobals({
              // 使用CDN引入外部库配置
              vue: 'Vue',
              'vue-router': 'VueRouter',
              axios: 'axios'
            })
          ]
        }
      ]
    }),
    Components({
      // 自动导入组件
      resolvers: [NaiveUiResolver()]
    }),
    viteCompression(), // 启用gzip压缩
    imageminPlugin({
      // 图片压缩配置
      // 压缩jpeg格式图片
      mozjpeg: {
        quality: 75 // 压缩质量从0到100
      },
      // 压缩png格式图片
      pngquant: {
        quality: [0.65, 0.9], // 压缩质量，该值是一个区间
        speed: 4 // 压缩速度，从1（最慢）到10（最快）
      },
      // 压缩svg格式图片
      svgo: {
        plugins: [
          {
            removeViewBox: false
          },
          {
            cleanupIDs: true
          }
        ]
      },
      // GIF图片的压缩
      gifsicle: {
        optimizationLevel: 1 // 1-3压缩等级，3是最慢的
      },
      // Webp格式
      webp: {
        quality: 75
      }
    })
  ],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'), // 设置`@`指向`src`目录
      '@nullVideo': path.resolve(__dirname, './src/components') // 自定义别名
    }
  },
  server: {
    port: 2000, // 开发服务器端口
    open: false // 是否自动打开浏览器
  },
  css: {
    postcss: {
      plugins: [
        postCssPxToRem({
          // px转rem插件配置
          rootValue: 16, // 设置1rem为16px
          propList: ['*']
        })
      ]
    },
    preprocessorOptions: {
      less: {
        charset: false,
        additionalData: `@import '@/variables.less';` // 全局引入变量文件
      }
    }
  },
  build: {
    rollupOptions: {
      output: {
        manualChunks(id) {
          // 代码分割逻辑
          // 如果模块是node_modules目录下的，可以返回它的包名
          if (id.includes('node_modules')) {
            return id
              .toString()
              .split('node_modules/')[1]
              .split('/')[0]
              .toString()
          }
        },
        plugins: [
          externalGlobals({
            // 使用CDN引入外部库配置
            vue: 'Vue',
            'vue-router': 'VueRouter',
            axios: 'axios'
          })
        ]
      }
    },
    chunkSizeWarningLimit: 2000, // chunk大小警告的限制
    cssCodeSplit: true, // 启用CSS代码分割
    sourcemap: false, // 不生成sourcemap
    assetsInlineLimit: 5000 // 小于5kb的资源将内联为Base64编码
  }
})
