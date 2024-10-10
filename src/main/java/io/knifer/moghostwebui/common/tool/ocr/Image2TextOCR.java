package io.knifer.moghostwebui.common.tool.ocr;

/**
 * OCR
 *
 * @author Knifer
 */
public interface Image2TextOCR {

    /**
     * 图片转文字
     * @param imgBase64 图片的base64编码
     * @return 图片中的文字
     */
    String doOCR(String imgBase64);
}
