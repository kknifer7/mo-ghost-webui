import { boot } from 'quasar/wrappers';
import axios, { AxiosInstance } from 'axios';
import { Cookies, Notify } from 'quasar';
import Router from '../router';
import noti from '@utils/notification';

declare module '@vue/runtime-core' {
  interface ComponentCustomProperties {
    $axios: AxiosInstance;
  }
}

// local-dev
const baseURL = 'http://localhost:9100/api';
// prod ip
//const baseURL = 'http://218.62.100.7:8002/backend';
// prod 域名
// const baseURL = 'https://smg.knifer.fun:8002/backend';
// frp prod 域名
// const baseURL = 'https://smg.knifer.fun/backend';
const api = axios.create({ baseURL });

api.defaults.headers.post['Content-Type'] = 'application/json';
api.defaults.headers['X-Requested-With'] = 'XMLHttpRequest';
api.defaults.timeout = 7000;
api.interceptors.response.use(
  async (res) => {
    const noNeedResolve = res.headers['mo-ghost-no-need-resolve'];
    const contentType = res.headers['content-type'];
    if (
      noNeedResolve === '1' ||
      !contentType ||
      (contentType.lastIndexOf('octet-stream') !== -1 && res.status >= 200) ||
      (contentType.lastIndexOf('text/html') !== -1 && res.status >= 200)
    ) {
      /**
       * 不进行响应码校验的请求：
       * 1. 服务端设置了无需解析的响应头
       * 2. 没有content-type
       * 3. 下载文件请求
       * 4. 拉取H5页面请求
       */
      return res;
    }
    if (res.status !== 200 || res.data.code !== 200) {
      res.data.code
        ? noti.failByCode(res.data.code)
        : noti.fail(`服务端未知异常[${res.status}]`);
    }

    return res;
  },
  async (error) => {
    if (error.code === 'ETIMEDOUT' || error.code === 'ECONNABORTED') {
      Notify.create({
        type: 'negative',
        position: 'top',
        timeout: 2000,
        message: '服务端请求超时，请等待一段时间后重试',
      });
    } else if (error.response.status === 403) {
      // 凭证过期，删除Cookie，重定向回登录页
      Cookies.remove('login');
      Router.replace('/login');
    } else if (error.response.status === 429) {
      Notify.create({
        type: 'warning',
        position: 'top',
        timeout: 2000,
        message: '操作过于频繁，请刷新页面重试',
        textColor: 'white',
      });
    } else if (error.response.status === 400) {
      error.response.data && error.response.data.code
        ? noti.failByCode(error.response.data.code, '传参方式错误')
        : noti.fail('传参方式错误');
    } else {
      Notify.create({
        type: 'warning',
        position: 'top',
        timeout: 2000,
        message: `服务端未知异常[${error}]`,
        textColor: 'white',
      });
    }

    return error;
  }
);

export default boot(({ app }) => {
  app.config.globalProperties.$axios = axios;
  // ^ ^ ^ this will allow you to use this.$axios (for Vue Options API form)
  //       so you won't necessarily have to import axios in each vue file

  app.config.globalProperties.$api = api;
  // ^ ^ ^ this will allow you to use this.$api (for Vue Options API form)
  //       so you can easily perform requests against your app's API
});

export { api, baseURL };
