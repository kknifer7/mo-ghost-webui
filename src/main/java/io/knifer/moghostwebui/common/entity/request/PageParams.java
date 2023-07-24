package io.knifer.moghostwebui.common.entity.request;

import com.google.common.collect.Lists;
import io.knifer.moghostwebui.common.constant.UtilConstants;
import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.query.sqm.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 分页请求参数对象
 *
 * @author Knifer
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class PageParams {

    /**
     * 页数
     */
    private Integer page = 0;

    /**
     * 数据量
     */
    private Integer size = 10;

    /**
     * 排序规则
     * eg: sorts=ctime,asc,projectId,desc
     * 上述eg的意思为: 按ctime字段顺序排序，按projectId字段倒序排序
     */
    private String sorts;

    /**
     * 转为PageRequest
     * 本方法主要处理PageParams中的排序字段（sorts）。然后将其转为PageRequest对象
     * @return PageRequest
     */
    public PageRequest toPageRequest(){
        List<Sort.Order> orders;
        List<String> sortsSplit;
        int len;
        String attrName;
        SortOrder sortOrder;

        if (StringUtils.isBlank(sorts)){
            return PageRequest.of(page, size);
        }
        sortsSplit = UtilConstants.COMMA_SPLITTER.splitToList(sorts);
        len = sortsSplit.size();
        if (len < 2){
            return PageRequest.of(page, size);
        }
        orders = Lists.newArrayListWithCapacity(len / 2);
        for (int i = 1; i < len; i += 2){
            attrName = sortsSplit.get(i - 1);
            sortOrder = resolveSortOrder(sortsSplit.get(i));
            orders.add(switch (sortOrder){
                case ASCENDING -> Sort.Order.asc(attrName);
                case DESCENDING -> Sort.Order.desc(attrName);
            });
        }

        return PageRequest.of(page, size, Sort.by(orders));
    }

    private SortOrder resolveSortOrder(String sortOrder){
        try {
            return SortOrder.interpret(sortOrder);
        }catch (IllegalArgumentException e){
            return SortOrder.ASCENDING;
        }
    }
}
