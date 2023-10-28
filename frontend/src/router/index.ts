import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";

const routes: RouteRecordRaw[] = [
  {
    path: "/",
    redirect: "/home",
  },
  {
    path: "/login",
    name: "login",
    component: () => import("@/views/login/index.vue"),
  },
  {
    path: "/home",
    name: "home",
    component: () => import("@/views/home/index.vue"),
  },
  {
    path: "/postVideo",
    name: "postVideo",
    component: () => import("@/views/post/index.vue"),
  },
  {
    path: "/personalCenter/:user_id",
    name: "personalCenter",
    redirect(to) {
      return {
        name: "myVideos",
        params: {
          user_id: to.params.user_id,
        },
      };
    },
    component: () => import("@/views/personalCenter/index.vue"),
    children: [
      {
        path: "/personalCenter/:user_id/myVideos",
        name: "myVideos",
        component: () => import("@/views/personalCenter/myVideos/index.vue"),
      },
      {
        path: "/personalCenter/:user_id/myCollections",
        name: "myCollections",
        component: () =>
          import("@/views/personalCenter/myCollections/index.vue"),
      },
      {
        path: "/personalCenter/:user_id/myFollowsAndFans",
        name: "myFollowsAndFans",
        component: () =>
          import("@/views/personalCenter/myFollowsAndFans/index.vue"),
      },
      {
        path: "/personalCenter/:user_id/myInfo",
        name: "myInfo",
        component: () => import("@/views/personalCenter/myInfo/index.vue"),
      },
    ],
  },
];

export const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
