package com.zjw.zerer.account.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zjw.zerer.core.constants.AppTableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: Account
 * @Description:
 * @author: jiewei
 * @date: 2019/8/6
 */
@Data
@TableName(AppTableName.Account)
public class Account {
    @TableId
    private Long id;

    private String mobile;

    private String password;

    private String salt;

    private Boolean master;

    private Boolean activated;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private LocalDateTime update_pwd;


}
