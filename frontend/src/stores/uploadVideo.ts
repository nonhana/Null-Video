import { defineStore } from 'pinia'
import type { UploadVideoInfo } from '@/utils/types'

export const useUploadVideoStore = defineStore('uploadVideo', {
  state: () => ({
    uploadVideoInfo: <UploadVideoInfo>{
      video_url: '',
      video_title: '',
      video_description: '',
      video_cover: '',
      video_type: '',
      video_tags: [],
      video_permission: ''
    }
  }),
  actions: {
    setVideoUrl(videoUrl: string) {
      this.uploadVideoInfo.video_url = videoUrl
    },
    reset() {
      this.uploadVideoInfo = <UploadVideoInfo>{
        video_url: '',
        video_title: '',
        video_description: '',
        video_cover: '',
        video_type: '',
        video_tags: [],
        video_permission: ''
      }
    }
  },
  persist: true
})
