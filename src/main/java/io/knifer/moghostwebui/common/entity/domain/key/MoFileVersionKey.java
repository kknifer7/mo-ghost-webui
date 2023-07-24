package io.knifer.moghostwebui.common.entity.domain.key;

import jakarta.persistence.Column;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 文件版本联合ID
 *
 * @author Knifer
 * @version 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class MoFileVersionKey extends ManyToManyKey<Integer, Integer> {

    @Column(nullable = false)
    private Integer moFileId;

    @Column(nullable = false)
    private Integer versionId;

    @Override
    protected Integer getK1() {
        return moFileId;
    }

    @Override
    protected Integer getK2() {
        return versionId;
    }
}
