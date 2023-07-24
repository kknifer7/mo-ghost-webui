<template>
  <div
    class="captcha-box"
    :style="{
      'background-image': 'url(' + baseStyle.bgImage + ')',
      width: baseStyle.width + baseStyle.margin + 'px',
      height: baseStyle.height + baseStyle.margin + 'px',
      'border-radius': baseStyle.borderRadius + 'px',
    }"
  >
    <div
      class="captcha-container"
      :style="{
        width: baseStyle.width + 'px',
        height: baseStyle.height + 'px',
      }"
    >
      <div
        class="captcha-title"
        :style="{ 'justify-content': baseStyle.titleAlign }"
      >
        <span
          :style="{
            color: baseStyle.titleColor,
            'font-size': baseStyle.titleFontSize + 'px',
            'font-weight': baseStyle.titleWeight,
          }"
          >{{ baseStyle.title }}</span
        >
      </div>

      <div class="captcha-content">
        <img
          class="captcha-content-bg"
          :src="captchaData.captchaBackgroundImage"
          ref="captcha-bg-img"
        />
        <img
          class="captcha-content-move"
          :src="captchaData.captchaTemplateImage"
          ref="captcha-move-img"
        />
      </div>

      <div class="captcha-track">
        <div class="captcha-track-bar">
          <div
            class="captcha-track-bar-mask"
            ref="captcha-slider-move-track"
            :style="{
              borderColor: baseStyle.trackerColor,
              backgroundColor: baseStyle.trackerBgColor,
            }"
          ></div>
          <div class="captcha-track-bar-line"></div>
        </div>
        <div
          class="captcha-track-slider"
          @mousedown.prevent="sliderDown"
          @touchstart.prevent="sliderDown"
          ref="slider-move-btn"
        ></div>
      </div>

      <div class="captcha-footer">
        <!-- 占位 -->
        <div style="width: 80%"></div>
        <div class="captcha-foot-content">
          <div
            class="captcha-foot-content-refresh"
            @click="refreshCaptcha"
          ></div>
          <div class="captcha-foot-content-close" @click="closeCaptcha"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SliderCaptcha',
  props: {
    /** 基础样式 */
    baseStyle: {
      type: Object,
      default: function () {
        return {};
      },
    },
    /** 验证码数据 */
    captchaData: {
      type: Object,
      default: function () {
        return {};
      },
    },
  },
  data() {
    return {
      // 鼠标事件坐标移动数组
      trackList: [],
      startTime: null,
      stopTime: null,
      startX: null,
      startY: null,
    };
  },
  computed: {
    // 计算滑块滑行的宽度
    boxWidth: function () {
      let captchaWidth = this.$refs['captcha-bg-img'].width;
      let sliderWidth = this.$refs['slider-move-btn'].offsetWidth;
      return captchaWidth - sliderWidth;
    },
  },
  methods: {
    getEventTypeStr(res) {
      if (res instanceof MouseEvent) {
        return 'mouse';
      } else if (res instanceof TouchEvent) {
        return 'touch';
      } else {
        throw Error('wrong invoke');
      }
    },
    // 按下事件
    sliderDown(res) {
      let eventType = this.getEventTypeStr(res);
      if (eventType === 'touch') {
        if (
          res.touches.length === 0 ||
          res.touches[0].target.className !== 'captcha-track-slider'
        ) {
          // 排除掉触摸事件时没有触摸到滑块对象的情况
          return;
        }
        res = res.touches[0];
      }
      this.trackList = [];
      let startX = res.pageX;
      let startY = res.pageY;
      let startTime = new Date().getTime();
      this.startX = startX;
      this.startY = startY;
      this.startTime = startTime;
      this.trackList.push({
        x: res.pageX - startX,
        y: res.pageY - startY,
        type: 'down',
        t: new Date().getTime() - this.startTime,
      });

      this.$refs['slider-move-btn'].addEventListener(
        `${eventType}move`,
        this.sliderMove
      );
      this.$refs['slider-move-btn'].addEventListener(
        eventType === 'mouse' ? 'mouseup' : 'touchend',
        this.sliderUp
      );
    },
    // 移动事件
    sliderMove(res) {
      let eventType = this.getEventTypeStr(res);
      if (eventType === 'touch') {
        res = res.touches[0];
      }
      let moveX = res.pageX - this.startX;
      this.trackList.push({
        x: moveX,
        y: res.pageY - this.startY,
        type: 'move',
        t: new Date().getTime() - this.startTime,
      });

      if (moveX < 0) {
        moveX = 0;
      } else if (moveX > this.boxWidth) {
        moveX = this.boxWidth;
      }
      this.$refs['slider-move-btn'].style.transform =
        'translate(' + moveX + 'px, 0px)';
      this.$refs['captcha-move-img'].style.transform =
        'translate(' + moveX + 'px, 0px)';
      this.$refs['captcha-slider-move-track'].style.width = moveX + 'px';
    },
    // 抬起事件
    sliderUp(res) {
      let eventType = this.getEventTypeStr(res);
      if (eventType === 'touch') {
        res = res.changedTouches[0];
      }
      this.$refs['slider-move-btn'].removeEventListener(
        `${eventType}move`,
        this.sliderMove
      );
      this.$refs['slider-move-btn'].removeEventListener(
        eventType === 'mouse' ? 'mouseup' : 'touchend',
        this.sliderUp
      );
      let moveX = res.pageX - this.startX;
      this.stopTime = new Date().getTime();
      this.trackList.push({
        x: moveX,
        y: res.pageY - this.startY,
        type: 'up',
        t: new Date().getTime() - this.startTime,
      });
      this.$refs['slider-move-btn'].style.transform =
        'translate(' + moveX + 'px, 0px)';
      this.$refs['captcha-move-img'].style.transform =
        'translate(' + moveX + 'px, 0px)';
      this.$refs['captcha-slider-move-track'].style.width = moveX + 'px';

      // 收集轨迹记录 并传入后台验证
      let captchaValidate = {
        bgImageWidth: this.$refs['captcha-bg-img'].width,
        bgImageHeight: this.$refs['captcha-bg-img'].height,
        sliderImageWidth: this.$refs['captcha-move-img'].width,
        sliderImageHeight: this.$refs['captcha-move-img'].height,
        startSlidingTime: this.startTime,
        endSlidingTime: this.stopTime,
        trackList: this.trackList,
        data: null,
      };
      this.$emit('validateCaptcha', captchaValidate);
    },

    // 还原滑块
    reset() {
      this.$refs['slider-move-btn'].style.transform = 'translate(0px, 0px)';
      this.$refs['captcha-move-img'].style.transform = 'translate(0px, 0px)';
      this.$refs['captcha-slider-move-track'].style.width = '0px';
    },

    // 关闭验证码事件
    closeCaptcha() {
      this.$emit('closeCaptcha');
    },
    // 刷新验证码事件
    refreshCaptcha() {
      this.$emit('refreshCaptcha');
    },
  },
};
</script>

