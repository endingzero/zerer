package com.zjw.zerer.miscellaneous.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.zjw.zerer.account.entity.Account;
import com.zjw.zerer.core.exception.ApiException;
import com.zjw.zerer.core.util.EnumCode;
import com.zjw.zerer.core.util.Result;
import com.zjw.zerer.miscellaneous.client.AccountClient;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * @ClassName: MiscellaneousController
 * @Description: 杂项controller
 * @author: jiewei
 * @date: 2019/8/6
 */
@RestController
@RequestMapping("/miscellaneous")
@Slf4j
public class MiscellaneousController {

    @Autowired
    private AccountClient accountClient;

    @GetMapping("/get")
    @ApiOperation(value = "获取账户", notes = "获取员工")
    @HystrixCommand(fallbackMethod = "accountFallBack")
    public Result<Account> get(@RequestParam(value = "id") Long id) {
        long start = System.currentTimeMillis();
        Result<Account> result = accountClient.get(id);
        log.info("time:" + (System.currentTimeMillis() - start));
        if (!Result.CODE_SUCCESS.equals(result.getCode())) {
            throw new ApiException(EnumCode.BAD_REQUEST);
        }

        return Result.ok(result.getData());
    }

    public Result<Account> accountFallBack(Long id){
        return Result.fail();
    }
}
