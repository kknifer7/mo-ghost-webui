export interface UploadSharding {
  id: number;
  fileName: string;
  fileKey: string;
  businessType: BusinessType;
  shardIndex: number;
  shardCount: number;
  doneFlag: boolean;
}

export enum BusinessType {
  SingleRelease = 'SINGLE_RELEASE',
  SingleReleaseFileReplace = 'SINGLE_RELEASE_FILE_REPLACE',
  VersionRelease = 'VERSION_RELEASE',
}
