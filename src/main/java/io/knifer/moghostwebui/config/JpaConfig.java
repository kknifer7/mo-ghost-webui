package io.knifer.moghostwebui.config;

import io.knifer.moghostwebui.common.constant.SettingConstants;
import io.knifer.moghostwebui.common.constant.UtilConstants;
import io.knifer.moghostwebui.repository.SettingEntryRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * - JPA设置 -
 *
 * @author Knifer
 * @version 1.0.0
 */
@Configuration
@AllArgsConstructor
public class JpaConfig implements AuditorAware<String> {

    private final SettingEntryRepository settingEntryRepository;

    @Override
    @NotNull
    public Optional<String> getCurrentAuditor() {
        return Optional.of(
                settingEntryRepository.findByKey(SettingConstants.SYS_SEC_USERNAME_ENTRY_KEY)
                        .orElseThrow(UtilConstants.NULL_OBJ_SUPPLIER)
                        .getValue()
        );
    }
}
