package com.luck.fun.luck.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lingyun.jiang
 * @since 2020-01-17
 */
@Data
public class TWxConfig implements Serializable {

    private static final long serialVersionUID = 5488863731743209429L;
    @TableId
    private String tenantNo;

    private String appid;

    private String secret;

    private String token;

}
