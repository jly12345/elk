import com.jxx.demo.model.ExcelData;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Test {

    public static void main(String[] args) {
        Map<ExcelData, Map<String, String>> map = new TreeMap<>();
        ExcelData a = new ExcelData("0001", "test01");
        ExcelData b = new ExcelData("0002", "test02");

        Map<String, String> avalue = new HashMap<>();
        avalue.put("aa", "1234");
        avalue.put("bb", "2345");

        Map<String, String> bvalue = new HashMap<>();
        bvalue.put("cc", "12234");
        bvalue.put("dd", "232245");

        map.put(a, avalue);
        map.put(b, bvalue);


//        for (Map.Entry<ExcelData, Map<String, String>> en : map.entrySet()) {
//            System.out.println(en.getKey()+ "---"+ en.getValue());
//        }
        System.out.println(map.get(a));

    }
}
