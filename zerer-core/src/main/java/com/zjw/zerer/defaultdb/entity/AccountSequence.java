package com.zjw.zerer.defaultdb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zjw.zerer.core.constants.AppTableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: AccountSequence
 * @Description:
 * @author: jiewei
 * @date: 2019/11/27
 */
@Data
@TableName(AppTableName.ACCOUNT_SEQUENCE)
public class AccountSequence {

    @TableId(type = IdType.AUTO)
    private Long accountId;

    private String dbName;

    private String tableName;

    private LocalDateTime createTime;
}
