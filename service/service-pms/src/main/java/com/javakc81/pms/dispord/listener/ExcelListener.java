package com.javakc81.pms.dispord.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.javakc81.pms.dispord.entity.DispOrd;
import com.javakc81.pms.dispord.service.DispOrdService;
import com.javakc81.pms.dispord.vo.DispOrdData;
import org.springframework.beans.BeanUtils;

public class ExcelListener extends AnalysisEventListener<DispOrdData> {

    private DispOrdService dispOrdService;

    public ExcelListener() {

    }

    public ExcelListener(DispOrdService dispOrdService) {
        this.dispOrdService = dispOrdService;
    }

    /**
     * 每次读取一行时执行此方法
     *
     * @param dispOrdData
     * @param analysisContext
     */
    @Override
    public void invoke(DispOrdData dispOrdData, AnalysisContext analysisContext) {
        // 创建实体类
        DispOrd dispOrd = new DispOrd();
        // copy数据
        BeanUtils.copyProperties(dispOrdData, dispOrd);
        // 保存
        dispOrdService.saveOrUpdate(dispOrd);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
