package com.zjw.zerer.miscellaneous.client;

import com.zjw.zerer.account.entity.Account;
import com.zjw.zerer.core.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @ClassName: AccountClient
 * @Description: 账户客户端
 * @author: jiewei
 * @date: 2019/8/6
 */
@FeignClient(value = "account-service")
public interface AccountClient {

    @GetMapping("/accountservice/account/get")
    Result<Account> get(@RequestParam(value = "id") Long id);

}
