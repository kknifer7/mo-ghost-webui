package io.knifer.moghostwebui.common.tool.security.impl;

import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.constant.MoConstants;
import io.knifer.moghostwebui.common.constant.UtilConstants;
import io.knifer.moghostwebui.common.exception.MoException;
import io.knifer.moghostwebui.common.tool.security.IPLocator;
import io.knifer.moghostwebui.common.util.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

/**
 * IP定位器
 *
 * @author Knifer
 * @version 1.0.0
 */
@Slf4j
@Component
public class CommonIPLocator implements IPLocator {

    private byte[] cBuffer;

    @SuppressWarnings("all")
    public CommonIPLocator(){
        String dbPath = null;
        File tmpIpXDBFile = new File("ip2region.xdb");

        tmpIpXDBFile.delete();
        try (InputStream in = getClass().getResourceAsStream(MoConstants.IP_2_REGION_XDB_PATH)){
            // 这里在生产环境会读取不到文件，采取先读取数据流写入外部文件，再读取外部文件
            FileCopyUtils.copy(in.readAllBytes(), tmpIpXDBFile);
            dbPath = tmpIpXDBFile.getAbsolutePath();
            cBuffer = Searcher.loadContentFromFile(dbPath);
            log.info("IP locator initialized, XDB file path: '{}'", dbPath);
        } catch (IOException e) {
            MoException.throwOut(ErrorCodes.UNKNOWN, e);
        } finally {
            tmpIpXDBFile.delete();
        }
    }

    @Override
    public Pair<String, String> locate() {
        String remoteIP = ServletUtil.getRemoteIp();

        return Pair.of(remoteIP, locate(remoteIP));
    }

    @Override
    public String locate(String remoteIP) {
        String result = "";
        Searcher searcher;

        if (remoteIP == null){
            return result;
        }
        try {
            searcher = Searcher.newWithBuffer(cBuffer);
            result = searcher.search(remoteIP);
        } catch (Exception e) {
            MoException.throwOut(ErrorCodes.UNKNOWN, e);
        }

        return UtilConstants.PIPE_SPLITTER.splitToStream(result.replace("0", ""))
                .distinct()
                .collect(Collectors.joining("|"));
    }
}
