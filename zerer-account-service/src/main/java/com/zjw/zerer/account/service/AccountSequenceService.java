package com.zjw.zerer.account.service;

import com.zjw.zerer.account.dto.AccountAddRequest;
import com.zjw.zerer.core.base.BaseService;
import com.zjw.zerer.defaultdb.entity.AccountSequence;

/**
 * @ClassName: AccountService
 * @Description: 账户服务
 * @author: jiewei
 * @date: 2019/8/6
 */
public interface AccountSequenceService extends BaseService<AccountSequence> {

    /**
     * 增加一个账户
     * @param request   请求参数
     * @return          返回id
     */
    Long add(AccountAddRequest request);
}
