import { Menu } from '../base/models';

export default [
  {
    id: 101,
    icon: 'inbox',
    label: '单发布管理',
    separator: true,
    children: [
      {
        id: 111,
        icon: 'view_list',
        label: '发布列表',
        path: '/single-release/list',
      },
      {
        id: 112,
        icon: 'lock',
        label: '访问控制',
        path: '/access-control/cdk-list',
      },
    ],
  },
  {
    id: 201,
    icon: 'folder_open',
    label: '版本发布管理',
    separator: true,
    children: [
      {
        id: 211,
        icon: 'snippet_folder',
        label: '独立文件',
        path: '/version/single-list',
      },
    ],
  },
  {
    id: 301,
    icon: 'settings',
    label: '系统设置',
    children: [
      {
        id: 311,
        icon: 'security',
        label: '安全设置',
        path: '/security',
      },
    ],
  },
] as Menu[];
