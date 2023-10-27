# `Null-Video`前端项目

## 技术栈

`Vue3+Vite+Pinia+TypeScript+Naive UI`

## src 目录结构说明

- api: 存放 API 接口相关。
- assets: 存放静态资源，子目录分为 imgs、svgs 和 fonts，分别用于存放不同类型的资源。
- components
  - Little: 存放小组件，子目录分为 Common(通用组件)、Items(item 组件)、Utils(功能性组件)。
  - ModelXXX: 用于存放组成各个页面的下一级组件，其中 XXX 部分为 views 中各个页面名称的首字母大写，比如存放 home 页面的组件，就新建 ModelHome 的新目录。
- hooks: 存放自定义工具类 hooks。
- router: vue-router 的配置目录，在其下的 index.ts 中具体进行配置。
- stores: pinia 状态管理目录，其中文件名按照需要管理的部分来命名。比如将用户的信息放到 pinia 统一管理，则可以新建一个 userInfo.ts 的文件。
- styles: 存放自定义的样式，通常是为了配置现有组件库的主题色。
- utils: 存放工具文件，包括工具函数和自定义接口(interface)。
  - index.ts: 内部存放各种封装好的工具函数，编写并暴露。
  - types.ts: 内部存放自定义的接口(interface)。
- views: 存放主页面文件的目录，配置方式为新建一个和页面名称相同的子目录，并在该目录下面新建 index.vue 文件。比如我要新建一个个人中心页，我可以新建一个 personalCenter 的子目录，并在该目录下面新建 index.vue 文件。
