<template>
  <div class="captcha-component">
    <div v-if="type == 'SLIDER'" class="captcha-template">
      <slider-captcha
        :baseStyle="captcha.baseStyle"
        :captchaData="captcha.captchaData"
        ref="sliderCaptcha"
        @closeCaptcha="closeCaptcha"
        @refreshCaptcha="refreshCaptcha"
        @validateCaptcha="validateCaptcha"
      ></slider-captcha>
    </div>
  </div>
</template>

<script>
import SliderCaptcha from './SliderCaptcha.vue';
import { securityService } from '@api/service';
export default {
  name: 'CaptchaIndex',
  components: {
    SliderCaptcha,
  },
  props: {
    /** 验证码类型 */
    // SLIDER、ROTATE_DEGREE、CONCAT、ROTATE
    type: {
      type: String,
      default: 'SLIDER',
    },
    /** 验证码标题 */
    title: {
      type: String,
      default: '拖动滑块完成拼图',
    },
    /** 标题水平方向（justify-content 选值），flex-start、center、flex-end */
    titleAlign: {
      type: String,
      default: 'center',
    },
    /** 验证码标题字体 */
    titleFontSize: {
      type: Number,
      default: 15,
    },
    /** 验证码标题字体颜色 */
    titleColor: {
      type: String,
      default: 'rgb(230, 162, 60)',
    },
    /** 标题字体粗细 */
    titleWeight: {
      type: String,
      default: 'bold',
    },
    /** 背景图片 */
    // bgImage: {
    //   type: String,
    //   default: 'https://minio.tianai.cloud/public/captcha-btn/btn3-bg.jpg',
    // },
    /** 验证码宽度 */
    width: {
      type: Number,
      default: 300,
    },
    /** 验证码高度 */
    height: {
      type: Number,
      default: 300,
    },
    /** 验证码边距 */
    margin: {
      type: Number,
      default: 20,
    },
    /** 圆角 */
    borderRadius: {
      type: Number,
      default: 10,
    },
    /** 滑块背景图  */
    moveBtnImg: {
      type: String,
      default: 'https://minio.tianai.cloud/public/captcha-btn/btn3.png',
    },
    /** 滑块图片宽度 */
    moveBtnImgWidth: {
      type: Number,
      default: 53,
    },
    /** 滑块图片高度 */
    moveBtnImgHeight: {
      type: Number,
      default: 38,
    },
    /** 滑块滑过的border样式 */
    trackerColor: {
      type: String,
      default: '#ef9c0d',
    },
    /** 滑块滑过的背景样式 */
    trackerBgColor: {
      type: String,
      default: '#f7b645',
    },
  },
  data() {
    return {
      captcha: {
        baseStyle: {},
        captchaData: {},

        captchaType: null,
      },
    };
  },
  watch: {
    type: {
      handler(newVal) {
        this.captchaType = newVal;
      },
      deep: true,
      immediate: true,
    },
  },
  created() {
    // 初始化基础数据
    this.captcha.baseStyle = {
      title: this.title,
      titleFontSize: this.titleFontSize,
      titleColor: this.titleColor,
      titleAlign: this.titleAlign,
      width: this.width,
      height: this.height,
      margin: this.margin,
      borderRadius: this.borderRadius,
      bgImage: this.bgImage,
      moveBtnImg: this.moveBtnImg,
      moveBtnImgWidth: this.moveBtnImgWidth,
      moveBtnImgHeight: this.moveBtnImgHeight,
      trackerColor: this.trackerColor,
      trackerBgColor: this.trackerBgColor,
    };

    // 生成验证码
    this.getCode();
  },
  methods: {
    getCode() {
      securityService.gen(this.captchaType).then((res) => {
        if (res) {
          this.captchaType = this.captchaType;
          this.captcha.captchaData = {
            type: this.captchaType,
            captchaId: res.data.id,
            captchaBackgroundImage: res.data.captcha.backgroundImage,
            captchaBackgroundImageWidth: res.data.captcha.backgroundImageWidth,
            captchaBackgroundImageHeight:
              res.data.captcha.backgroundImageHeight,
            captchaTemplateImage: res.data.captcha.templateImage,
            captchaTemplateImageWidth: res.data.captcha.templateImageHeight,
            captchaTemplateImageHeight: res.data.captcha.templateImageWidth,
            data: res.data.captcha.data,
          };
        } else {
          console.log('生成验证码失败！');
        }
      });
    },

    // 关闭验证码
    closeCaptcha() {
      this.$emit('closeCaptcha');
    },
    // 刷新验证码
    refreshCaptcha() {
      this.getCode();
      this.$emit('refreshCaptcha');
    },

    // 验证
    validateCaptcha(data) {
      data.captchaId = this.captcha.captchaData.captchaId;
      data.type = this.captcha.captchaData.type;
      securityService.check(data.captchaId, data).then((res) => {
        console.log('check ===>', res);
        if (res.status === 200) {
          if (!res.data) {
            this.refreshCaptcha();
          }
          this.$emit('validateCaptcha', res.data);
        }
      });

      if (data.type == 'SLIDER') {
        this.$refs['sliderCaptcha'].reset();
      } else if (data.type == 'ROTATE') {
      } else if (data.type == 'CONCAT') {
      }
    },
  },
};
</script>

<style scoped>
.captcha-template {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
