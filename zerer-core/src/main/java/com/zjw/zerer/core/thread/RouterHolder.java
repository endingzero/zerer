package com.zjw.zerer.core.thread;

import com.zjw.zerer.core.util.Constants;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: RouterHolder
 * @Description:
 * @author: jiewei
 * @date: 2019/11/29
 */
public class RouterHolder {

    private static final ThreadLocal<Router> LOCAL = new ThreadLocal<>();


    public static Router getTarget() {

        return LOCAL.get();
    }


    public static void setDataSource(String dbName) {
        Router target = LOCAL.get();
        if (target == null) {
            target = new Router();
            LOCAL.set(target);
        }
        target.setDatasource(dbName);
    }

    public static void setTarget(Router holder) {
        LOCAL.set(holder);
    }

    public static void clean() {
        LOCAL.remove();
    }

    public static void setDefaultRouter(String dbName,String tableName, Long accountId) {
        Router router=new Router();
        router.setDatasource(dbName);
        router.setAccountId(accountId);
        router.setTenant(true);
        router.setTableSuffix(Constants.TABLE_ROUTER_SPILT + tableName);
        RouterHolder.setTarget(router);
    }


    @Data
    public static class Router implements Serializable {

        // 数据库名
        private String datasource;

        // 表后缀 _+companyId
        private String tableSuffix;

        // 账户ID
        private Long accountId;

        // 是否执行多租户策略
        private boolean isTenant;
    }
}
