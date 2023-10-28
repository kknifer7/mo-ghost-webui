import { SingleReleaseCdk, SingleReleaseCdkAdd } from '@/access-control/models';
import { MoFile } from '@/version/models';
import { api } from 'src/boot/axios';
import { baseURL } from 'src/boot/axios';
import { SingleRelease } from 'src/components/single-release/models';

const singleReleaseService = {
  findList: async function (page: number, size: number) {
    return api.get('/single-release/', buildParams(page, size));
  },
  findListByCdkId: async function (cdkId: number) {
    return api.get(`/single-release-cdk-aso/${cdkId}`);
  },
  update: async function (data: SingleRelease) {
    return api.patch('/single-release/', data);
  },
  replaceFile: async function (data: SingleRelease, newFile: MoFile) {
    data.fileId = newFile.id;

    return this.update(data);
  },
  add: async function (file: MoFile) {
    return api.post('/single-release/', {
      fullName: `发布-${file.originName}`,
      fileId: file.id,
      releaseStatus: 'CREATED',
    });
  },
  delete: async function (id: number) {
    return api.delete(`/single-release/${id}`);
  },
};

const moFileService = {
  findSingleList: async function (page: number, size: number) {
    return api.get('/mo-file/single', buildParams(page, size));
  },
  download: async function (id: number) {
    return api.get(`/mo-file/${id}`, {
      responseType: 'blob',
      timeout: 7200000,
    });
  },
  getUploadUrl: function (versionId?: number) {
    return `${baseURL}/mo-file/${versionId ? versionId : ''}`;
  },
  getReplaceUrl: function (fileId: number) {
    return `${baseURL}/mo-file/${fileId}`;
  },
  rename: async function (data: { id: number; name: string }) {
    return api.patch(`/mo-file/name/${data.id}/${data.name}`);
  },
  updateRemark: async function (data: { id: number; remark: string }) {
    return api.patch(`/mo-file/remark/${data.id}/${data.remark}`);
  },
  delete: async function (id: number) {
    return api.delete(`/mo-file/${id}`);
  },
  replace: async function (id: number) {
    return api.patch(`/mo-file/${id}`);
  },
};

const singleReleaseCdkService = {
  findList: async function (page: number, size: number) {
    return api.get('/single-release-cdk/', buildParams(page, size));
  },
  update: async function (data: SingleReleaseCdk) {
    return api.patch('/single-release-cdk/', data);
  },
  add: async function (data: SingleReleaseCdkAdd) {
    return api.post('/single-release-cdk/', data);
  },
  delete: async function (id: number) {
    return api.delete(`/single-release-cdk/${id}`);
  },
  getDownloadLink: function (code: string, releaseId: number) {
    return `${baseURL}/mo-file/single-release/${code}/${releaseId}`;
  },
  deleteAuthWord: async function (id: number) {
    return api.delete(`/single-release-cdk/auth-word/${id}`);
  },
};

const securityService = {
  updateAccount: async function (username: string, password: string) {
    return api.patch('/settings/account', { username, password });
  },
  gen: async function (type: string) {
    return api.get('/security/gen', { params: { type } });
  },
  check: async function (captchaId: string, data: unknown) {
    return api.post(`/security/check?id=${captchaId}`, data);
  },
  login: async function (data: { username: string; password: string }) {
    return api.post('/auth/', data);
  },
  logout: async function () {
    return api.delete('/auth/');
  },
};

function buildParams(
  page: number,
  size: number,
  sorts?: string,
  filter?: unknown
) {
  return {
    params: {
      page,
      size,
      sorts,
      filter,
    },
  };
}

export {
  singleReleaseService,
  moFileService,
  singleReleaseCdkService,
  securityService,
};
