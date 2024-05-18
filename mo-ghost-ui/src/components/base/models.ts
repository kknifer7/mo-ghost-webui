import { AxiosResponse } from 'axios';

/**
 * 全局事件
 */
export const GlobalEvents = {
  NewUploadTask: 'new-upload-task', // 新上传任务
  FinishUploadTask: 'finish-upload-task', // 上传任务完成
};

/**
 * 菜单
 */
export interface Menu {
  id: number;
  icon: string;
  label: string;
  separator?: boolean;
  children?: Menu[];
  path?: string;
}

/**
 * 表格列
 */
export interface TableColumn {
  name: string;
  label: string;
  field: string;
  align: string;
}

/**
 * 分页
 */
export class Pagination {
  page: number;
  size: number;
  rowsPerPage: number;
  rowsNumber: number;

  static readonly FIRST_PAGE_NUMBER = 1;
  static readonly DEFAULT_ROWS_PER_PAGE = 10;
  static readonly PAGE_MAX_SIZE = 2000;

  constructor(
    page: number,
    size: number,
    rowsPerPage: number,
    rowsNumber: number
  ) {
    this.page = page;
    this.size = size;
    this.rowsPerPage = rowsPerPage;
    this.rowsNumber = rowsNumber;
  }

  static readonly EMPTY = new Pagination(0, 0, 0, 0);

  public reset(): void {
    this.page = Pagination.FIRST_PAGE_NUMBER;
    this.size = 0;
    this.rowsPerPage = Pagination.DEFAULT_ROWS_PER_PAGE;
    this.rowsNumber = -1;
  }

  public static of(
    page: number,
    size: number,
    rowsPerPage: number,
    rowsNumber = -1
  ): Pagination {
    return new Pagination(
      page,
      size,
      rowsPerPage < 1 ? this.PAGE_MAX_SIZE : rowsPerPage,
      rowsNumber
    );
  }

  public static ofDefault(): Pagination {
    return Pagination.of(this.FIRST_PAGE_NUMBER, 0, this.DEFAULT_ROWS_PER_PAGE);
  }

  /**
   * 如果分页需要从0开始，使用此方法构造分页对象
   */
  public static ofZeroIndexed(
    page: number,
    size: number,
    rowsPerPage: number,
    rowsNumber = -1
  ): Pagination {
    return Pagination.of(
      page > 0 ? page - 1 : page,
      0,
      rowsPerPage,
      rowsNumber
    );
  }

  public static buildZeroIndexedForRequest(
    pagination: Pagination,
    page: number,
    rowsPerPage: number
  ): void {
    pagination.page = page > 0 ? page - 1 : page;
    pagination.rowsPerPage = rowsPerPage < 1 ? this.PAGE_MAX_SIZE : rowsPerPage;
  }

  public static buildZeroIndexedForResponse(
    pagination: Pagination,
    response: AxiosResponse
  ) {
    pagination.page = response.data.data.number + 1;
    pagination.rowsNumber = response.data.data.totalElements;
  }
}

/**
 * 文件上传
 */
export interface UploadSharding {
  fileId?: number;
  fileName: string;
  businessType: string;
  shardIndex: number;
  shardSize: number;
  shardCount: number;
  fileKey: string;
  fileSize: number;
  shardContent: string;
}
