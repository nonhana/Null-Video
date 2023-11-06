import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { getRandomVideoAPI } from '@/api/video/video'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/home/:video_id',
    name: 'home',
    component: () => import('@/views/home/index.vue')
  },
  {
    path: '/postVideo',
    name: 'postVideo',
    component: () => import('@/views/post/index.vue')
  },
  {
    path: '/personalCenter/:user_id',
    name: 'personalCenter',
    redirect(to) {
      return {
        name: 'myVideos',
        params: {
          user_id: to.params.user_id
        }
      }
    },
    component: () => import('@/views/personalCenter/index.vue'),
    children: [
      {
        path: '/personalCenter/:user_id/myVideos',
        name: 'myVideos',
        component: () => import('@/views/personalCenter/myVideos/index.vue')
      },
      {
        path: '/personalCenter/:user_id/myCollections',
        name: 'myCollections',
        component: () =>
          import('@/views/personalCenter/myCollections/index.vue')
      },
      {
        path: '/personalCenter/:user_id/myFollowsAndFans',
        name: 'myFollowsAndFans',
        component: () =>
          import('@/views/personalCenter/myFollowsAndFans/index.vue')
      },
      {
        path: '/personalCenter/:user_id/myPlayer/:video_id',
        name: 'myPlayer',
        component: () => import('@/views/personalCenter/myPlayer/index.vue')
      }
    ]
  }
]

export const router = createRouter({
  history: createWebHistory(),
  routes
})

// 如果路由跳转到home，需要先获取一些视频的数据，取其中的第一个视频数据的video_id，然后跳转到home/:video_id
router.beforeEach(async (to, _, next) => {
  if (to.fullPath === '/home') {
    if (localStorage.getItem('videoTypes')) {
      // 筛选出selected为true的videoType
      const videoTypes = JSON.parse(localStorage.getItem('videoTypes')!).filter(
        (item: any) => item.selected
      ) as any[]
      console.log('videoTypes', videoTypes)
      const flag = videoTypes.some((item) => item.id === 'all')
      if (!flag) {
        // 获取每个videoType的视频数据
        const res = await Promise.all(
          videoTypes.map((item: any) =>
            getRandomVideoAPI({
              videoTypeId: item.id
            })
          )
        )
        // 把获取到的所有视频数据放到localStorage中
        localStorage.setItem(
          'videoList',
          JSON.stringify(res.map((item) => item.data))
        )
        next({
          name: 'home',
          params: {
            video_id: res[0].data[0].videoId
          }
        })
      }
    }
    const res = await getRandomVideoAPI({})
    if (res.code === 0) {
      localStorage.setItem('videoList', JSON.stringify(res.data))
      next({
        name: 'home',
        params: {
          video_id: res.data[0].videoId
        }
      })
    }
  }
  next()
})

export default router
