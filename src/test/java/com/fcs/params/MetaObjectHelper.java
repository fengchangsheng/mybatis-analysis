package com.fcs.params;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

/**
 * Created by fengcs on 2018/3/2.
 */
public class MetaObjectHelper {

    private static ObjectFactory objectFactory = new DefaultObjectFactory();
    private static ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();

    public static MetaObject newMetaObject(Object object) {
        return MetaObject.forObject(object, objectFactory, objectWrapperFactory);
    }

}
