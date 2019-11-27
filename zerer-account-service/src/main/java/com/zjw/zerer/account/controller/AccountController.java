package com.zjw.zerer.account.controller;

import com.zjw.zerer.account.dto.AccountAddRequest;
import com.zjw.zerer.account.service.AccountService;
import com.zjw.zerer.account.entity.Account;
import com.zjw.zerer.core.exception.ApiException;
import com.zjw.zerer.core.util.EnumCode;
import com.zjw.zerer.core.util.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Random;


/**
 * @ClassName: AccountController
 * @Description:
 * @author: jiewei
 * @date: 2019/7/30
 */
@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/add")
    @ApiOperation(value = "添加账户", notes = "添加账户")
    public Result<Void> add(@RequestParam(value = "id") AccountAddRequest request) {
        accountService.add(request);
        return Result.ok();
    }

    @GetMapping("/get")
    @ApiOperation(value = "获取账户", notes = "获取员工")
    public Result<Account> get(@RequestParam(value = "id") Long id) {
        Account account = accountService.getById(id);
        if(account == null) {
            throw new ApiException(EnumCode.BAD_REQUEST);
        }
        //测试熔断默认时间为2000ms
//        int sleepTime = new Random().nextInt(6000);
//        try {
//            Thread.sleep(Long.valueOf(sleepTime));
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return Result.ok(account);
    }
}
