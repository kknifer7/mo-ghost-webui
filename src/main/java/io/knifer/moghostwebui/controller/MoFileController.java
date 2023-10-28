package io.knifer.moghostwebui.controller;

import io.knifer.moghostwebui.common.entity.request.MoFileQueryRequest;
import io.knifer.moghostwebui.common.entity.request.PageParams;
import io.knifer.moghostwebui.common.entity.vo.ApiResult;
import io.knifer.moghostwebui.common.entity.vo.MoFileVO;
import io.knifer.moghostwebui.service.MoFileService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 文件
 *
 * @author Knifer
 * @version 1.0.0
 */
@RequestMapping("/mo-file")
@RestController
@AllArgsConstructor
public class MoFileController {

    private final MoFileService service;

    /**
     * 分页查询
     * @param pageParams 分页参数
     * @param request 请求参数
     * @return 分页数据
     */
    @GetMapping("/")
    public ApiResult<Page<MoFileVO>> getPage(
            PageParams pageParams,
            @Valid MoFileQueryRequest request
    ){
        return ApiResult.ok(service.listPage(pageParams, request));
    }

    /**
     * 分页查询独立文件
     * @param pageParams 分页参数
     * @return data
     */
    @GetMapping("/single")
    public ApiResult<Page<MoFileVO>> getSingle(PageParams pageParams){
        return ApiResult.ok(service.listPageSingle(pageParams));
    }

    /**
     * 根据版本ID查询
     * @param versionId 版本ID
     * @return 文件列表
     */
    @GetMapping("/by-version-id/{versionId}")
    public ApiResult<List<MoFileVO>> getByVersionId(@PathVariable("versionId") Integer versionId){
        return ApiResult.ok(service.listMoFileByVersionId(versionId));
    }

    /**
     * 向指定的版本批量新增文件
     * @param files 上传文件列表
     * @param versionId 版本ID（可为空）
     * @return void
     */
    @PostMapping({ "/{versionId}", "/" })
    public ApiResult<Void> addFiles(MultipartFile[] files, @Nullable @PathVariable("versionId") Integer versionId){
        service.add(files, versionId);

        return ApiResult.ok();
    }

    /**
     * 在指定版本批量添加文件
     * @param fileIds 文件ID列表
     * @param versionId 版本ID
     * @return void
     */
    @PostMapping("/to/{versionId}")
    public ApiResult<Void> bindFilesToVersion(
            @RequestBody List<Integer> fileIds,
            @PathVariable("versionId") Integer versionId
    ){
        service.addMoFilesToVersion(fileIds, versionId);

        return ApiResult.ok();
    }

    /**
     * 根据ID删除文件
     * @param id 文件ID
     * @return void
     */
    @DeleteMapping("/{id}")
    public ApiResult<Void> deleteById(@PathVariable("id") Integer id){
        service.deleteMoFileById(id);

        return ApiResult.ok();
    }

    /**
     * 根据ID列表批量删除文件
     * @param ids 文件ID列表
     * @return void
     */
    @DeleteMapping("/")
    public ApiResult<Void> deleteByIds(@RequestBody List<Integer> ids){
        service.deleteMoFileByIds(ids);

        return ApiResult.ok();
    }

    /**
     * 根据文件ID下载
     * @param id ID
     */
    @GetMapping("/{id}")
    public void downloadById(@PathVariable("id") Integer id){
        service.downloadById(id);
    }

    /**
     * 根据文件ID更名
     * @param id ID
     * @param newName 新名称
     * @return void
     */
    @PatchMapping("/name/{id}/{newName}")
    public ApiResult<Void> rename(
            @PathVariable("id") Integer id,
            @PathVariable("newName") String newName
    ){
        service.renameById(id, newName);

        return ApiResult.ok();
    }

    /**
     * 根据ID更新备注
     * @param id ID
     * @param newRemark 新备注
     * @return void
     */
    @PatchMapping("/remark/{id}/{newRemark}")
    public ApiResult<Void> updateRemark(
            @PathVariable("id") Integer id,
            @PathVariable("newRemark") String newRemark
    ){
        service.updateRemarkById(id, newRemark);

        return ApiResult.ok();
    }

    /**
     * 根据单发布 CDK code 下载
     * @param code 单发布CDK的code
     */
    @GetMapping("/single-release/{code}/{releaseId}")
    public void downloadBySingleReleaseCDKCodeAndReleaseId(
            @PathVariable("code") String code,
            @PathVariable("releaseId") Integer releaseId
    ){
        service.downloadBySingleReleaseCDKCodeAndReleaseId(code, releaseId);
    }

    /**
     * 替换文件
     * @param file 上传的文件
     * @param id 要替换的文件信息ID
     * @return void
     */
    @PutMapping("/{id}")
    public ApiResult<Void> replace(MultipartFile file, @PathVariable("id") Integer id){
        service.replace(file, id);

        return ApiResult.ok();
    }
}
