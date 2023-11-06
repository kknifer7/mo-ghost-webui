import { AxiosResponse } from 'axios';

export interface Menu {
  id: number;
  icon: string;
  label: string;
  separator?: boolean;
  children?: Menu[];
  path?: string;
}

export interface TableColumn {
  name: string;
  label: string;
  field: string;
  align: string;
}

export class Pagination {
  page: number;
  rowsPerPage: number;
  rowsNumber: number;

  static readonly FIRST_PAGE_NUMBER = 1;
  static readonly DEFAULT_ROWS_PER_PAGE = 10;
  static readonly PAGE_MAX_SIZE = 2000;

  constructor(page: number, rowsPerPage: number, rowsNumber: number) {
    this.page = page;
    this.rowsPerPage = rowsPerPage;
    this.rowsNumber = rowsNumber;
  }

  static readonly EMPTY = new Pagination(0, 0, 0);

  public reset(): void {
    this.page = Pagination.FIRST_PAGE_NUMBER;
    this.rowsPerPage = Pagination.DEFAULT_ROWS_PER_PAGE;
    this.rowsNumber = -1;
  }

  public static of(
    page: number,
    rowsPerPage: number,
    rowsNumber = -1
  ): Pagination {
    return new Pagination(
      page,
      rowsPerPage < 1 ? this.PAGE_MAX_SIZE : rowsPerPage,
      rowsNumber
    );
  }

  public static ofDefault(): Pagination {
    return Pagination.of(this.FIRST_PAGE_NUMBER, this.DEFAULT_ROWS_PER_PAGE);
  }

  /**
   * 如果分页需要从0开始，使用此方法构造分页对象
   */
  public static ofZeroIndexed(
    page: number,
    rowsPerPage: number,
    rowsNumber = -1
  ): Pagination {
    return Pagination.of(page > 0 ? page - 1 : page, rowsPerPage, rowsNumber);
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
