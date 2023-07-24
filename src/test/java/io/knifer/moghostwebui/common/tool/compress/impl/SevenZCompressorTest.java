package io.knifer.moghostwebui.common.tool.compress.impl;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 7z压缩测试
 *
 * @author Knifer
 * @version 1.0.0
 */
@SpringBootTest
public class SevenZCompressorTest {

    @Resource
    SevenZCompressor compressor;

    @Test
    void test(){
        compressor.compress("E:\\录制\\素材", "E:\\录制\\test.7z");
    }
}
