package io.knifer.moghostwebui.service;

import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.constant.SecurityConstants;
import io.knifer.moghostwebui.common.entity.request.OCRRequest;
import io.knifer.moghostwebui.common.exception.MoException;
import io.knifer.moghostwebui.common.tool.ocr.Image2TextOCR;
import io.knifer.moghostwebui.common.util.ServletUtil;
import io.knifer.moghostwebui.repository.SingleReleaseCDKRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 爬虫助手服务
 *
 * @author Knifer
 */
@Service
@RequiredArgsConstructor
public class SpiderAssistService {

    private final Image2TextOCR ocr;
    private final SingleReleaseCDKRepository singleReleaseCDKRepository;

    public String ocr(OCRRequest request) {
        validateMoGhostID();

        return ocr.doOCR(request.getImage());
    }

    @SuppressWarnings("ConstantConditions")
    private void validateMoGhostID() {
        String authWord = ServletUtil.getRequestHeader(SecurityConstants.REQ_HEADER_MO_GHOST_AUTH_ID);

        if (StringUtils.isBlank(authWord) || SecurityConstants.ANDROID_INVALID_MAC.equals(authWord)) {
            MoException.throwOut(ErrorCodes.FORBIDDEN, "auth word blank or invalid");
        }
        if (!singleReleaseCDKRepository.existsByAuthWord(authWord)) {
            MoException.throwOut(ErrorCodes.FORBIDDEN, "auth word invalid");
        }
    }
}
