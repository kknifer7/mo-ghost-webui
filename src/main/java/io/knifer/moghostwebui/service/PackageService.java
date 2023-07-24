package io.knifer.moghostwebui.service;

import io.knifer.moghostwebui.common.tool.file.FileAssessor;
import io.knifer.moghostwebui.repository.PackageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 包服务
 *
 * @author Knifer
 * @version 1.0.0
 */
@Service
@AllArgsConstructor
public class PackageService {

    private final PackageRepository repository;

    private final FileAssessor fileAssessor;

    /**
     * 检查指定包是否准备完毕
     * @param id 包ID
     * @return bool
     */
    public Boolean checkReadyById(Integer id){
        return repository.existsByIdAndReadyTrue(id);
    }

    /**
     * 根据ID删除
     * @param id ID
     */
    @Transactional
    public void deleteById(Integer id){
        repository.findById(id)
                .ifPresent(p -> {
                    if (p.getReady()){
                        repository.delete(p);
                        fileAssessor.deletePackage(p);
                    }
                });
    }
}
