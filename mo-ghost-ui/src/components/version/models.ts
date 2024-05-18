export interface Version {
  id: number;
  name: string;
  versionStatus: string;
  updatingType: string;
  ctime: string;
  mtime: string;
}

export interface MoFile {
  id: number;
  originName: string;
  path: string;
  size: string;
  state: MoFileState;
  remark: string;
  versions: Version[];
  ctime: string;
  mtime: string;
}

export enum MoFileState {
  Normal = 'NORMAL',
  Merging = 'MERGING',
  MergeFailed = 'MERGE_FAILED',
}

export const columns = [
  {
    name: 'originName',
    label: '名称💠',
    field: 'originName',
    align: 'center',
  },
  {
    name: 'path',
    label: '路径',
    field: 'path',
    align: 'center',
  },
  {
    name: 'size',
    label: '大小',
    field: 'size',
    align: 'center',
  },
  {
    name: 'state',
    label: '状态',
    field: 'state',
    align: 'center',
  },
  {
    name: 'remark',
    label: '备注💠',
    field: 'remark',
    align: 'center',
  },
  {
    name: 'ctime',
    label: '创建时间',
    field: 'ctime',
    align: 'center',
  },
  {
    name: 'mtime',
    label: '修改时间',
    field: 'mtime',
    align: 'center',
  },
  {
    name: 'option',
    label: '操作',
    field: 'option',
    align: 'center',
  },
];
