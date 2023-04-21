package org.jiopi.ibean.kernel.context;

import java.util.concurrent.ConcurrentHashMap;
import org.jiopi.ibean.kernel.util.ObjectAccessor;

/**
 * 
 * a iBean Object Pool to cache singleton Objects 
 * 
 * @since 2010.5.18
 *
 */
public class ObjectPool {

    private final ConcurrentHashMap<String, Object> singletonObjectPool = new ConcurrentHashMap<String, Object>();

    public <T> T get(String name, Class<T> returnType) {
        return ObjectAccessor.processReturnValue(singletonObjectPool.get(name), returnType);
    }

    public void put(String name, Object obj) {
        singletonObjectPool.put(name, ObjectAccessor.filterArg(obj));
    }
}
