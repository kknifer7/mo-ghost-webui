import { SingleRelease } from '@/single-release/models';

export interface SingleReleaseCdk {
  id: number;
  code: string;
  expireAt: string;
  expired: boolean;
  lastAccessIP: string;
  lastAccessRegion: string;
  totalAccess: number;
  cdkStatus: string;
  remark: string;
  singleReleases: SingleRelease[];
  srIds?: number[]; // 用于更新
}

export interface SingleReleaseCdkAdd {
  srIds: number[];
  expireAt: string;
  cdkStatus: string;
  remark: string;
}

export const columns = [
  {
    name: 'remark',
    label: '备注💠',
    field: 'remark',
    align: 'center',
  },
  {
    name: 'cdkStatus',
    label: '状态💠',
    field: 'cdkStatus',
    align: 'center',
  },
  {
    name: 'code',
    label: '访问标识符💠',
    field: 'code',
    align: 'center',
  },
  {
    name: 'authWord',
    label: '身份标识符💠',
    field: 'authWord',
    align: 'center',
  },
  {
    name: 'expireAt',
    label: '过期时间💠',
    field: 'expireAt',
    align: 'center',
  },
  {
    name: 'expired',
    label: '是否过期',
    field: 'expired',
    align: 'center',
  },
  {
    name: 'lastAccessAt',
    label: '上次访问时间',
    field: 'lastAccessAt',
    align: 'center',
  },
  {
    name: 'lastAccessIP',
    label: '上次访问IP',
    field: 'lastAccessIP',
    align: 'center',
  },
  {
    name: 'lastAccessRegion',
    label: '上次访问地区',
    field: 'lastAccessRegion',
    align: 'center',
  },
  {
    name: 'totalAccess',
    label: '总访问量',
    field: 'totalAccess',
    align: 'center',
  },
  {
    name: 'option',
    label: '操作',
    field: 'option',
    align: 'center',
  },
];
