package com.zjw.zerer.account.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjw.zerer.account.dto.AccountAddRequest;
import com.zjw.zerer.account.entity.Account;
import com.zjw.zerer.account.entity.AccountSequence;
import com.zjw.zerer.account.mapper.AccountMapper;
import com.zjw.zerer.account.mapper.AccountSequenceMapper;
import com.zjw.zerer.account.service.AccountSequenceService;
import com.zjw.zerer.account.service.AccountService;
import com.zjw.zerer.core.util.DbNameGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @ClassName: AccountServiceImpl
 * @Description: 账户服务实现类
 * @author: jiewei
 * @date: 2019/8/6
 */
@Service
public class AccountSequenceServiceImpl extends ServiceImpl<AccountSequenceMapper,AccountSequence> implements AccountSequenceService {

    /**
     * 增加一个账户
     * @param request   请求参数
     * @return          id
     */
    @Override
    public Long add(AccountAddRequest request) {

        AccountSequence accountSequence = new AccountSequence();
        accountSequence.setCreateTime(LocalDateTime.now());
        this.baseMapper.insert(accountSequence);
        accountSequence.setDbName(DbNameGenerator.generateDbName(accountSequence.getAccountId()));
        accountSequence.setTableName(DbNameGenerator.generateTabelName(accountSequence.getAccountId()));
        this.baseMapper.updateById(accountSequence);
        return accountSequence.getAccountId();
    }
}
