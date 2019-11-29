package com.zjw.zerer.core.util;


public class DbNameGenerator {

    private final static int ONE_DB_MAX_ID = 100;

    private final static int TWO_DB_MAX_ID = 200;

    private final static int FOUR_DB_MAX_ID = 400;

    public static String generateDbName(Long accountId) {

        StringBuilder sb = new StringBuilder();
        sb.append(Constants.DB_NAME_PREFIX).append(Constants.TABLE_ROUTER_SPILT);
        // 数据库的分界
        if (accountId <= ONE_DB_MAX_ID) {
            // 此时一个库
            sb.append(0);
        }else if (accountId <= TWO_DB_MAX_ID) {
            // 此时两个库
            sb.append(accountId % 2);
        } else if (accountId <= FOUR_DB_MAX_ID) {
            // 此时四个库
            sb.append(accountId % 2 + 2);
        }
        return sb.toString();
    }


    public static String generateTabelName(Long accountId) {

        StringBuilder sb = new StringBuilder();
        // 数据库的分界
        if (accountId <= ONE_DB_MAX_ID) {
            // 此时一个库
            sb.append(accountId % 2);
        }else if (accountId <= TWO_DB_MAX_ID) {
            // 此时两个库
            sb.append(accountId % 2).append(Constants.TABLE_ROUTER_SPILT).append(1);
        } else if (accountId <= FOUR_DB_MAX_ID) {
            // 此时四个库
            sb.append(accountId % 4).append(Constants.TABLE_ROUTER_SPILT).append(2);
        }
        return sb.toString();
    }

}
