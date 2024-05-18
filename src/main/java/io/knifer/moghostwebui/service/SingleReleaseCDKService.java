package io.knifer.moghostwebui.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.entity.domain.SingleRelease;
import io.knifer.moghostwebui.common.entity.domain.SingleReleaseCDK;
import io.knifer.moghostwebui.common.entity.domain.SingleReleaseCDKASO;
import io.knifer.moghostwebui.common.entity.domain.key.ID;
import io.knifer.moghostwebui.common.entity.request.PageParams;
import io.knifer.moghostwebui.common.entity.request.SingleReleaseCDKAddUpdateRequest;
import io.knifer.moghostwebui.common.entity.request.SingleReleaseCDKQueryRequest;
import io.knifer.moghostwebui.common.entity.vo.SingleReleaseCDKVO;
import io.knifer.moghostwebui.common.exception.MoException;
import io.knifer.moghostwebui.repository.SingleReleaseCDKASORepository;
import io.knifer.moghostwebui.repository.SingleReleaseCDKRepository;
import io.knifer.moghostwebui.repository.SingleReleaseRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 单发布CDK服务
 *
 * @author Knifer
 * @version 1.0.0
 */
@Service
@AllArgsConstructor
public class SingleReleaseCDKService {

    private final SingleReleaseCDKRepository repository;
    private final SingleReleaseRepository releaseRepository;
    private final SingleReleaseCDKASORepository cdkAsoRepository;

    /**
     * 新增
     * @param request 请求参数
     */
    @Transactional
    public void add(SingleReleaseCDKAddUpdateRequest request){
        SingleReleaseCDK po;

        validateRequest(request);
        po = repository.save(SingleReleaseCDK.from(request));
        cdkAsoRepository.saveAll(SingleReleaseCDKASO.batchOf(request.getSrIds(), po.getId()));
    }

    /**
     * 根据ID删除
     * @param id ID
     */
    @Transactional
    public void deleteById(Integer id){
        if (repository.existsById(id)){
            repository.deleteById(id);
            cdkAsoRepository.deleteByIdCdkId(id);
        }
    }

    /**
     * 更新
     * @param request 请求参数
     */
    @Transactional
    public void update(SingleReleaseCDKAddUpdateRequest request){
        Integer cdkId = request.getId();

        if (cdkId == null){
            return;
        }
        validateRequest(request);
        repository.findById(cdkId).ifPresent(cdk -> {
            SingleReleaseCDK.update(request, cdk);
            repository.save(cdk);
            cdkAsoRepository.deleteByIdCdkId(cdkId);
            cdkAsoRepository.saveAll(SingleReleaseCDKASO.batchOf(request.getSrIds(), cdkId));
        });
    }

    /**
     * 验证发布对象ID存在
     * @param request 请求参数
     */
    private void validateRequest(SingleReleaseCDKAddUpdateRequest request){
        List<Integer> srIds = request.getSrIds();
        Integer id = request.getId();
        String code;

        if (!srIds.isEmpty() && releaseRepository.countByIdIn(request.getSrIds()) != srIds.size()){
            MoException.throwOut(ErrorCodes.VALIDATION_FAILED, "SingleReleaseCDK add failed, srId not exists");
        }
        if (id != null){
            code = request.getCode();
            if (StringUtils.isBlank(code)){
                MoException.throwOut(
                        ErrorCodes.VALIDATION_FAILED,
                        "SingleReleaseCDK update failed, code can't be blank"
                );
            }
            if (repository.existsByCodeAndIdNot(code, id)){
                MoException.throwOut(
                        ErrorCodes.SINGLE_RELEASE_CDK_CODE_DUPLICATED,
                        "SingleReleaseCDK update failed, code '" + code + "' duplicated"
                );
            }
        }
    }

    /**
     * 分页查询
     * @param pageParams 分页参数
     * @param request 查询参数
     * @return data
     */
    public Page<SingleReleaseCDKVO> listPage(PageParams pageParams, SingleReleaseCDKQueryRequest request){
        Page<SingleReleaseCDK> poPage = repository.findAll(request.toSpecification(), pageParams.toPageRequest());
        Collection<Integer> ids;
        List<Pair<Integer, SingleRelease>> releases;
        Multimap<Integer, SingleRelease> cdkIdAndReleaseMap;

        if (poPage.isEmpty()){
            return Page.empty();
        }
        ids = ID.mapIds(poPage.getContent());
        releases = cdkAsoRepository.findCdkIdAndReleaseByIdCdkIds(ids);
        if (releases.isEmpty()){
            return poPage.map(SingleReleaseCDKVO::from);
        }
        cdkIdAndReleaseMap = HashMultimap.create(ids.size(), releases.size());
        releases.forEach(pair -> cdkIdAndReleaseMap.put(pair.getFirst(), pair.getSecond()));

        return poPage.map(cdk -> SingleReleaseCDKVO.of(cdk, cdkIdAndReleaseMap.get(cdk.getId())));
    }

    /**
     * 根据cdkId删除权限标识（解绑设备）
     * @param cdkId cdkId
     */
    public void deleteAuthWordById(Integer cdkId){
        repository.findById(cdkId)
                .ifPresent(cdk -> {
                    cdk.setAuthWord(null);
                    repository.save(cdk);
                });
    }
}
