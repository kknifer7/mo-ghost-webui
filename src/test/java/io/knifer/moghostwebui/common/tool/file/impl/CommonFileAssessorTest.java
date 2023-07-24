package io.knifer.moghostwebui.common.tool.file.impl;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 文件访问器测试
 *
 * @author Knifer
 * @version 1.0.0
 */
@SpringBootTest
public class CommonFileAssessorTest {

    @Resource
    CommonFileAssessor assessor;

    @Test
    void test(){
        System.out.println(assessor.getFileList("C:\\Users\\31564\\Desktop"));
    }
}
