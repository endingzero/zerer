package com.zjw.zerer.account.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjw.zerer.account.dto.AccountAddRequest;
import com.zjw.zerer.account.mapper.AccountMapper;
import com.zjw.zerer.account.service.AccountSequenceService;
import com.zjw.zerer.account.service.AccountService;
import com.zjw.zerer.account.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

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
    public void add(AccountAddRequest request) {
        Long accountId = accountSequenceService.add(request);
        Account account = new Account();
        account.setAccountId(accountId);
        account.setCreateTime(LocalDateTime.now());
        account.setActivated(Boolean.TRUE);
        account.setMaster(Boolean.TRUE);
        account.setSalt(String.valueOf(Math.random()*9+1));
        account.setMobile(request.getMobile());
        account.setPassword(request.getPassword());
        this.baseMapper.insert(account);
    }
}
