package com.zjw.zerer.account.controller;

import com.zjw.zerer.account.service.AccountService;
import com.zjw.zerer.account.entity.Account;
import com.zjw.zerer.core.exception.ApiException;
import com.zjw.zerer.core.util.EnumCode;
import com.zjw.zerer.core.util.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @ClassName: AccountController
 * @Description:
 * @author: jiewei
 * @date: 2019/7/30
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/get")
    @ApiOperation(value = "获取账户", notes = "获取员工")
    public Result<Account> get(@RequestParam(value = "id") Long id) {
        Account account = accountService.getById(id);
        if(account == null) {
            throw new ApiException(EnumCode.BAD_REQUEST);
        }
        return Result.ok(account);
    }
}
