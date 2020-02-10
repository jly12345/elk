package com.jxx.demo.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.FileUtils;
import com.jxx.demo.ProgressBarContext;
import com.jxx.demo.listener.DataListener;
import com.jxx.demo.model.ExcelData;
import javafx.scene.control.Alert;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @author lingyun.jiang
 */
@Slf4j
public class WorkService {

    private String srcPath;
    private String targetPath;
    private String master;

    public WorkService(String srcPath, String targetPath, String master) {
        this.srcPath = srcPath;
        this.master = master;
        this.targetPath = targetPath;
    }


    public Map<String, String> startWork() {
        //check file
        if(! checkFile(this.srcPath, this.targetPath)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("信息");
            String msg = String.format("目录不存在,请检查路径是否存在，待检查路径：%s，%s", this.srcPath, this.targetPath);
            alert.headerTextProperty().set(msg);
            alert.showAndWait();
            return null;
        }

        ProgressBarContext.getInstance().setProgress(ProgressBarContext.getInstance().getProgress() + 0.2);
        log.info("start parse file: {}", this.srcPath);
        InputStream inputStream = null;
        InputStream targetStream = null;
        try {
            targetStream = new FileInputStream(this.targetPath);
            backFile(targetStream);
            EasyExcel.read(this.srcPath, ExcelData.class, new DataListener(master, this.targetPath)).sheet().doRead();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(targetStream);
        }


        return null;
    }

    private void backFile(InputStream targetStream) {
        String bakPath = this.targetPath.substring(0, this.targetPath.lastIndexOf(File.separator));
        String fileName = this.targetPath.substring(this.targetPath.lastIndexOf(File.separator) + 1);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String localTime = df.format(now);
        FileUtils.writeToFile(new File(bakPath + File.separator + localTime + "_" + fileName), targetStream);
    }

    private boolean checkFile(String srcPath, String targetPath) {
        File src = new File(srcPath);
        boolean exists = src.exists();
        if(! exists) {
            return false;
        }
        File target = new File(targetPath);
        exists = target.exists();
        if(! exists) {
            return false;
        }
        return true;
    }
}
