package com.luck.fun.luck.config;

import com.luck.fun.luck.entity.TWxConfig;
import com.luck.fun.luck.service.TWxConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;

/**
 * @author Think
 */
@Component
@Slf4j
public class Tenant  {

    public static TWxConfig config ;

    @Autowired
    private TWxConfigService configService;

    @Value("${luck.tenantNo}")
    private String tenantNo;

    @PostConstruct
    public void init() throws Exception {
        config = configService.getById("TN0000000001");
        if(config == null){
            String format = MessageFormat.format("tenantNo = {} is not config in db", tenantNo);
            throw new Exception(format);
        }
    }


}
