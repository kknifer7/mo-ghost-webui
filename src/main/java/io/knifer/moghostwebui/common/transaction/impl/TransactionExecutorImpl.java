package io.knifer.moghostwebui.common.transaction.impl;

import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.exception.MoException;
import io.knifer.moghostwebui.common.transaction.TransactionExecutor;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.concurrent.Callable;

/**
 * - 事务服务 -
 *
 * @author Knifer
 * @version 1.0.0
 */
@Service
public class TransactionExecutorImpl implements TransactionExecutor {

    @Resource
    private PlatformTransactionManager transactionManager;

    private final ThreadLocal<TransactionStatus> threadLocal = new ThreadLocal<>();

    @Override
    public void begin() {
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        threadLocal.set(transactionManager.getTransaction(transactionDefinition));
    }

    @Override
    public void commit() {
        transactionManager.commit(threadLocal.get());
        threadLocal.remove();
    }

    @Override
    public void rollback() {
        transactionManager.rollback(threadLocal.get());
        threadLocal.remove();
    }

    @Override
    public void execute(Runnable runnable) {
        try {
            begin();
            runnable.run();
            commit();
        } catch (Exception e) {
            rollback();
            throw new MoException(ErrorCodes.UNKNOWN, e);
        }
    }

    @Override
    public <V> V execute(Callable<V> callable) {
        try {
            begin();
            V result = callable.call();
            commit();
            return result;
        } catch (Exception e) {
            rollback();
            throw new MoException(ErrorCodes.UNKNOWN, e);
        }
    }
}
