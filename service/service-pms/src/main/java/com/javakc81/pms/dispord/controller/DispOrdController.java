package com.javakc81.pms.dispord.controller;

import com.alibaba.excel.EasyExcel;
import com.javakc81.commonutils.api.APICODE;
import com.javakc81.pms.dispord.entity.DispOrd;
import com.javakc81.pms.dispord.listener.ExcelListener;
import com.javakc81.pms.dispord.service.DispOrdService;
import com.javakc81.pms.dispord.vo.DispOrdData;
import com.javakc81.pms.dispord.vo.DispOrdQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
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

    @ApiOperation(value = "列表导出", notes = "使用EasyExcel技术进行列表导出")
    @GetMapping("exportEasyExcel")
    public void exportEasyExcel(HttpServletResponse response) {
        // 查询数据库数据
        List<DispOrd> dispOrdList = dispOrdService.findAll();

        // 设置对象封装类的集合
        List<DispOrdData> dispOrdDataList = new ArrayList<>();

        for (DispOrd dispOrd : dispOrdList) {
            // 创建封装类
            DispOrdData dispOrdData = new DispOrdData();
            // copy数据
            BeanUtils.copyProperties(dispOrd, dispOrdData);
            // 添加对象到集合当中
            dispOrdDataList.add(dispOrdData);
        }

        // 导出excel
        String fileName = "dispord";
        try {
            // ## 设置响应信息
            response.reset();
            response.setContentType("application/vnd.ms-excel; charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8") + ".xlsx");
            EasyExcel.write(response.getOutputStream(), DispOrdData.class).sheet("指令列表").doWrite(dispOrdDataList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "列表导入", notes = "使用EasyExcel技术进行列表导入")
    @PostMapping("importEasyExcel")
    public void importEasyExcel(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DispOrdData.class, new ExcelListener(dispOrdService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
