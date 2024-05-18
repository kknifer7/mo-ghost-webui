package io.knifer.moghostwebui.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.exception.MoException;

/**
 * JSON工具类
 *
 * @author Knifer
 * @version 1.0.0
 */
public final class JSONUtil {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private JSONUtil() {
        throw new AssertionError();
    }

    /**
     * 将对象转为JSON字符串
     * @param obj 对象
     * @return JSON字符串
     */
    public static <T> String toJSON(T obj) {
        String result = null;

        try {
            result = obj == null ? "" : objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            MoException.throwOut(ErrorCodes.UNKNOWN, e);
        }

        return result;
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        T result = null;

        if (json == null) {
            return result;
        }
        try {
            result = objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            MoException.throwOut(ErrorCodes.UNKNOWN, e);
        }

        return result;
    }
}
