import { defineStore } from 'pinia'
import type { HomeVideoInfo } from '@/utils/types'

export const useVideoListStore = defineStore('videoList', {
  state: () => ({
    videoList: <HomeVideoInfo[]>[]
  }),
  actions: {
    setVideoList(videoList: HomeVideoInfo[]) {
      this.videoList = videoList
    },
    addVideoList(videoList: HomeVideoInfo[]) {
      this.videoList.push(...videoList)
    },
    clearVideoList() {
      this.videoList = []
    }
  },
  persist: true
})
