package io.knifer.moghostwebui.common.tool.security;

/**
 * 路径匹配器
 * @link <a href="https://blog.csdn.net/a303549861/article/details/82902234">来源</a>
 */
public interface PatternMatcher {
    boolean matches(String var1, String var2);
}