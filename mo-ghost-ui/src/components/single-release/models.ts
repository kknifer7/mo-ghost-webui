export interface SingleRelease {
  id: number;
  fullName: string;
  fileId: number;
  fileName: string;
  fileSize: string;
  releaseStatus: string;
  totalAccess: string;
  remark: string;
  ctime: string;
  mtime: string;
}

const columns = [
  {
    name: 'fullName',
    label: '名称💠',
    field: 'fullName',
    align: 'center',
  },
  {
    name: 'fileName',
    label: '文件名称💠',
    field: 'fileName',
    align: 'center',
  },
  {
    name: 'fileSize',
    label: '文件大小',
    field: 'fileSize',
    align: 'center',
  },
  {
    name: 'releaseStatus',
    label: '状态💠',
    field: 'releaseStatus',
    align: 'center',
  },
  {
    name: 'totalAccess',
    label: '总访问量',
    field: 'totalAccess',
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

export { columns };
