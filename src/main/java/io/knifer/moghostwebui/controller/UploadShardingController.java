package io.knifer.moghostwebui.controller;

import io.knifer.moghostwebui.common.entity.request.UploadShardingRequest;
import io.knifer.moghostwebui.common.entity.vo.ApiResult;
import io.knifer.moghostwebui.common.entity.vo.UploadShardingVO;
import io.knifer.moghostwebui.common.entity.vo.ValueVO;
import io.knifer.moghostwebui.service.UploadShardingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分片上传控制器
 *
 * @author Knifer
 * @version 1.0.0
 */
@RestController
@RequestMapping("/upload")
@AllArgsConstructor
public class UploadShardingController {

    private final UploadShardingService service;

    /**
     * 查询上传分片信息列表
     *
     * @return 上传分片信息列表
     */
    @GetMapping("/")
    public ApiResult<List<UploadShardingVO>> getAll() {
        return ApiResult.ok(service.findAll());
    }

    /**
     * 根据ID删除分片信息
     * @param id ID
     * @return void
     */
    @DeleteMapping("/{id}")
    public ApiResult<Void> deleteById(@PathVariable("id") Integer id) {
        service.removeById(id);

        return ApiResult.ok();
    }

    /**
     * 根据文件Key删除
     * @param fileKey 文件Key
     * @return void
     */
    @DeleteMapping("/file-key/{fileKey}")
    public ApiResult<Void> deleteByFileKey(@PathVariable("fileKey") String fileKey) {
        service.removeByFileKey(fileKey);

        return ApiResult.ok();
    }

    /**
     * 清理所有已完成的分片信息
     * @return void
     */
    @DeleteMapping("/all-done")
    public ApiResult<Void> deleteAllDone() {
        service.removeAllDone();

        return ApiResult.ok();
    }

    /**
     * 上传分片
     * @param request 请求对象
     * @return 更新后的分片信息
     */
    @PostMapping("/")
    public ApiResult<UploadShardingVO> upload(@Valid @RequestBody UploadShardingRequest request) {
        return ApiResult.ok(service.upload(request));
    }

    /**
     * 获取指定FileKey缺失的分片
     * @param fileKey 文件唯一标识
     * @return 缺失的分片号列表
     */
    @GetMapping("/missing-parts/{fileKey}")
    public ApiResult<ValueVO<List<Integer>>> getMissingParts(@PathVariable("fileKey") String fileKey){
        return ApiResult.ok(service.findMissingParts(fileKey));
    }

    /**
     * 根据ID获取文件KEY
     * @param id ID
     * @return 文件KEY
     */
    @GetMapping("/file-key/{id}")
    public ApiResult<ValueVO<String>> getFileKeyById(@PathVariable("id") Integer id) {
        return ApiResult.ok(service.findFileKeyById(id));
    }
}
