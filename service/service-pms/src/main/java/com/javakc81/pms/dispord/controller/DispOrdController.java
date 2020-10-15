package com.javakc81.pms.dispord.controller;

import com.javakc81.commonutils.api.APICODE;
import com.javakc81.pms.dispord.entity.DispOrd;
import com.javakc81.pms.dispord.service.DispOrdService;
import com.javakc81.pms.dispord.vo.DispOrdQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "调度指令库 - 控制层")
@RestController
@RequestMapping("/pms/dispord")
@CrossOrigin
public class DispOrdController {


    @Autowired
    private DispOrdService dispOrdService;

    /**
     * 查询所有调度指令库
     *
     * @return 结果集
     */
    @ApiOperation(value = "查询所有调度指令库")
    @GetMapping
    public APICODE findAll() {
        // 查询
        List<DispOrd> dispOrdList = dispOrdService.findAll();
        return APICODE.OK().data("items", dispOrdList);
    }

    @ApiOperation(value = "带条件的分页查询")
    @PostMapping("{pageNo}/{pageSize}")
    public APICODE findPageDispOrd(@RequestBody(required = false) DispOrdQuery dispOrdQuery,
                                   @PathVariable(name = "pageNo") int pageNo,
                                   @PathVariable(name = "pageSize") int pageSize) {

        Page<DispOrd> page = dispOrdService.findPageDispord(dispOrdQuery, pageNo, pageSize);
        long totalElements = page.getTotalElements();
        List<DispOrd> dispOrdList = page.getContent();
        return APICODE.OK().data("total", totalElements).data("items", dispOrdList);
    }

    @ApiOperation(value = "新增调度指令库")
    @PostMapping("saveDispOrd")
    public APICODE saveDispOrd(@RequestBody DispOrd dispOrd) {
        dispOrdService.saveOrUpdate(dispOrd);
        return APICODE.OK();
    }

    @GetMapping("{dispOrdId}")
    @ApiOperation(value = "根据ID查询调度指令库管理")
    public APICODE getDispOrdById(@PathVariable String dispOrdId) {
        DispOrd dispOrd = dispOrdService.getById(dispOrdId);
        return APICODE.OK().data("dispOrd", dispOrd);
    }

    @ApiOperation(value = "根据ID修改调度指令库管理")
    @PutMapping
    public APICODE updateDispOrd(@RequestBody DispOrd dispOrd) {
        dispOrdService.saveOrUpdate(dispOrd);
        return APICODE.OK();
    }

    @DeleteMapping("{dispOrdId}")
    @ApiOperation(value = "根据ID删除调度指令库管理")
    public APICODE deleteDispOrd(@PathVariable(name = "dispOrdId") String dispOrdId) {
        dispOrdService.removeById(dispOrdId);
        return APICODE.OK();
    }

}
