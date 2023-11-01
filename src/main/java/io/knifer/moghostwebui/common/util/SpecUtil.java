package io.knifer.moghostwebui.common.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;

import javax.annotation.Nullable;

/**
 * Spring Data JPA Specification 相关工具类
 *
 * @author Knifer
 * @version 1.0.0
 */
public final class SpecUtil {

    /**
     * 如果object不为空，向predicate拼接表达式并返回结果
     * @param criteriaBuilder criteria构建器
     * @param predicate predicate
     * @param keyWord 逻辑关键词
     * @param anotherPredicate 要拼接的表达式
     * @return Predicate
     */
    @Nullable
    public static Predicate acceptNonnull(
            CriteriaBuilder criteriaBuilder,
            @Nullable Predicate predicate,
            KeyWord keyWord,
            @Nullable Predicate anotherPredicate
    ) {
        if (anotherPredicate == null){
            return predicate;
        }

        return predicate == null ? anotherPredicate : switch (keyWord){
            case AND -> criteriaBuilder.and(predicate, anotherPredicate);
            case OR -> criteriaBuilder.or(predicate, anotherPredicate);
        };
    }

    public enum KeyWord { AND, OR }

    private SpecUtil(){
        throw new AssertionError();
    }
}
