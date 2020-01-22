package com.luck.fun.luck.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lingyun.jiang
 * @since 2020-01-19
 */
@Data
public class TError implements Serializable {

    private static final long serialVersionUID = 5310492017110527739L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String msg;

    public TError(String msg) {
        this.msg = msg;
    }
}
