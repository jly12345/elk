package com.jxx.demo.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author lingyun.jiang
 */
@Data
@NoArgsConstructor
public class ExcelData implements Serializable, Comparable<ExcelData> {

    private static final long serialVersionUID = 2212256310149268744L;
    @ExcelProperty("学号")
    private String studentNo;

    @ExcelProperty("姓名")
    private String studentName;

    @ExcelProperty("课程id")
    private String courseId;

    @ExcelProperty("综合成绩")
    private String score;

    @ExcelProperty("班主任")
    private String master;

    public ExcelData(String studentNo, String studentName) {
        this.studentNo = studentNo;
        this.studentName = studentName;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        ExcelData excelData = (ExcelData) o;
        return Objects.equals(studentNo, excelData.studentNo) &&
                Objects.equals(studentName, excelData.studentName);

    }

    @Override
    public int hashCode() {
        return Objects.hash(studentNo, studentName);
    }

    @Override
    public int compareTo(ExcelData o) {
        long a = 0;
        long b = 0;
        try {
            a = Long.parseLong(studentNo);
            b = Long.parseLong(o.getStudentNo());
        } catch(NumberFormatException e) {
        }
        long result = a - b;
        if(result > 0) {
            return 1;
        } else if(result < 0) {
            return - 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "ExcelData{" +
                "studentNo='" + studentNo + '\'' +
                ", studentName='" + studentName + '\'' +
                '}';
    }
}
