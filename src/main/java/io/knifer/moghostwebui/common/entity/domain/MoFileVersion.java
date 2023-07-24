package io.knifer.moghostwebui.common.entity.domain;

import io.knifer.moghostwebui.common.entity.domain.key.MoFileVersionKey;
import io.knifer.moghostwebui.common.entity.domain.key.MultiID;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

/**
 * 文件-版本关联
 *
 * @author Knifer
 * @version 1.0.0
 */
@Table(name = "aso_mo_file_version")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MoFileVersion implements MultiID<Integer, Integer> {

    @EmbeddedId
    private MoFileVersionKey id;

    public static MoFileVersion of(Integer moFileId, Integer versionId){
        MoFileVersionKey key = new MoFileVersionKey();
        MoFileVersion result = new MoFileVersion();

        key.setMoFileId(moFileId);
        key.setVersionId(versionId);
        result.setId(key);

        return result;
    }

    public static List<MoFileVersion> batchOf(List<MoFile> files, Integer versionId){
        return files.stream()
                .map(f -> MoFileVersion.of(f.getId(), versionId))
                .toList();
    }
}
