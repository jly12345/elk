package com.jxx.demo.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ExcelUtil {

    public static String sheetName = "成绩";

    public static int dataRow = 0;

    //读取excel
    public static Workbook readExcel(String filePath) {
        Workbook wb = null;
        if(filePath == null) {
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if(".xls".equals(extString)) {
                return wb = new HSSFWorkbook(is);
            } else if(".xlsx".equals(extString)) {
                return wb = new XSSFWorkbook(is);
            } else {
                return wb = null;
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return wb;
    }

    public static Cell getCell(Row row, int i) {
        Cell cell = row.getCell(i);
        if(cell == null) {
            cell = row.createCell(i);
        }
        return cell;
    }


}

