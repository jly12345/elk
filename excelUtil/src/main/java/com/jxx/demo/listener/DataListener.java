package com.jxx.demo.listener;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.jxx.demo.ProgressBarContext;
import com.jxx.demo.model.ExcelData;
import com.jxx.demo.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
public class DataListener extends AnalysisEventListener<ExcelData> {
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 500;
    private String master;
    private String targetPath;
    List<ExcelData> list = new ArrayList<>();
    Map<ExcelData, Map<String, String>> composeDataListMap = new TreeMap<>();

    public DataListener(String master, String targetPath) {
        this.master = master;
        this.targetPath = targetPath;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(ExcelData data, AnalysisContext context) {
        if(master.equals(data.getMaster())) {
            list.add(data);
            // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
//            if (list.size() >= BATCH_COUNT) {
//                // 存储完成清理 list
//                list.clear();
//            }
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        ProgressBarContext.getInstance().setProgress(ProgressBarContext.getInstance().getProgress() + 0.3);
        log.info("所有数据解析完成！总共:{}", list.size());
        composeData(list);


    }

    private void composeData(List<ExcelData> list) {
        Map<ExcelData, Map<String, String>> collect = list.parallelStream().collect(Collectors.groupingBy(e -> fetchGroupKey(e), Collectors.toMap(ExcelData :: getCourseId, ExcelData :: getScore)));
        Iterator<ExcelData> iterator = collect.keySet().iterator();
        while(iterator.hasNext()) {
            ExcelData objKey = iterator.next();
            Map<String, String> objValue = collect.get(objKey);
            composeDataListMap.put(objKey, objValue);
        }

        list.clear();

        Iterator<ExcelData> iter = composeDataListMap.keySet().iterator();
        while(iter.hasNext()) {
            ExcelData key = iter.next();
            Map<String, String> value = composeDataListMap.get(key);
            log.info("key:{},{}", key, value);

        }
        EasyExcel.read(this.targetPath, new ExcelHeadDataListener(composeDataListMap, this.targetPath)).sheet(ExcelUtil.sheetName).doRead();
    }

    private ExcelData fetchGroupKey(ExcelData e) {
        ExcelData composeData = new ExcelData(e.getStudentNo(), e.getStudentName());
        return composeData;
    }


}
