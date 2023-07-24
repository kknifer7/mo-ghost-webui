package io.knifer.moghostwebui.common.entity.request;

import io.knifer.moghostwebui.common.constant.PeriodQueryStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 单发布CDK查询
 *
 * @author Knifer
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class SingleReleaseCDKQueryRequest {

    private PeriodQueryStatus tab;
}
