import { Dict, DictItem } from './modules';

const SingleReleaseStatus = new Dict(
  new Map([
    [
      'CREATED',
      DictItem.of('创建', 'add', 'text-primary text-bold', 'primary'),
    ],
    [
      'PUBLISHED',
      DictItem.of('发布', 'add', 'text-positive text-bold', 'positive'),
    ],
    [
      'DISABLED',
      DictItem.of('下架', 'add', 'text-negative text-bold', 'negative'),
    ],
  ])
);

const SingleReleaseCdkStatus = new Dict(
  new Map([
    [
      'ACTIVATED',
      DictItem.of('生效', 'add', 'text-positive text-bold', 'positive'),
    ],
    [
      'BANNED',
      DictItem.of('封禁', 'add', 'text-negative text-bold', 'negative'),
    ],
  ])
);

const PeriodQueryStatus = new Dict(
  new Map([
    ['ALL', DictItem.of('全部')],
    ['ACTIVATED', DictItem.of('有效')],
    ['EXPIRED', DictItem.of('过期')],
    ['ABOUT_TO_EXPIRED', DictItem.of('即将过期（1个月内）')],
  ])
);

export { SingleReleaseStatus, SingleReleaseCdkStatus, PeriodQueryStatus };
