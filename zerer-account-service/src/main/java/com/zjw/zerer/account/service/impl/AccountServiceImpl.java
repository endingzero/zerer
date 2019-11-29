package com.zjw.zerer.account.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjw.zerer.core.thread.RouterHolder;
import com.zjw.zerer.customdb.account.dto.AccountAddRequest;
import com.zjw.zerer.customdb.account.mapper.AccountMapper;
import com.zjw.zerer.account.service.AccountSequenceService;
import com.zjw.zerer.account.service.AccountService;
import com.zjw.zerer.customdb.account.entity.Account;
import com.zjw.zerer.defaultdb.entity.AccountSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @ClassName: AccountServiceImpl
 * @Description: 账户服务实现类
 * @author: jiewei
 * @date: 2019/8/6
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper,Account> implements AccountService {

    @Autowired
    private AccountSequenceService accountSequenceService;


    /**
     * 添加账户
     * @param request
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(AccountAddRequest request) {
        AccountSequence accountSequence = accountSequenceService.add(request);
        Account account = new Account();
        account.setAccountId(accountSequence.getAccountId());
        account.setCreateTime(LocalDateTime.now());
        account.setActivated(Boolean.TRUE);
        account.setMaster(Boolean.TRUE);
        account.setSalt("0");
        account.setMobile(request.getMobile());
        account.setPassword(request.getPassword());
        RouterHolder.setDefaultRouter(accountSequence.getDbName(),accountSequence.getTableName(),accountSequence.getAccountId());
        this.baseMapper.insert(account);
    }
}
