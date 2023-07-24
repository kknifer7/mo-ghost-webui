const colorUtils = {
  getRandomHex: function (withNumberSign = true) {
    return (
      (withNumberSign ? '#' : '') +
      ((Math.random() * 0x1000000) << 0).toString(16).substring(-6)
    );
  },
};

export default colorUtils;
