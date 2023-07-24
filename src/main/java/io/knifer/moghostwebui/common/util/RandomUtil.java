package io.knifer.moghostwebui.common.util;

import com.google.common.base.Strings;
import io.knifer.moghostwebui.common.constant.UtilConstants;
import jakarta.annotation.Nonnull;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ID工具类
 *
 * @author Knifer
 * @version 1.0.0
 */
public final class RandomUtil {

    private RandomUtil(){
        throw new AssertionError();
    }

    /**
     * 生成一个用于存储的唯一文件名
     * @param originName 原始文件名
     * @return 存储文件名
     */
    public static String nextStorageName(@Nonnull String originName){
        return Strings.nullToEmpty(originName) +
                "." +
                nextUUID() +
                LocalDateTime.now().format(UtilConstants.STORAGE_DATETIME_FORMATTER);
    }

    public static String nextUUID(){
        return nextUUID(false);
    }

    public static String nextUUID(boolean withHyphen){
        if (withHyphen){
            return UUID.randomUUID().toString();
        }else{
            return UUID.randomUUID().toString().replaceAll("-", "");
        }
    }
}
