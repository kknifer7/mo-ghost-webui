package io.knifer.moghostwebui.handle.security.impl;

import io.knifer.moghostwebui.common.constant.SettingConstants;
import io.knifer.moghostwebui.common.constant.UtilConstants;
import io.knifer.moghostwebui.common.entity.domain.SettingEntry;
import io.knifer.moghostwebui.handle.security.Verifier;
import io.knifer.moghostwebui.repository.SettingEntryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 访问Key验证器
 *
 * @author Knifer
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
public class AccessKeyVerifier implements Verifier<String> {

    private final SettingEntryRepository settingEntryRepository;

    @Override
    public boolean verify(String accessKey) {
        SettingEntry setting;
        String trueKey;

        if (StringUtils.isBlank(accessKey)) {
            return false;
        }
        setting = settingEntryRepository.findByKey(SettingConstants.SYS_SEC_ACCESS_KEY_ENTRY_KEY)
                .orElseThrow(UtilConstants.NULL_OBJ_SUPPLIER);
        trueKey = setting.getValue();

        return !StringUtils.EMPTY.equals(trueKey) && Objects.equals(accessKey, trueKey);
    }
}
