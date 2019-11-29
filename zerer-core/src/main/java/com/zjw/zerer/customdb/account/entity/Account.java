package com.zjw.zerer.customdb.account.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
@TableName(AppTableName.ACCOUNT)
public class Account {

    @TableId(type = IdType.INPUT)
    private Long accountId;

    private String mobile;

    private String password;

    private String salt;

    private Boolean master;

    private Boolean activated;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean update_pwd;


}
