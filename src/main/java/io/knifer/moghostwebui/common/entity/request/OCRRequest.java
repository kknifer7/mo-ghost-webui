package io.knifer.moghostwebui.common.entity.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * OCR请求
 *
 * @author Knifer
 */
@Getter
@Setter
@ToString
public class OCRRequest {

    @NotBlank
    private String image;
}
