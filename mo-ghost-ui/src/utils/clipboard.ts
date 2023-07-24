import { copyToClipboard, Notify } from 'quasar';

const clipboardUtils = {
  save: function (content: string) {
    copyToClipboard(content)
      .then(() => {
        Notify.create({
          type: 'positive',
          position: 'bottom-right',
          timeout: 2000,
          message: '已复制到粘贴板',
        });
      })
      .catch(() => {
        Notify.create({
          type: 'warning',
          position: 'bottom-right',
          timeout: 2000,
          message: '复制失败，您的浏览器可能不支持该项操作',
        });
      });
  },
};

export default clipboardUtils;
