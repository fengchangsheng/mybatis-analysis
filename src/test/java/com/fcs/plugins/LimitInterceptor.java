package com.fcs.plugins;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Properties;

@Intercepts({@Signature(
    type = StatementHandler.class,
    method = "prepare",
    args = {Connection.class}
)})
public class LimitInterceptor implements Interceptor {

    private static Logger logger = LoggerFactory.getLogger(LimitInterceptor.class);

    private static String table_alias = "LIMIT_TABLE_OWN";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();

        String sql = boundSql.getSql();
        logger.debug("sql =======>" + sql);
        String countSql = "select count(*) from (" + sql + ")";

        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        // 层层剥离
        while (metaObject.hasGetter("h")) {
            Object object = metaObject.getValue("h");
            metaObject = SystemMetaObject.forObject(object);
        }
        if (sql != null && sql.toUpperCase().trim().indexOf("SELECT") == 0) {
            PagingRowBound pagingRowBound = (PagingRowBound) metaObject.getValue("delegate.rowBounds");
            int offset = pagingRowBound.getOffset();
            int limit = pagingRowBound.getLimit();
            String limitSql = "select * from (" + sql + ") "+ table_alias + " limit " + offset + "," + limit;
            logger.debug("limitSql =======>" + limitSql);
            metaObject.setValue("delegate.boundSql.sql", limitSql);
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
