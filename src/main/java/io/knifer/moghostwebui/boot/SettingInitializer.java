package io.knifer.moghostwebui.boot;

import io.knifer.moghostwebui.common.constant.DataType;
import io.knifer.moghostwebui.common.constant.SecurityConstants;
import io.knifer.moghostwebui.common.constant.SettingConstants;
import io.knifer.moghostwebui.common.entity.domain.SettingEntry;
import io.knifer.moghostwebui.common.entity.domain.SettingTag;
import io.knifer.moghostwebui.common.transaction.TransactionExecutor;
import io.knifer.moghostwebui.repository.SettingEntryRepository;
import io.knifer.moghostwebui.repository.SettingTagRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * - 初始化器 -
 *
 * @author Knifer
 * @version 1.0.0
 */
@Component
@Slf4j
@AllArgsConstructor
public class SettingInitializer implements InitializingBean {

    private final SettingTagRepository settingTagRepository;

    private final SettingEntryRepository settingEntryRepository;

    private final TransactionExecutor transactionExecutor;

    @Override
    public void afterPropertiesSet() {
        transactionExecutor.begin();
        try {
            settingTagRepository.findByName(SettingConstants.SYS_INIT_TAG_NAME)
                    .ifPresentOrElse(settingTag -> {
                        List<SettingEntry> entries =
                                settingEntryRepository.findByTagName(SettingConstants.SYS_INIT_TAG_NAME);

                        if (entries.isEmpty()){
                            log.warn("System initialize entry not found, we're going to initialize......");
                            doInit();
                        }else if (!"1".equals(entries.get(0).getValue())){
                            log.info("System initialize entry value not equals '1', we're going to initialize......");
                            deleteExistsSysEntry();
                            doInit();
                        }
                    }, () -> {
                        log.info("System is not initialized, we're going to do it......");
                        settingTagRepository.save(SettingTag.of(SettingConstants.SYS_INIT_TAG_NAME));
                        doInit();
                    });
        }catch (Exception e){
            transactionExecutor.rollback();

            return;
        }
        transactionExecutor.commit();
    }

    private void deleteExistsSysEntry(){
        settingEntryRepository.deleteByKey(SettingConstants.SYS_INIT_ENTRY_KEY);
        settingEntryRepository.deleteByKey(SettingConstants.SYS_SEC_USERNAME_ENTRY_KEY);
        settingEntryRepository.deleteByKey(SettingConstants.SYS_SEC_PASSWORD_ENTRY_KEY);
        settingTagRepository.deleteByName(SettingConstants.SYS_SEC_TAG_NAME);
    }

    private void doInit(){
        // TODO 初始化配置项
        settingTagRepository.save(SettingTag.of(SettingConstants.SYS_SEC_TAG_NAME));
        settingEntryRepository.save(SettingEntry.of(
                SettingConstants.SYS_INIT_ENTRY_KEY,
                DataType.INTEGER,
                "1",
                SettingConstants.SYS_INIT_TAG_NAME
        ));
        settingEntryRepository.save(SettingEntry.of(
                SettingConstants.SYS_SEC_USERNAME_ENTRY_KEY,
                DataType.STRING,
                SecurityConstants.DEFAULT_USERNAME,
                SettingConstants.SYS_SEC_TAG_NAME
        ));
        settingEntryRepository.save(SettingEntry.of(
                SettingConstants.SYS_SEC_PASSWORD_ENTRY_KEY,
                DataType.STRING,
                SecurityConstants.DEFAULT_PASSWORD,
                SettingConstants.SYS_SEC_TAG_NAME
        ));
        settingEntryRepository.save(SettingEntry.of(
                SettingConstants.SYS_SEC_ACCESS_KEY_ENTRY_KEY,
                DataType.STRING,
                StringUtils.EMPTY,
                SettingConstants.SYS_SEC_TAG_NAME
        ));
        log.info("System initialized.");
    }
}
