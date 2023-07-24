package io.knifer.moghostwebui.common.transaction;

import java.util.concurrent.Callable;

/**
 * - 事务服务 -
 *
 * @author Knifer
 * @version 1.0.0
 */
public interface TransactionExecutor {

    void begin();

    void commit();

    void rollback();

    /**
     * 在事务中执行回调函数
     *
     * @param callable 回调函数
     */
    <V> V execute(Callable<V> callable);

}

