import { Notify } from 'quasar';

const ErrorCodes = new Map([
  [1001, '文件删除失败，因为有针对该文件的单发布'],
  [4001, '账号或密码错误'],
  [4002, '验证失败'],
  [4003, '标识符已存在，请更换'],
]);

const noti = {
  success: function (message = '操作成功') {
    Notify.create({
      message: message,
      position: 'bottom-right',
      timeout: 2000,
      type: 'positive',
    });
  },
  fail: function (message = '操作失败') {
    Notify.create({
      message: message,
      position: 'top',
      timeout: 2000,
      type: 'warning',
      textColor: 'white',
    });
  },
  failByCode: function (code: number, fallbackMsg = '未知错误') {
    const msg = ErrorCodes.get(code);

    Notify.create({
      message: msg ? msg : fallbackMsg,
      position: 'top',
      timeout: 2000,
      type: 'warning',
      textColor: 'white',
    });
  },
};

export default noti;
