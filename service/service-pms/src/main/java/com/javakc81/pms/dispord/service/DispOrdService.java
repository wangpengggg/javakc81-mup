package com.javakc81.pms.dispord.service;

import com.javakc81.commonutils.jpa.base.service.BaseService;
import com.javakc81.commonutils.jpa.dynamic.SimpleSpecificationBuilder;
import com.javakc81.pms.dispord.dao.DispOrdDao;
import com.javakc81.pms.dispord.entity.DispOrd;
import com.javakc81.pms.dispord.vo.DispOrdQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class DispOrdService extends BaseService<DispOrdDao, DispOrd> {

    @Autowired
    private DispOrdDao dispOrdDao;

    public List<DispOrd> findAll() {
        return dispOrdDao.findAll();
    }

    /**
     * 带条件分页查询
     *
     * @param dispOrdQuery 条件
     * @param pageNo       当前页
     * @param pageSize     最大条数
     * @return 结果
     */
    public Page<DispOrd> findPageDispord(DispOrdQuery dispOrdQuery, int pageNo, int pageSize) {
        // 指定需要查询的条件
        SimpleSpecificationBuilder<DispOrd> simpleSpecificationBuilder = new SimpleSpecificationBuilder<>();
        if (!StringUtils.isEmpty(dispOrdQuery.getOrderName())) {
            /**
             * 可用操作符
             * = 等值、!= 不等值 (字符串、数字)
             * >=、<=、>、< (数字)
             * ge，le，gt，lt(字符串)
             * :表示like %v%
             * l:表示 v%
             * :l表示 %v
             * null表示 is null
             * !null表示 is not null
             */
            simpleSpecificationBuilder.and("orderName", ":", dispOrdQuery.getOrderName());
        }
        if(!StringUtils.isEmpty(dispOrdQuery.getBeginDate())) {
            simpleSpecificationBuilder.and("gmtCreate", "ge", dispOrdQuery.getBeginDate());
        }
        if(!StringUtils.isEmpty(dispOrdQuery.getEndDate())) {
            simpleSpecificationBuilder.and("gmtCreate", "lt", dispOrdQuery.getEndDate());
        }
        Page page = dispOrdDao.findAll(simpleSpecificationBuilder.getSpecification(), PageRequest.of(pageNo - 1, pageSize));
        return page;
    }

}
