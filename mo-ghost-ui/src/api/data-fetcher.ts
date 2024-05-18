import { AxiosResponse } from 'axios';
import { Notify } from 'quasar';
import { Pagination } from '@base/models';
import { ref, Ref } from 'vue';

/**
 * 远程数据获取器接口
 */
interface DataFetcher {
  /**
   * 请求远程数据（通常用于表格组件请求时的回调）
   * @param props 远程数据接口调用时传入的参数
   * @param useLoading 是否使用加载组件
   */
  onRequest(props: unknown, useLoading: boolean): Promise<boolean>;
  /**
   * 数据变化时做出的行为（通常用于表格组件更新数据后的回调）
   */
  onDataChanged(useLoading: boolean): Promise<void>;
  /**
   * 刷新数据
   */
  refresh(useLoading: boolean): Promise<boolean>;
  /**
   * 数据更新行为
   * @param updateSupplier 更新数据的远程调用操作
   * @param data 远程接口要传入的参数
   */
  dataUpdate<P>(
    updateSupplier: (data: P) => Promise<AxiosResponse>,
    data: P
  ): Promise<boolean>;
}

abstract class BaseDataFetcher<T> implements DataFetcher {
  /**
   * 加载标识：用于标识当前是否正在进行数据加载
   */
  protected loading: Ref<boolean>;

  /**
   * 存放数据的ref引用
   */
  protected data: Ref<Array<T>>;

  constructor(loading: Ref<boolean>, data: Ref<Array<T>>) {
    this.loading = loading;
    this.data = data;
  }

  public abstract onRequest(
    props: unknown,
    useLoading: boolean | undefined
  ): Promise<boolean>;

  public async onDataChanged(useLoading?: boolean): Promise<void> {
    await this.refresh(useLoading);
  }

  public async refresh(
    useLoading?: boolean,
    filter?: unknown
  ): Promise<boolean> {
    return this.onRequest(
      {
        filter,
      },
      useLoading
    );
  }

  public async dataUpdate<P>(
    updateSupplier: (data: P) => Promise<AxiosResponse>,
    data: P,
    forceRefresh = true,
    filter?: unknown
  ): Promise<boolean> {
    this.loading.value = true;
    const response = await updateSupplier(data);
    this.loading.value = false;
    if (
      response.data &&
      response.data.code === 200 &&
      (await this.refresh(true, filter))
    ) {
      Notify.create({
        type: 'positive',
        position: 'bottom-right',
        timeout: 2000,
        message: '操作成功',
      });

      return true;
    } else {
      if (forceRefresh) {
        this.refresh(true, filter);
      }

      return false;
    }
  }
}

class FullDataFetcher<T> extends BaseDataFetcher<T> {
  private dataSupplier: (filter: unknown) => Promise<AxiosResponse>;

  constructor(
    dataSupplier: (filter: unknown) => Promise<AxiosResponse>,
    loading: Ref<boolean>,
    data = ref()
  ) {
    super(loading, data);
    this.dataSupplier = dataSupplier;
  }

  public static of<T>(
    dataSupplier: (filter: unknown) => Promise<AxiosResponse>,
    loading: Ref<boolean>,
    data = ref<Array<T>>()
  ) {
    return new FullDataFetcher(dataSupplier, loading, data);
  }

  public override async onRequest(
    filter: unknown,
    useLoading = true
  ): Promise<boolean> {
    if (useLoading) {
      this.loading.value = true;
    }

    return await this.fetchData(filter);
  }

  private async fetchData(filter: unknown) {
    const response = await this.dataSupplier(filter);

    if (response.data.code === 200) {
      this.data.value = response.data.data;
      this.loading.value = false;

      return true;
    }

    return false;
  }
}

class PagedDataFetcher<T> extends BaseDataFetcher<T> {
  private pagination: Ref<Pagination>;

  private dataSupplier: (
    page: number,
    rowsPerPage: number,
    filter: unknown
  ) => Promise<AxiosResponse>;

  constructor(
    dataSupplier: (
      page: number,
      rowsPerPage: number,
      filter: unknown
    ) => Promise<AxiosResponse>,
    pagination: Ref<Pagination>,
    loading: Ref<boolean>,
    data = ref()
  ) {
    super(loading, data);
    this.pagination = pagination;
    this.dataSupplier = dataSupplier;
  }

  public static of = <T>(
    dataSupplier: (
      page: number,
      rowsPerPage: number,
      filter: unknown
    ) => Promise<AxiosResponse>,
    pagination: Ref<Pagination>,
    loading: Ref<boolean>,
    data = ref<Array<T>>()
  ): PagedDataFetcher<T> => {
    return new PagedDataFetcher(dataSupplier, pagination, loading, data);
  };

  public override async onRequest(
    props: {
      pagination: Pagination;
      filter?: unknown;
    },
    useLoading = true
  ) {
    if (useLoading) {
      this.loading.value = true;
    }

    const { page, rowsPerPage } = props.pagination;

    Pagination.buildZeroIndexedForRequest(
      this.pagination.value,
      page,
      rowsPerPage
    );
    return await this.fetchData(props.filter);
  }

  private fetchData = async (filter: unknown): Promise<boolean> => {
    const response = await this.dataSupplier(
      this.pagination.value.page,
      this.pagination.value.rowsPerPage,
      filter
    );

    if (response.data.code === 200) {
      this.data.value = response.data.data.content;
      this.pagination.value.size = this.data.value.length;
      Pagination.buildZeroIndexedForResponse(this.pagination.value, response);
      this.loading.value = false;

      return true;
    }

    return false;
  };

  public override async refresh(
    useLoading = true,
    filter?: unknown
  ): Promise<boolean> {
    if (this.pagination.value.size <= 1) {
      this.pagination.value.reset();
    }

    return this.onRequest(
      {
        pagination: this.pagination.value,
        filter,
      },
      useLoading
    );
  }
}

export { type DataFetcher, FullDataFetcher, PagedDataFetcher };
