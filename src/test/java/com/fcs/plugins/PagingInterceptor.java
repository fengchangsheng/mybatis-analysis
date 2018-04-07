package com.fcs.plugins;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
@Intercepts({@Signature(
    type = Executor.class,
    method = "query",
    args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
)})
public class PagingInterceptor implements Interceptor {

    private static Logger logger = LoggerFactory.getLogger(PagingInterceptor.class);

    private static final int mappedStatement_index = 0;
    private static final int param_index = 1;
    private static final int rowBounds_index = 2;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[mappedStatement_index];
        Object paramObject = args[param_index];
//        BoundSql boundSql = mappedStatement.getBoundSql(paramObject);
        BoundSql boundSql = mappedStatement.getSqlSource().getBoundSql(paramObject);
        String sql = boundSql.getSql();
        logger.debug("================= sql ================>" + sql);
        String upperCaseSql = sql.toUpperCase();
        logger.debug("================= upperCaseSql ================>" + upperCaseSql);
        String countSql = "select count(*) from (" + sql + ")";
//        Configuration configuration = mappedStatement.getConfiguration();
//        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
//        BoundSql newBoundSql = new BoundSql(configuration, countSql, parameterMappings, paramObject);
        Executor executor = (Executor) invocation.getTarget();
        executor.query(mappedStatement, paramObject , null, null);


        return invocation.proceed();
    }

    private MappedStatement createMappedStatement(MappedStatement oldMappedStatement){
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(oldMappedStatement.getConfiguration(),
                oldMappedStatement.getId(), oldMappedStatement.getSqlSource(),
                oldMappedStatement.getSqlCommandType());
        return msBuilder.build();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
