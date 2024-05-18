import fileUploader from '@tools/file-uploader';
import { MoFileUploadInfo } from '@tools/file-uploader/models';
import { defineStore } from 'pinia';

export const useUploaderStore = defineStore('uploader', {
  state: () => ({
    runningFlag: false,
    workingList: new Set<MoFileUploadInfo>(),
  }),
  getters: {
    getRunningFlag: (state) => state.runningFlag,
    getWorkingList: (state) => state.workingList,
  },
  actions: {
    setRunningFlag(flag: boolean) {
      this.runningFlag = flag;
    },
    sendTask(
      fileInfo: MoFileUploadInfo,
      shardUploadedCallback?: (fileInfo: MoFileUploadInfo) => void,
      finishCallback?: (fileInfo: MoFileUploadInfo) => void
    ) {
      this.workingList.add(fileInfo);
      fileUploader.upload(
        fileInfo,
        (fileInfo) =>
          shardUploadedCallback ? shardUploadedCallback(fileInfo) : void 0,
        (fileInfo) => {
          this.cancelTaskByFileKey(fileInfo.fileKey);
          finishCallback ? finishCallback(fileInfo) : void 0;
        }
      );
    },
    cancelTask(fileInfo: MoFileUploadInfo, callback?: () => void) {
      this.workingList.delete(fileInfo);
      if (this.workingList.size < 1) {
        this.runningFlag = false;
      }
      fileUploader.cancelUpload(fileInfo.fileKey, callback);
    },
    cancelTaskByShardingId(shardingId: number, callback?: () => void) {
      let targetElem = null;

      this.workingList.forEach((fileInfo) => {
        if (fileInfo.fileId === shardingId) {
          targetElem = fileInfo;
        }
      });
      if (targetElem) {
        this.cancelTask(targetElem, callback);
      }
    },
    cancelTaskByFileKey(fileKey: string, callback?: () => void) {
      let targetElem = null;

      this.workingList.forEach((fileInfo) => {
        if (fileInfo.fileKey === fileKey) {
          targetElem = fileInfo;
        }
      });
      if (targetElem) {
        this.cancelTask(targetElem, callback);
      }
    },
    existTaskByFileKey(fileKey: string) {
      let flag = false;

      this.workingList.forEach((fileInfo) => {
        if (fileInfo.fileKey === fileKey) {
          flag = true;
        }
      });

      return flag;
    },
  },
});
