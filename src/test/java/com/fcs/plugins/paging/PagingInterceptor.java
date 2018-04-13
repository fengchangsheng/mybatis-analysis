package com.fcs.plugins.paging;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
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
    private static final int paramObject_index = 1;
    private static final int rowBounds_index = 2;
    private static final String count_table_alias = "count_temp_table";
    private static final String limit_table_alias = "limit_temp_table";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        Object rowBoundsObject = args[rowBounds_index];
        if (!(rowBoundsObject instanceof PagingRowBound)) {
            logger.debug("> query with no limit. ");
            return invocation.proceed();
        }
        PagingRowBound pagingRowBound = (PagingRowBound) rowBoundsObject;
        MappedStatement mappedStatement = (MappedStatement) args[mappedStatement_index];
        Object paramObject = args[paramObject_index];
        // 重写了一个SqlSource应该执行这个  后面这个就不需要了
        BoundSql boundSql = mappedStatement.getBoundSql(paramObject);
//        BoundSql boundSql = mappedStatement.getSqlSource().getBoundSql(paramObject);
        String sql = boundSql.getSql();
        logger.debug("> original sql: " + sql);
//        String upperCaseSql = sql.toUpperCase();
        String countSql = "select count(*) from (" + sql + ") " + count_table_alias;
        Configuration configuration = mappedStatement.getConfiguration();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        // 构建新的查询组件（没有set方法又环环相扣）
        BoundSql newBoundSql = new BoundSql(configuration, countSql, parameterMappings, paramObject);
        MappedStatement newMappedStatement = createMappedStatementByCopy(mappedStatement, newBoundSql, true);
        Executor executor = (Executor) invocation.getTarget();
        // 真实的分页操作
        String limitSql = "select * from (" + sql + ") " + limit_table_alias + " limit " + pagingRowBound.getOffset() +
                "," + pagingRowBound.getLimit();
        BoundSql limitBoundSql = new BoundSql(configuration, limitSql, parameterMappings, paramObject);
        MappedStatement limitMappedStatement = createMappedStatementByCopy(mappedStatement, limitBoundSql, false);
        args[mappedStatement_index] = limitMappedStatement;
        // 查询总记录数
        logger.debug("> invoke count select ");
        List<Integer> counts = executor.query(newMappedStatement, paramObject , new RowBounds(), null);
        if (counts != null && !counts.isEmpty()) {
            int totalCount = counts.get(0);
            logger.debug("> totalCount: " + totalCount);
            Paging paging = new Paging(pagingRowBound, totalCount);
            Object result = invocation.proceed();
            return new PagingList<Object>((Collection<?>) result, paging);
        }
        return invocation.proceed();
    }

    private MappedStatement createMappedStatementByCopy(MappedStatement oldMappedStatement,
                                                        BoundSql newBoundSql,
                                                        boolean isQueryCount){
        SqlSource sqlSource;
        if (newBoundSql == null) {
            sqlSource = oldMappedStatement.getSqlSource();
        } else {
            sqlSource = new SimpleSqlSource(newBoundSql);
        }
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(
                oldMappedStatement.getConfiguration(),
                oldMappedStatement.getId(), sqlSource,
                oldMappedStatement.getSqlCommandType());
        if (isQueryCount) {
            List<ResultMap> resultMaps = new ArrayList<>();
            ResultMap.Builder inlineResultMapBuilder = new ResultMap.Builder(
                    oldMappedStatement.getConfiguration(),
                    msBuilder.id() + "-Count-Inline",
                    Integer.class,
                    new ArrayList<ResultMapping>(),
                    null);
            resultMaps.add(inlineResultMapBuilder.build());
            msBuilder.resultMaps(resultMaps);
        } else {
            msBuilder.resultMaps(oldMappedStatement.getResultMaps());
        }
        return msBuilder.build();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * Executor执行query方法时会间接通过SqlSource来获取BoundSql :
     *   BoundSql boundSql = ms.getBoundSql(parameterObject);
     *
     * 而原来的DynamicSqlSource#getBoundSql会触发一系列动态解析，获取的时候已经不经意间完成了相关的操作。
     * 后面框架的执行会再次调用mappedStatement来获取BoundSql，又要走一遍，而且这样你改了BoundSql也没用了。
     * 唯一的办法就是重写个SqlSource，把修改过的BoundSql放进去，然后只是简单地提供一个get方法让后面使用这个BoundSql。
     * 分页相关的操作都是基于原SQL的变种，不添加额外的参数，有参数也是直接写到sql中了，基本不用破坏其本身的结构。
     *
     * @see DynamicSqlSource
     */
    private class SimpleSqlSource implements SqlSource{

        private BoundSql boundSql;

        public SimpleSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }

    }
}
