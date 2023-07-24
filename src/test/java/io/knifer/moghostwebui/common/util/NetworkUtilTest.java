package io.knifer.moghostwebui.common.util;

import io.knifer.moghostwebui.common.tool.security.IPLocator;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * IP地址工具类测试
 *
 * @author Knifer
 * @version 1.0.0
 */
@SpringBootTest
public class NetworkUtilTest {

    @Resource
    IPLocator locator;

    @Test
    void quickStartTest() throws IOException {
        // 1、创建 searcher 对象
        String dbPath = new ClassPathResource("network/ip2region.xdb").getURI().getPath();
        Searcher searcher;
        try {
            searcher = Searcher.newWithFileOnly(dbPath);
        } catch (IOException e) {
            System.out.printf("failed to create searcher with `%s`: %s\n", dbPath, e);
            return;
        }

        // 2、查询
        String ip = null;
        try {
            ip = "250.1.2.255";
            long sTime = System.nanoTime();
            String region = searcher.search(ip);
            long cost = TimeUnit.NANOSECONDS.toMicros((long) (System.nanoTime() - sTime));
            System.out.printf("{region: %s, ioCount: %d, took: %d μs}\n", region, searcher.getIOCount(), cost);
        } catch (Exception e) {
            System.out.printf("failed to search(%s): %s\n", ip, e);
        }

        // 3、关闭资源
        searcher.close();

        // 备注：并发使用，每个线程需要创建一个独立的 searcher 对象单独使用。
    }

    @Test
    void test(){
        System.out.println(locator.locate("218.62.100.7"));
    }
}
