import { UploadSharding } from '@base/models';
import { MoFileUploadInfo } from './models';
import { Base64 } from 'js-base64';
import { uploadService } from '@api/service';
import { useUploaderStore } from 'src/stores/uploader-store';

const ShardSize = 1024 * 1024 * 20;

export default {
  cancelUpload: async function (fileKey: string, callback?: () => void) {
    await uploadService.deleteByFileKey(fileKey);
    callback ? callback() : void 0;
  },
  upload: async function (
    fileInfo: MoFileUploadInfo,
    shardUploadedCallback?: (fileInfo: MoFileUploadInfo) => void,
    finishCallback?: (fileInfo: MoFileUploadInfo) => void
  ) {
    const fileKey = fileInfo.fileKey;
    if (!useUploaderStore().getWorkingList.has(fileInfo)) {
      // 如果分片信息ID不在工作列表中，说明任务已经暂停或被取消，不进行任何操作
      return;
    }
    const file = fileInfo.file;
    const missingParts = (await uploadService.getMissingParts(fileKey)).data
      .data.value;
    let shardingId = -1;

    if (missingParts.length > 0) {
      for (let i = 0; i < missingParts.length; i++) {
        const shard = await this.getFileShard(
          fileInfo,
          fileKey,
          missingParts[i]
        );
        shardingId = (await uploadService.upload(shard)).data.data.id;
        shardUploadedCallback ? shardUploadedCallback(fileInfo) : void 0;
      }
    } else {
      const shardCount = this.getFileShardCount(file);
      for (let i = 0; i < shardCount; i++) {
        const shard = await this.getFileShard(fileInfo, fileKey, i);
        shardingId = (await uploadService.upload(shard)).data.data.id;
        shardUploadedCallback ? shardUploadedCallback(fileInfo) : void 0;
      }
    }
    finishCallback ? finishCallback(fileInfo) : void 0;

    return shardingId;
  },
  getFileShardCount: function (file: File) {
    return Math.ceil(file.size / ShardSize);
  },
  getFileShard: async function (
    fileInfo: MoFileUploadInfo,
    fileKey: string,
    index = 0
  ): Promise<UploadSharding> {
    const file = fileInfo.file;
    const businessType = fileInfo.businessType;
    const start = index * ShardSize;
    const end = Math.min((index + 1) * ShardSize, file.size);

    const slice = file.slice(start, end);
    const reader = new FileReader();

    return new Promise<UploadSharding>((resolve, reject) => {
      reader.onload = (event) => {
        const shardContent = Base64.fromUint8Array(
          new Uint8Array(event.target?.result as ArrayBuffer)
        );
        const uploadSharding: UploadSharding = {
          fileId: fileInfo.fileId,
          fileName: file.name,
          businessType,
          shardIndex: index,
          shardSize: ShardSize,
          shardCount: this.getFileShardCount(file),
          fileKey,
          fileSize: file.size,
          shardContent,
        };

        resolve(uploadSharding);
      };

      reader.onerror = () => {
        reject(new Error('Error reading file slice'));
      };

      reader.readAsArrayBuffer(slice);
    });
  },
};
