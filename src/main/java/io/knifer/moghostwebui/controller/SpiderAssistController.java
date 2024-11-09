package io.knifer.moghostwebui.controller;

import io.knifer.moghostwebui.common.entity.request.OCRRequest;
import io.knifer.moghostwebui.common.entity.vo.ApiResult;
import io.knifer.moghostwebui.common.entity.vo.ValueVO;
import io.knifer.moghostwebui.service.SpiderAssistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 爬虫辅助
 *
 * @author Knifer
 */
@RestController
@RequestMapping("/spider-assist")
@RequiredArgsConstructor
public class SpiderAssistController {

    private final SpiderAssistService service;

    /**
     * OCR
     * @param request 参数
     * @return 图片中的文字
     */
    @PostMapping("/ocr")
    public ApiResult<ValueVO<String>> ocr(@Valid @RequestBody OCRRequest request) {
        return ApiResult.ok(ValueVO.from(service.ocr(request)));
    }
}
