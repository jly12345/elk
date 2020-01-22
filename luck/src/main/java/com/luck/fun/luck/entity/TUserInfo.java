package com.luck.fun.luck.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lingyun.jiang
 * @since 2020-01-17
 */
@Data
public class TUserInfo implements Serializable {

    private static final long serialVersionUID = -4598120297410948476L;
    private String tenantNo;

    private String appid;
    @TableId
    private String openid;

    private String uinfo;

}
