package io.knifer.moghostwebui.common.util;

import org.junit.jupiter.api.Test;

/**
 * 随机工具类测试
 *
 * @author Knifer
 * @version 1.0.0
 */
public class RandomUtilTest {

    @Test
    void test(){
        System.out.println(RandomUtil.nextUUID(true).length());
    }
}
