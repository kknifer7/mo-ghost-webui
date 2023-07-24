package io.knifer.moghostwebui.common.tool.security.impl;

import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.exception.MoException;
import io.knifer.moghostwebui.common.tool.security.PathChecker;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 目录安全检测器
 *
 * @author Knifer
 * @version 1.0.0
 */
@Component
public class CommonPathChecker implements PathChecker {

    @Override
    public void validate(String path) {
        validate(path, false);
    }

    @Override
    public void validate(String path, boolean allowBlank) {
        if (!check(path, allowBlank)){
            MoException.throwOut(ErrorCodes.SECURITY_CHECK_FAILED, "File name check failed.");
        }
    }

    @Override
    public boolean check(String path) {
        return check(path, false);
    }

    @Override
    public boolean check(String path, boolean allowBlank) {
        return (allowBlank || StringUtils.isNotBlank(path)) &&
                !path.replace(File.separatorChar, '/').contains("./");
    }
}
