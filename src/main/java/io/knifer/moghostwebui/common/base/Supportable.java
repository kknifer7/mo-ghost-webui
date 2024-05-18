package io.knifer.moghostwebui.common.base;

/**
 * 可支持的（用于策略模式）
 * @author Knifer
 * @version 1.0.0
 */
public interface Supportable<T> {

    boolean support(T obj);
}