<style scoped>
.captcha-box {
  display: flex;
  justify-content: center;
  align-items: center;
}

.captcha-container {
  display: flex;
  flex-direction: column;
}

.captcha-title {
  width: 100%;
  height: 10%;
  display: flex;
  align-items: center;
}

.captcha-content {
  width: 100%;
  height: 65%;
  position: relative;
}

.captcha-content-bg {
  width: 100%;
  height: 100%;
  border-radius: 5px;
}

.captcha-content-move {
  left: 0;
  position: absolute;
  transform: translate(0px, 0px);
  height: 100%;
}

.captcha-footer {
  width: 100%;
  height: 10%;
  display: flex;
  align-items: center;
}

.captcha-foot-content {
  width: 20%;
  display: flex;
  justify-content: space-around;
}

.captcha-foot-content-refresh {
  width: 20px;
  height: 20px;
  background: url('../../assets/img/captcha-icon.png') no-repeat 0 -167px;
}

.captcha-foot-content-close {
  width: 20px;
  height: 20px;
  background: url('../../assets/img/captcha-icon.png') no-repeat 0 -14px;
}

.captcha-track {
  width: 100%;
  height: 15%;
  position: relative;
  display: flex;
  align-items: center;
}

.captcha-track-bar {
  width: 100%;
  height: 65%;
  background-color: #f5f5f5;
  opacity: 0.8;
  border-radius: 4px;
}

.captcha-track-bar-mask {
  border-color: rgb(239, 156, 13);
  background-color: rgb(247, 182, 69);
  width: 0px;
}

@keyframes captchaTrackBarLine {
  from {
    left: 0;
  }

  to {
    left: 100%;
  }
}

.captcha-track-bar-line {
  animation: captchaTrackBarLine 2s infinite;
  height: 65%;
  width: 5px;
  background-color: #fff;
  position: absolute;
  filter: opacity(0.5);
  box-shadow: 1px 1px 1px #fff;
}

.captcha-track-slider {
  background-image: url('../../assets/img/captcha-btn.png');
  background-repeat: no-repeat;
  background-size: contain;
  transform: translate(0px, 0px);
  position: absolute;
  width: 53px;
  height: 85%;
  border-radius: 5px;
}
</style>
