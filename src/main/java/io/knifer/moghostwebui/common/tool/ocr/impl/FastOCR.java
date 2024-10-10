package io.knifer.moghostwebui.common.tool.ocr.impl;

import io.github.mymonstercat.Model;
import io.github.mymonstercat.ocr.InferenceEngine;
import io.github.mymonstercat.ocr.config.ParamConfig;
import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.constant.MoConstants;
import io.knifer.moghostwebui.common.exception.MoException;
import io.knifer.moghostwebui.common.tool.ocr.Image2TextOCR;
import io.knifer.moghostwebui.common.util.CodecUtil;
import io.knifer.moghostwebui.common.util.RandomUtil;
import io.knifer.moghostwebui.config.properties.MoGhostProperties;
import org.apache.commons.io.FileUtils;
import org.apache.tika.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * 快速OCR实现类
 * <a href="https://github.com/MyMonsterCat/RapidOcr-Java">参考</a>
 * @author Knifer
 */
@Component
public class FastOCR implements Image2TextOCR {

    private final InferenceEngine engine;
    private final ParamConfig paramConfig;
    private final Path tmpImgPath;

    public FastOCR(@Autowired MoGhostProperties prop) {
        engine = InferenceEngine.getInstance(Model.ONNX_PPOCR_V4);
        paramConfig = ParamConfig.getDefaultConfig();
        tmpImgPath = Path.of(prop.getStorage().getTmpPath());
        if (Files.notExists(tmpImgPath)) {
            try {
                Files.createDirectory(tmpImgPath);
            } catch (IOException e) {
                MoException.throwOut(ErrorCodes.UNKNOWN, e);
            }
        }
    }

    @Override
    public String doOCR(String imgBase64) {
        Path imgPath = Path.of(
                tmpImgPath.toString(),
                MoConstants.DE_CAPTCHA_TMP_FILE_NAME_PREFIX + RandomUtil.nextStorageName("img")
        );
        String result = StringUtils.EMPTY;

        try {
            if (Files.notExists(imgPath)) {
                Files.createFile(imgPath);
            }
            Files.write(
                    imgPath,
                    CodecUtil.base64(imgBase64),
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
            result = engine.runOcr(imgPath.toString(), paramConfig).getStrRes().trim();
        } catch (Exception e) {
            MoException.throwOut(ErrorCodes.UNKNOWN, e);
        } finally {
            FileUtils.deleteQuietly(imgPath.toFile());
        }

        return result;
    }
}
