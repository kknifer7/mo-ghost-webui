export enum BusinessType {
  SingleRelease = 'SINGLE_RELEASE',
  VersionRelease = 'VERSION_RELEASE',
}

export interface MoFileUploadInfo {
  fileId?: number;
  fileKey: string;
  file: File;
  businessType: BusinessType;
}
