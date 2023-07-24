import { Loading } from 'quasar';

const loadingUtils = {
  show: function (delay?: number) {
    Loading.show({ delay });
  },

  hide: function () {
    Loading.hide();
  },
};

export default loadingUtils;
