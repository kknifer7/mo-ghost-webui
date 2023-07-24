import { Cookies } from 'quasar';
import { RouteRecordRaw } from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      {
        path: '',
        component: () => import('pages/IndexPage.vue'),
      },
      {
        path: '/single-release/list',
        component: () => import('pages/SingleReleasePage.vue'),
      },
      {
        path: '/version/single-list',
        component: () => import('pages/VersionSinglePage.vue'),
      },
      {
        path: '/access-control/cdk-list',
        component: () => import('pages/AccessControlPage.vue'),
      },
      {
        path: '/security',
        component: () => import('pages/SecurityPage.vue'),
      },
    ],
    beforeEnter: function () {
      return Cookies.get('login') === '1' ? true : '/login';
    },
  },
  {
    path: '/login',
    component: () => import('pages/LoginPage.vue'),
    beforeEnter: function () {
      return Cookies.get('login') === '1' ? '/' : true;
    },
  },
  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue'),
  },
];

export default routes;
