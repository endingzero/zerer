package com.zjw.zerer.accountservice.controller;

import com.zjw.zerer.core.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/get")
    public Result<String> get() {
        return Result.ok();
    }
}
