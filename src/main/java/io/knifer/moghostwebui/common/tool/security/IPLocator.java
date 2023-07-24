package io.knifer.moghostwebui.common.tool.security;

import org.springframework.data.util.Pair;

/**
 * IP定位
 *
 * @author Knifer
 * @version 1.0.0
 */
public interface IPLocator {

    /**
     * 查找远程IP所在地（取HttpServletRequest中的远程IP）
     * @return pair (IP地址, 所在区域名称) eg. (x.x.x.x, 中国|A省|B市|移动)
     */
    Pair<String, String> locate();

    /**
     * 查找远程IP所在地
     * @return regionName 所在区域名称 eg. 中国|A省|B市|移动
     */
    String locate(String remoteIP);
}
