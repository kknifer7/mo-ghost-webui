const stringUtils = {
  isBlank: function (object?: string | null) {
    return (
      typeof object === 'undefined' || object === null || object.length === 0
    );
  },
  isNotBlank: function (object?: string | null) {
    return (
      typeof object !== 'undefined' && object !== null && object.length > 0
    );
  },
  isAllNotBlank: function (...objects: Array<string | null>) {
    return objects.filter((obj) => !this.isNotBlank(obj)).length === 0;
  },
  isAnyBlank: function (...objects: Array<string | null>) {
    return objects.filter((obj) => !this.isNotBlank(obj)).length > 0;
  },
  clip: function (
    obj: string | null,
    limitLength: number,
    ellipsisSymble = '...'
  ) {
    if (this.isBlank(obj)) {
      return '';
    } else {
      // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
      return obj!.length > limitLength
        ? // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
          obj!.substring(0, limitLength) + ellipsisSymble
        : obj;
    }
  },
};

export default stringUtils;
