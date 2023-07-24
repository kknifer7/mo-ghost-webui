package io.knifer.moghostwebui.service;

import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.entity.domain.Project;
import io.knifer.moghostwebui.common.entity.request.ProjectAddUpdateRequest;
import io.knifer.moghostwebui.common.entity.vo.ProjectVO;
import io.knifer.moghostwebui.common.exception.MoException;
import io.knifer.moghostwebui.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * 项目服务
 *
 * @author Knifer
 * @version 1.0.0
 */
@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository repository;

    /**
     * 获取项目列表
     * @param pageRequest 分页参数
     * @return 分页数据
     */
    public Page<ProjectVO> getProjectList(PageRequest pageRequest){
        return repository.findAll(pageRequest).map(ProjectVO::from);
    }

    /**
     * 新增项目
     * @param request 请求参数
     */
    public void add(ProjectAddUpdateRequest request){
        validateNameDuplicated(request);
        repository.save(Project.from(request));
    }

    /**
     * 更新项目信息
     * @param request 请求参数
     */
    public void update(ProjectAddUpdateRequest request){
        Integer id = request.getId();

        if (id == null){
            MoException.throwOut(ErrorCodes.VALIDATION_FAILED);
            return;
        }
        repository.findById(id)
                .ifPresent(p -> {
                    String newName = request.getName();
                    String newRemark = request.getRemark();
                    String name = p.getName();
                    String remark = p.getRemark();
                    boolean updateFlag = false;

                    if (!name.equals(newName)){
                        validateNameDuplicated(request);
                        updateFlag = true;
                    } else if (!remark.equals(newRemark)){
                        updateFlag = true;
                    }
                    if (updateFlag){
                        Project.update(request, p);
                        repository.save(p);
                    }
                });
    }

    /**
     * 项目名称判重
     * @param request 请求参数
     */
    private void validateNameDuplicated(ProjectAddUpdateRequest request) {
        Integer id = request.getId();
        String name = request.getName();
        boolean addFlag = id == null;
        boolean invalid = false;

        if (addFlag) {
            if (repository.existsByName(name)) {
                invalid = true;
            }
        } else if (repository.existsByNameAndIdNot(name, id)){
            invalid = true;
        }
        if (invalid){
            MoException.throwOut(ErrorCodes.VALIDATION_DUPLICATED, "project name duplicated.");
        }
    }
}
