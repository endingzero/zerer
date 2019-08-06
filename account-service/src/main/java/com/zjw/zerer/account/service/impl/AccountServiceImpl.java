package com.zjw.zerer.account.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjw.zerer.account.mapper.AccountMapper;
import com.zjw.zerer.account.service.AccountService;
import com.zjw.zerer.account.entity.Account;
import org.springframework.stereotype.Service;

/**
 * @ClassName: AccountServiceImpl
 * @Description: 账户服务实现类
 * @author: jiewei
 * @date: 2019/8/6
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper,Account> implements AccountService {
}
