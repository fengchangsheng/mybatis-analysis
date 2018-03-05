package com.fcs.params;

import ognl.OgnlException;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.xmltags.OgnlCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fengcs on 2018/3/2.
 * Ognl相关测试
 */
public class ParamBindTest {

    static {
        OgnlRuntime.setPropertyAccessor(ParamBindTest.ContextMap.class, new ParamBindTest.ContextAccessor());
    }

    private final ParamBindTest.ContextMap bindings;
    public static final String PARAMETER_OBJECT_KEY = "_parameter";
    public static final String DATABASE_ID_KEY = "_databaseId";

    public ParamBindTest(Object parameterObject) {
        if (parameterObject != null && !(parameterObject instanceof Map)) {
            MetaObject metaObject = MetaObjectHelper.newMetaObject(parameterObject);
            bindings = new ContextMap(metaObject);
        } else {// 参数为null或者是Map类型
            bindings = new ContextMap(null);
        }
        bindings.put(PARAMETER_OBJECT_KEY, parameterObject);
        bindings.put(DATABASE_ID_KEY, "Mysql");
    }

    public static void main(String[] args) {
        Map parameterObject = new HashMap();
        List idList = new ArrayList<Integer>();
        idList.add(1);
        parameterObject.put("list", idList);
        ParamBindTest testMetaObject = new ParamBindTest(parameterObject);
        Object value = OgnlCache.getValue("list", testMetaObject.getBindings());
        System.out.println(value);
    }

    public Map<String, Object> getBindings() {
        return bindings;
    }

    public void bind(String name, Object value) {
        bindings.put(name, value);
    }

    static class ContextMap extends HashMap<String, Object> {
        private static final long serialVersionUID = 2977601501966151582L;

        private MetaObject parameterMetaObject;
        public ContextMap(MetaObject parameterMetaObject) {
            this.parameterMetaObject = parameterMetaObject;
        }

        @Override
        public Object get(Object key) {
            String strKey = (String) key;
            if (super.containsKey(strKey)) {
                return super.get(strKey);
            }

            if (parameterMetaObject != null) {
                Object object = parameterMetaObject.getValue(strKey);
                if (object != null) {
                    super.put(strKey, object);
                }

                return object;
            }

            return null;
        }
    }

    static class ContextAccessor implements PropertyAccessor {

        public Object getProperty(Map context, Object target, Object name)
                throws OgnlException {
            Map map = (Map) target;

            Object result = map.get(name);
            if (result != null) {
                return result;
            }

            Object parameterObject = map.get(PARAMETER_OBJECT_KEY);
            if (parameterObject instanceof Map) {
                return ((Map)parameterObject).get(name);
            }

            return null;
        }

        public void setProperty(Map context, Object target, Object name, Object value)
                throws OgnlException {
            Map map = (Map) target;
            map.put(name, value);
        }
    }
}
