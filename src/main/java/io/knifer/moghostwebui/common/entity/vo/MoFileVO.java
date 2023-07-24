package io.knifer.moghostwebui.common.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.knifer.moghostwebui.common.entity.domain.Version;
import io.knifer.moghostwebui.common.entity.domain.key.ID;
import io.knifer.moghostwebui.common.entity.domain.MoFile;
import io.knifer.moghostwebui.common.util.FormattingUtil;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文件
 *
 * @author Knifer
 * @version 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MoFileVO implements ID<Integer> {

    private Integer id;

    private String originName;

    private String path;

    private String size;

    private String remark;

    private List<VersionVO> versions;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ctime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime mtime;

    public static MoFileVO from(MoFile po){
        MoFileVO result = new MoFileVO();

        result.setId(po.getId());
        result.setOriginName(po.getOriginName());
        result.setPath(po.getPath());
        result.setSize(FormattingUtil.sizeFormat(po.getSize()));
        result.setRemark(po.getRemark());
        result.setCtime(po.getCtime());
        result.setMtime(po.getMtime());
        result.setVersions(List.of());

        return result;
    }

    public static MoFileVO of(MoFile po, List<Version> versions){
        MoFileVO result = from(po);

        result.setVersions(versions.stream().map(VersionVO::from).toList());

        return result;
    }
}
