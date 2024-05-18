package io.knifer.moghostwebui.handle.security;

/**
 * 验证器
 *
 * @author Knifer
 * @version 1.0.0
 */
public interface Verifier<T> {

    boolean verify(T String);
}
