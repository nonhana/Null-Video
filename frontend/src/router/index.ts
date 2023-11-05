import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { getMyVideoListAPI } from '@/api/video/video'
/* 自行引入Pinia */
import pinia from '../stores/store'
import { useVideoListStore } from '@/stores/videoList'

const videoListStore = useVideoListStore(pinia)

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
  console.log(to)
  if (to.fullPath === '/home') {
    const res = await getMyVideoListAPI({ userId: '8' })
    if (res.code === 0) {
      videoListStore.setVideoList(res.data)
      console.log('videoListStore.videoList', videoListStore.videoList)
      next({
        name: 'home',
        params: {
          video_id: res.data[0].videoId
        }
      })
    }
  } else {
    next()
  }
})

export default router
