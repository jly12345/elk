package com.jxx.demo.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.jxx.demo.ProgressBarContext;
import com.jxx.demo.model.ExcelData;
import com.jxx.demo.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lingyun.jiang
 */
@Slf4j
public class ExcelHeadDataListener extends AnalysisEventListener<Object> {
    Map<ExcelData, Map<String, String>> composeDataListMap;
    Map<String, Integer> headMap = new HashMap<>();
    Boolean flag = Boolean.TRUE;
    String targetPath;

    public ExcelHeadDataListener(Map<ExcelData, Map<String, String>> composeDataListMap, String targetPath) {
        this.composeDataListMap = composeDataListMap;
        this.targetPath = targetPath;
    }

    /**
     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     *
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        log.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
        if(exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            log.error("第{}行，第{}列解析异常，数据为:{}", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData());
        }
    }
    @Override
    public void invoke(Object data, AnalysisContext context) {
        try {
            ExcelUtil.dataRow++;
            if(("课程\n" + "代码").equals(((Map) data).get(1))) {
                Map<Integer, String> tmp = (Map<Integer, String>) data;
                for(Integer key : tmp.keySet()) {
                    if(key > 1) {
                        this.headMap.put(tmp.get(key), key);
                    }
                }
            }
            if(("学号").equals(((Map) data).get(0))) {
                flag = Boolean.FALSE;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        ProgressBarContext.getInstance().setProgress(ProgressBarContext.getInstance().getProgress() + 0.2);
        log.info("解析模板完成,开始行:{},map:{}", ExcelUtil.dataRow++, headMap);
        FileOutputStream fileOut = null;
        Workbook workbook = null;
        try {
            workbook = ExcelUtil.readExcel(this.targetPath);
            Sheet sheet = workbook.getSheet(ExcelUtil.sheetName);
            fileOut = new FileOutputStream(this.targetPath);
            int index = ExcelUtil.dataRow;
            for(ExcelData key : this.composeDataListMap.keySet()) {
                Row row = sheet.getRow(index);
                if(row == null) {
                    row = sheet.createRow(index);
                }
                ExcelUtil.getCell(row, 0).setCellValue(key.getStudentNo());
                ExcelUtil.getCell(row, 1).setCellValue(key.getStudentName());
                Map<String, String> courseMap = this.composeDataListMap.get(key);
                for(String course : courseMap.keySet()) {
                    Integer cellIndex = headMap.get(course);
                    if(cellIndex == null) {
                        log.error("studentNo:{},course:{} 没有导入成功！", key.getStudentNo(), course);
                        continue;
                    }
                    ExcelUtil.getCell(row, cellIndex).setCellValue(courseMap.get(course));
                }
                index++;
            }
            workbook.write(fileOut);
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(workbook);
            IOUtils.closeQuietly(fileOut);
            ExcelUtil.dataRow = 0;
            ProgressBarContext.getInstance().setProgress(1);
        }

    }

    @Override
    public boolean hasNext(AnalysisContext context) {
        return flag;
    }
}
