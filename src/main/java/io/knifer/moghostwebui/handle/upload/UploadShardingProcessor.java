package io.knifer.moghostwebui.handle.upload;

import io.knifer.moghostwebui.common.base.Supportable;
import io.knifer.moghostwebui.common.entity.domain.UploadSharding;

/**
 * 分片处理器
 *
 * @author Knifer
 * @version 1.0.0
 */
public interface UploadShardingProcessor<T> extends Supportable<UploadSharding> {

    /**
     * 处理分片前的准备工作
     * @param data 某种数据
     */
    void prepare(T data);

    /**
     * 处理分片。必须调用，不调用可能导致内存泄漏
     *
     * @param uploadSharding 分片信息（未合并）
     */
    void process(UploadSharding uploadSharding);
}
