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
    path: "/home/:id",
    name: "home",
    component: () => import("@/views/home/index.vue"),
  },
  {
    path: "/postVideo",
    name: "postVideo",
    component: () => import("@/views/post/index.vue"),
  },
  {
    path: "/personalCenter/:id",
    name: "personalCenter",
    component: () => import("@/views/personalCenter/index.vue"),
  },
];

export const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
