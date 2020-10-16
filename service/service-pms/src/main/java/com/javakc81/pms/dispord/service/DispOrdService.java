package com.javakc81.pms.dispord.service;

import com.javakc81.commonutils.jpa.base.service.BaseService;
import com.javakc81.commonutils.jpa.dynamic.SimpleSpecificationBuilder;
import com.javakc81.pms.dispord.dao.DispOrdDao;
import com.javakc81.pms.dispord.entity.DispOrd;
import com.javakc81.pms.dispord.vo.DispOrdQuery;
import org.apache.poi.hssf.usermodel.HSSFAnchor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        if (!StringUtils.isEmpty(dispOrdQuery.getBeginDate())) {
            simpleSpecificationBuilder.and("gmtCreate", "ge", dispOrdQuery.getBeginDate());
        }
        if (!StringUtils.isEmpty(dispOrdQuery.getEndDate())) {
            simpleSpecificationBuilder.and("gmtCreate", "lt", dispOrdQuery.getEndDate());
        }
        Page page = dispOrdDao.findAll(simpleSpecificationBuilder.getSpecification(), PageRequest.of(pageNo - 1, pageSize));
        return page;
    }

    /**
     * 导出 Excel
     */
    public void exportExcel(HttpServletResponse response) {
        // 设置表头
        String[] titles = {"指令名称", "优先级", "指令类型", "指令描述"};

        // 1.创建一个workbook（工作簿），他是一个Excel文件
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        // 2.创建sheet
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("调度指令库");
        // 3.创建第一行，表头
        HSSFRow hssfRow = hssfSheet.createRow(0);
        // 4.设置表头数据
        for (int i = 0; i < titles.length; i++) {
            hssfRow.createCell(i).setCellValue(titles[i]);
        }
        // 5.查询数据
        List<DispOrd> ordList = dao.findAll();

        // 6.设置其余行的数据
        if (null != ordList) {
            for (int i = 0; i < ordList.size(); i++) {
                DispOrd dispOrd = ordList.get(i);
                // 不能再创建第0行
                HSSFRow row = hssfSheet.createRow(i + 1);
                row.createCell(0).setCellValue(dispOrd.getOrderName());
                row.createCell(1).setCellValue(dispOrd.getPriority());
                row.createCell(2).setCellValue(dispOrd.getSpecType());
                row.createCell(3).setCellValue(dispOrd.getOrderDesc());
            }
        }

        String fileName = new String(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        // 导出Excel
        try {
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
            ServletOutputStream outputStream = response.getOutputStream();
            hssfWorkbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入Excel
     * @param file
     */
    @Transactional
    public void importExcel(MultipartFile file) {
        try {
            // 得到文件流
            InputStream inputStream = file.getInputStream();
            // 创建workbook接口
            Workbook workbook = null;
            // 设置workbook版本
            if (file.getOriginalFilename().endsWith(".xlsx")) {
                // 标识2003及以上版本 后缀为.xlsx
                workbook = new XSSFWorkbook(inputStream);
            } else {
                workbook = new HSSFWorkbook(inputStream);
            }
            // 得到当前ExcelSheet的总数
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                // 创建DispOrd集合
                List<DispOrd> dispOrdList = new ArrayList<>();
                Sheet sheet = workbook.getSheetAt(i);
                // 获取一共有多少行
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                for (int j = 1; j < physicalNumberOfRows; j++) {
                    // 创建实体对象
                    DispOrd dispOrd = new DispOrd();
                    Row row = sheet.getRow(j);
                    dispOrd.setOrderName(row.getCell(0).getStringCellValue());
                    dispOrd.setPriority((int) row.getCell(1).getNumericCellValue());
                    dispOrd.setSpecType((int) row.getCell(2).getNumericCellValue());
                    dispOrd.setOrderDesc(row.getCell(3).getStringCellValue());
                    // 设置到集合当中
                    dispOrdList.add(dispOrd);
                }
                // 批量添加
                dao.saveAll(dispOrdList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
