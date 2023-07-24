package io.knifer.moghostwebui.service;

import io.knifer.moghostwebui.common.entity.domain.MoFile;
import io.knifer.moghostwebui.repository.MoFileRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 版本服务测试
 *
 * @author Knifer
 * @version 1.0.0
 */
@SpringBootTest
public class VersionServiceTest {

    @Resource
    MoFileRepository moFileRepository;

    @Test
    void test(){
        List<MoFile> files = moFileRepository.findByVersionId(1);

        Assertions.assertFalse(files.isEmpty());
    }
}
