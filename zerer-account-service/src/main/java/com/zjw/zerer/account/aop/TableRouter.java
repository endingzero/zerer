package com.zjw.zerer.account.aop;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import com.zjw.zerer.core.thread.RouterHolder;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class TableRouter{

    /**
     * 多租户属于 SQL 解析部分，依赖 MP 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        /*
         * 【测试多租户】 SQL 解析处理拦截器<br>
         * 这里固定写成住户 1 实际情况你可以从cookie读取，因此数据看不到 【 麻花藤 】 这条记录（ 注意观察 SQL ）<br>
         */
        List<ISqlParser> sqlParserList = new ArrayList<>();
        TenantSqlParser tenantSqlParser = new TenantSqlParser(){
            @Override
            public void processInsert(Insert insert) {
                if (this.getTenantHandler().doTableFilter(insert.getTable().getName())) {
                    // 过滤退出执行
                    return;
                }
                // insert 语句存在shopId则不再添加
                boolean hasTenantIdColumn = insert.getColumns().stream()
                        .anyMatch(c -> c.getColumnName().equals(this.getTenantHandler().getTenantIdColumn()));

                if (hasTenantIdColumn) {
                    return;
                }
                insert.getColumns().add(new Column(this.getTenantHandler().getTenantIdColumn()));
                if (insert.getSelect() != null) {
                    processPlainSelect((PlainSelect) insert.getSelect().getSelectBody(), true);
                } else if (insert.getItemsList() != null) {
                    ((ExpressionList) insert.getItemsList()).getExpressions().add(getTenantHandler().getTenantId());
                } else {
                    throw new MybatisPlusException(
                            "Failed to process multiple-table update, please exclude the tableName or statementId");
                }
            }
        };
        tenantSqlParser.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId() {
                return new LongValue(RouterHolder.getTarget().getAccountId().toString());
            }

            @Override
            public String getTenantIdColumn() {
                return "account_id";
            }

            @Override
            public boolean doTableFilter(String tableName) {
                if (RouterHolder.getTarget() != null && RouterHolder.getTarget().isTenant()
                        && RouterHolder.getTarget().getAccountId() != null) {
                    if (tableName.matches("^.*_[0-9]*$")) {
                        return false;
                    }
                }
                return true;
            }

        });

        sqlParserList.add(tenantSqlParser);
        paginationInterceptor.setSqlParserList(sqlParserList);
//        paginationInterceptor.setSqlParserFilter(new ISqlParserFilter() {
//            @Override
//            public boolean doFilter(MetaObject metaObject) {
//                MappedStatement ms = SqlParserHelper.getMappedStatement(metaObject);
//                // 过滤自定义查询此时无租户信息约束【 麻花藤 】出现
//                if ("com.baomidou.springboot.mapper.UserMapper.selectListBySQL".equals(ms.getId())) {
//                    return true;
//                }
//                return false;
//            }
//        });
        return paginationInterceptor;
    }
}
