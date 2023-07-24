package io.knifer.moghostwebui.controller;

import io.knifer.moghostwebui.common.entity.request.PageParams;
import io.knifer.moghostwebui.common.entity.vo.ApiResult;
import io.knifer.moghostwebui.common.entity.vo.SingleReleaseVO;
import io.knifer.moghostwebui.service.SingleReleaseCDKASOService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 单发布-单发布CDK 关联
 *
 * @author Knifer
 * @version 1.0.0
 */
@RestController
@RequestMapping("/single-release-cdk-aso")
@AllArgsConstructor
public class SingleReleaseCDKASOController {

    private final SingleReleaseCDKASOService service;

}
