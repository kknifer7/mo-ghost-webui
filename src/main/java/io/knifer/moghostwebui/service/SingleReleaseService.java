package io.knifer.moghostwebui.service;

import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.entity.domain.MoFile;
import io.knifer.moghostwebui.common.entity.domain.SingleRelease;
import io.knifer.moghostwebui.common.entity.domain.key.ID;
import io.knifer.moghostwebui.common.entity.request.PageParams;
import io.knifer.moghostwebui.common.entity.request.SingleReleaseAddUpdateRequest;
import io.knifer.moghostwebui.common.entity.vo.SingleReleaseVO;
import io.knifer.moghostwebui.common.exception.MoException;
import io.knifer.moghostwebui.repository.MoFileRepository;
import io.knifer.moghostwebui.repository.SingleReleaseCDKASORepository;
import io.knifer.moghostwebui.repository.SingleReleaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 单发布服务
 *
 * @author Knifer
 * @version 1.0.0
 */
@Service
@AllArgsConstructor
public class SingleReleaseService {

    private final SingleReleaseRepository repository;
    private final SingleReleaseCDKASORepository cdkAsoRepository;
    private final MoFileRepository moFileRepository;

    /**
     * 新增单发布
     *
     * @param request 请求对象
     */
    public void add(SingleReleaseAddUpdateRequest request) {
        validateFileId(request);
        repository.save(SingleRelease.from(request));
    }

    /**
     * 更新单发布
     *
     * @param request 请求对象
     */
    public void update(SingleReleaseAddUpdateRequest request) {
        if (request.getId() == null) {
            MoException.throwOut(ErrorCodes.VALIDATION_FAILED, "SingleRelease id can't be null");
        }
        validateFileId(request);
        repository.findById(request.getId())
                .ifPresent(r -> {
                    SingleRelease.update(request, r);
                    repository.save(r);
                });
    }

    private void validateFileId(SingleReleaseAddUpdateRequest request){
        if (!moFileRepository.existsById(request.getFileId())) {
            MoException.throwOut(
                    ErrorCodes.VALIDATION_FAILED,
                    "Adding failed, SingleRelease fileId not exists."
            );
        }
    }

    /**
     * 根据ID删除
     *
     * @param id ID
     */
    @Transactional
    public void deleteById(Integer id) {
        if (repository.existsById(id)){
            repository.deleteById(id);
            cdkAsoRepository.deleteByIdSrId(id);
        }
    }

    public Page<SingleReleaseVO> listPage(PageParams pageParams) {
        Page<SingleRelease> poPage = repository.findAll(pageParams.toPageRequest());
        List<Integer> fileIds;
        Map<Integer, MoFile> fileIdMap;

        if (poPage.isEmpty()) {
            return Page.empty();
        }
        fileIds = poPage.getContent().stream().map(SingleRelease::getFileId).toList();
        fileIdMap = ID.mapIdsMap(moFileRepository.findAllById(fileIds));

        return poPage.map(p -> SingleReleaseVO.of(p, fileIdMap.get(p.getFileId())));
    }
}
