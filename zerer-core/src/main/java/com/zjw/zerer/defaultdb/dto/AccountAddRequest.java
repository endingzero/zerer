package com.zjw.zerer.defaultdb.dto;

import lombok.Data;

/**
 * @ClassName: AccountAddRequest
 * @Description: 添加账户
 * @author: jiewei
 * @date: 2019/11/27
 */
@Data
public class AccountAddRequest {

    private String mobile;

    private String password;
}
