package io.knifer.moghostwebui.common.tool.security.impl;

import io.knifer.moghostwebui.common.tool.security.PatternMatcher;

/**
 * 路径匹配器
 * @link <a href="https://blog.csdn.net/a303549861/article/details/82902234">来源</a>
 */
public class CommonPathMatcher implements PatternMatcher {
    private static final CommonPathMatcher INSTANCE = new CommonPathMatcher();

    public CommonPathMatcher() {
    }

    public static CommonPathMatcher getInstance() {
        return INSTANCE;
    }

    public boolean matches(String pattern, String source) {
        if (pattern != null && source != null) {
            pattern = pattern.trim();
            source = source.trim();
            int start;
            if (pattern.endsWith("*")) {
                start = pattern.length() - 1;
                if (source.length() >= start && pattern.substring(0, start).equals(source.substring(0, start))) {
                    return true;
                }
            } else if (pattern.startsWith("*")) {
                start = pattern.length() - 1;
                if (source.length() >= start && source.endsWith(pattern.substring(1))) {
                    return true;
                }
            } else if (pattern.contains("*")) {
                start = pattern.indexOf("*");
                int end = pattern.lastIndexOf("*");
                if (source.startsWith(pattern.substring(0, start)) && source.endsWith(pattern.substring(end + 1))) {
                    return true;
                }
            } else if (pattern.equals(source)) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }
}
