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
    component: () => import("@/views/personalCenter/index.vue"),
    children: [
      {
        path: "/personalCenter/myVideos/:user_id",
        name: "myVideo",
        component: () => import("@/views/personalCenter/myVideos/index.vue"),
      },
      {
        path: "/personalCenter/myCollections/:user_id",
        name: "myCollection",
        component: () =>
          import("@/views/personalCenter/myCollections/index.vue"),
      },
      {
        path: "/personalCenter/myFollowsAndFans/:user_id",
        name: "myFollowsAndFans",
        component: () =>
          import("@/views/personalCenter/myFollowsAndFans/index.vue"),
      },
      {
        path: "/personalCenter/myInfo/:user_id",
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
