package io.knifer.moghostwebui.common.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.knifer.moghostwebui.common.constant.SingleReleaseStatus;
import io.knifer.moghostwebui.common.entity.domain.MoFile;
import io.knifer.moghostwebui.common.entity.domain.SingleRelease;
import io.knifer.moghostwebui.common.entity.domain.key.ID;
import io.knifer.moghostwebui.common.util.FormattingUtil;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 单发布
 *
 * @author Knifer
 * @version 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SingleReleaseVO implements ID<Integer> {

    /**
     * ID
     */
    private Integer id;

    /**
     * 名称
     */
    private String fullName;

    /**
     * 文件ID
     */
    private Integer fileId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件大小
     */
    private String fileSize;

    /**
     * 发布状态
     */
    private SingleReleaseStatus releaseStatus;

    /**
     * 总访问量
     */
    private String totalAccess;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ctime;

    /**
     * 修改日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime mtime;

    public static List<SingleReleaseVO> batchFrom(Collection<SingleRelease> poList){
        return poList.stream().map(SingleReleaseVO::from).toList();
    }

    public static SingleReleaseVO from(SingleRelease po){
        SingleReleaseVO result = new SingleReleaseVO();

        result.setId(po.getId());
        result.setFullName(po.getFullName());
        result.setFileId(po.getFileId());
        result.setFileName("");
        result.setFileSize("");
        result.setReleaseStatus(po.getReleaseStatus());
        result.setTotalAccess(String.valueOf(po.getTotalAccess()));
        result.setRemark(po.getRemark());
        result.setCtime(po.getCtime());
        result.setMtime(po.getMtime());

        return result;
    }

    public static SingleReleaseVO of(SingleRelease po, MoFile file){
        SingleReleaseVO result = from(po);

        result.setFileName(file.getOriginName());
        result.setFileSize(FormattingUtil.sizeFormat(file.getSize()));

        return result;
    }
}