import stringUtils from './string';

const dateTimePattern = new RegExp(
  /^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\s+(20|21|22|23|[0-1]\d):[0-5]\d:[0-5]\d$/
);

const dateTimeUtils = {
  isValid: function (dateTime?: string | null) {
    return stringUtils.isNotBlank(dateTime) && dateTimePattern.test(dateTime!);
  },
};

export default dateTimeUtils;
