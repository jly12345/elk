package com.luck.fun.luck;

import com.luck.fun.luck.entity.TUserInfo;
import com.luck.fun.luck.service.TUserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration
class LuckApplicationTests {
    @Autowired
    TUserInfoService userInfoService;

    @Test
    public void testUserInfoService() {
        TUserInfo userInfo = new TUserInfo();
        userInfo.setAppid("12345");
        userInfo.setOpenid("12345");
        userInfo.setTenantNo("12345");
        userInfo.setUinfo("222");
        userInfoService.save(userInfo);
    }


}
