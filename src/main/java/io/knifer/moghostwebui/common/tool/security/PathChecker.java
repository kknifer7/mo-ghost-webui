package io.knifer.moghostwebui.common.tool.security;

/**
 * 目录安全检测器
 *
 * @author Knifer
 * @version 1.0.0
 */
public interface PathChecker {

    default void validate(String path) {
        validate(path, false);
    }

    void validate(String path, boolean allowBlank);

    boolean check(String path);

    boolean check(String path, boolean allowBlank);
}
