package com.zjw.zerer.account.service;

import com.zjw.zerer.account.dto.AccountAddRequest;
import com.zjw.zerer.core.base.BaseService;
import com.zjw.zerer.account.entity.Account;

/**
 * @ClassName: AccountService
 * @Description: 账户服务
 * @author: jiewei
 * @date: 2019/8/6
 */
public interface AccountService extends BaseService<Account> {
    /**
     * 添加账户
     * @param request
     */
    void add(AccountAddRequest request);
}
