package com.zjw.zerer.account.aop;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import com.zjw.zerer.core.thread.RouterHolder;
import com.zjw.zerer.core.util.Constants;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.RowBounds;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * @ClassName: TableRouter
 * @Description:
 * @author: jiewei
 * @date: 2019/11/29
 */
@Component
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class TableRouter extends AbstractSqlParserHandler implements Interceptor {

    /**
     * 模仿PaginationInterceptor
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        buildShopTenantSqlParser();
        this.sqlParser(metaObject);

        // 针对定义了rowBounds，做为mapper接口方法的参数
        String originalSql = (String) metaObject.getValue(PluginUtils.DELEGATE_BOUNDSQL_SQL);
        if (RouterHolder.getTarget() != null && RouterHolder.getTarget().getTableSuffix() != null) {
            originalSql = originalSql.replaceAll(Constants.TABLE_SUFFIX, RouterHolder.getTarget().getTableSuffix());
            metaObject.setValue(PluginUtils.DELEGATE_BOUNDSQL_SQL, originalSql);
            metaObject.setValue(Constants.DELEGATE_ROWBOUNDS_OFFSET, RowBounds.NO_ROW_OFFSET);
            metaObject.setValue(Constants.DELEGATE_ROWBOUNDS_LIMIT, RowBounds.NO_ROW_LIMIT);
        }
        return invocation.proceed();
    }

    /**
     * 多租户属于 SQL 解析部分，依赖 MP 分页插件
     */
    public void buildShopTenantSqlParser() {
        List<ISqlParser> sqlParserList = new ArrayList<>();
        TenantSqlParser tenantSqlParser = new TenantSqlParser() {
            @Override
            public void processInsert(Insert insert) {
                if (this.getTenantHandler().doTableFilter(insert.getTable().getName())) {
                    // 过滤退出执行
                    return;
                }
                // insert 语句存在shopId则不再添加
                boolean hasTenantIdColumn = insert.getColumns().stream()
                        .anyMatch(c -> c.getColumnName().toLowerCase().equals(this.getTenantHandler().getTenantIdColumn().toLowerCase()));

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
        this.setSqlParserList(sqlParserList);
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
