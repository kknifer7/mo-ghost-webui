const objectUtils = {
  toBoolean: function (object: unknown) {
    return typeof object !== 'undefined' && object !== null;
  },
  allToBoolean: function (...objects: Array<unknown>) {
    return objects.filter((obj) => !this.toBoolean(obj)).length === 0;
  },
};

export default objectUtils;
