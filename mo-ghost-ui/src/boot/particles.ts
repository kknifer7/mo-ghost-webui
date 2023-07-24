import { boot } from 'quasar/wrappers';
import { loadFull } from 'tsparticles';
import { Engine } from 'tsparticles/types/bundle';
import Particles from 'particles.vue3';

export const particlesInit = async (engine: Engine) => {
  await loadFull(engine);
};

export const particlesLoaded = async (container: unknown) => {
  console.log('Particles container loaded', container);
};

export const particles = {
  background: {
    color: {
      value: '#F8F8FF', // 背景颜色
    },
    opacity: 1,
  },
  fpsLimit: 60,
  interactivity: {
    events: {
      onClick: {
        enable: true,
        mode: 'push', // 可用的click模式有: "push", "remove", "repulse", "bubble"。
      },
      onHover: {
        enable: true,
        mode: 'grab', // 可用的hover模式有: "grab", "repulse", "bubble"。
      },
      resize: true,
    },
    modes: {
      bubble: {
        distance: 400,
        duration: 2,
        opacity: 0.8,
        size: 30,
      },
      push: {
        quantity: 4,
      },
      repulse: {
        distance: 200,
        duration: 0.4,
      },
    },
  },
  particles: {
    color: {
      value: '#00FFFF',
    },
    links: {
      color: '#00CED1', // '#dedede'。线条颜色。
      distance: 150, // 线条长度
      enable: true, // 是否有线条
      opacity: 0.5, // 线条透明度。
      width: 1, // 线条宽度。
    },
    collisions: {
      enable: false,
    },
    move: {
      direction: 'none',
      enable: true,
      outMode: 'bounce',
      random: false,
      speed: 4, // 粒子运动速度。
      straight: false,
    },
    number: {
      density: {
        enable: true,
        area: 800,
      },
      value: 70, // 粒子数量。
    },
    opacity: {
      value: 0.5, // 粒子透明度。
    },
    shape: {
      type: 'circle', // 可用的粒子外观类型有："circle","edge","triangle", "polygon","star"
    },
    size: {
      random: true,
      value: 5,
    },
  },
  detectRetina: true,
};

// "async" is optional;
// more info on params: https://v2.quasar.dev/quasar-cli/boot-files
export default boot(({ app }) => {
  app.use(Particles);
});
